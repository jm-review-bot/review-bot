package spring.app.core.steps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.NoNumbersEnteredException;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.Review;
import spring.app.model.StudentReview;
import spring.app.model.Theme;
import spring.app.model.User;
import spring.app.service.abstraction.ReviewService;
import spring.app.service.abstraction.StorageService;
import spring.app.service.abstraction.StudentReviewService;
import spring.app.service.abstraction.ThemeService;
import spring.app.util.StringParser;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Comparator;
import java.util.ArrayList;

import static spring.app.core.StepSelector.USER_PASS_REVIEW_ADD_THEME;
import static spring.app.core.StepSelector.USER_PASS_REVIEW_GET_LIST_REVIEW;
import static spring.app.core.StepSelector.USER_PASS_REVIEW_ADD_STUDENT_REVIEW;
import static spring.app.core.StepSelector.START;
import static spring.app.util.Keyboards.DEF_BACK_KB;


@Component
public class UserPassReviewGetListReview extends Step {

    private final StorageService storageService;
    private final ThemeService themeService;
    private final ReviewService reviewService;
    private final StudentReviewService studentReviewService;
    private final Map<Integer, Map<Integer, Long>> reviewsIndex = new HashMap<>();//по vkId хранит позицию (в списке выбора ревью для записи [этот список выведен был пользователю])
    // и айди ревью соответственно

    @Autowired
    public UserPassReviewGetListReview(StorageService storageService, ThemeService themeService,
                                       ReviewService reviewService, StudentReviewService studentReviewService) {
        //у шага нет статического текста, но есть статические(видимые независимо от юзера) кнопки
        super("", DEF_BACK_KB);
        this.storageService = storageService;
        this.themeService = themeService;
        this.reviewService = reviewService;
        this.studentReviewService = studentReviewService;
    }

    @Override
    public void enter(BotContext context) {
        Integer vkId = context.getVkId();
        //с прошлошо шага получаем ID темы и по нему из запроса получаем тему
        Theme theme = themeService.getThemeById(Long.parseLong(storageService.getUserStorage(vkId, USER_PASS_REVIEW_ADD_THEME).get(0)));
        // получаю список созданных мною ревью, если они имеется
        //получаю список ревью по теме
        List<Review> reviewsAll = reviewService.getAllReviewsByTheme(context.getUser().getId(), theme, LocalDateTime.now());

        //список ревью сортирую по дате
        reviewsAll.sort(Comparator.comparing(Review::getDate));
        //сохраняю в коллекцию id ревью и присваиваю им порядковые номера, при этом формирую список ревью для вывода
        Map<Integer, Long> indexMap = new HashMap<>();
        for (int i = 0; i < reviewsAll.size(); i++) {
            indexMap.put(i+1, reviewsAll.get(i).getId());
        }
        reviewsIndex.put(vkId, indexMap);
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException, NoNumbersEnteredException {
        Integer vkId = context.getVkId();
        String currentInput = context.getInput();
        if (StringParser.isNumeric(currentInput)) {
            Integer command = StringParser.toNumbersSet(currentInput).iterator().next();
            //получаю список ревью, которые вывелись пользователю
            Map<Integer, Long> allReviewsAndIndex = reviewsIndex.get(vkId);
            //из списка ревью по порядковому номеру получаю id ревью, если null - значит введен номер ревью не доступный в списке
            Long idReview = allReviewsAndIndex.get(command) != null ? allReviewsAndIndex.get(command) : 0L;
            if (idReview > 0L) {
                Review review = reviewService.getReviewById(idReview);
                //проверяю остались ли свободные места в выбранном ревью
                if (studentReviewService.getNumberStudentReviewByIdReview(review.getId()) < 3) {
                    studentReviewService.addStudentReview(new StudentReview(context.getUser(), review));
                    //сохраняю дату ревью для следующего шага и очищаю данные в Storage для этого шага
                    List<String> list = new ArrayList<>();
                    list.add(StringParser.localDateTimeToString(review.getDate()));
                    storageService.updateUserStorage(vkId, USER_PASS_REVIEW_GET_LIST_REVIEW, list);
                    storageService.removeUserStorage(vkId, USER_PASS_REVIEW_ADD_THEME);
                    //удаляю запись по id из списка, т.к. при удалении записи на ревью и повторной записи, выводится
                    //сообщение: Выбранное Вами ревью уже заполнено!
                    reviewsIndex.keySet().remove(vkId);
                    sendUserToNextStep(context, USER_PASS_REVIEW_ADD_STUDENT_REVIEW);
                } else {
                    throw new ProcessInputException("Выбранное Вами ревью уже заполнено!\n\nВыбери удобное время и дату для записи, всё время и дата указывается в МСК " +
                            "часовом поясе, для выбора отправь в ответе число соответствующее удобному для тебя времени.\n\n");
                }
                // если idReview равно отрицательному числу, значит запись на данное ревью не доступна,
                // т.к. в это время я сам принимаю ревью
            } else if (idReview < 0L) {
                //сюда мы могли попасть только если выбранное ревью пересекается с одним из наших
                //сейчас мы сюда попасть не можем (т. к. проверка на пересечение исключена)
                // получаю ревью на которое нет возможности записаться
                Review reviewNoAccess = reviewService.getReviewById(Math.abs(idReview));
                // получаю список моих ревью, которые пересекаются с выбранным ревью
                List<Review> reviewsMy = reviewService.getMyReviewForDate(vkId, reviewNoAccess.getDate(), 59);

                StringBuilder reviewList = new StringBuilder("Не возможно записаться на данное ревью. В это время вы уже назначены проверящим на ревью:\n");
                Integer i = 1;
                for (Review review : reviewsMy) {
                    reviewList.append(i)
                            .append(". ")
                            .append("Тема: " + review.getTheme().getTitle() + " ")
                            .append("Дата: " + StringParser.localDateTimeToString(review.getDate()) + "\n");
                    i++;
                }

                throw new ProcessInputException(reviewList.toString());
            } else {
                throw new ProcessInputException("Введен неверный номер ревью...");
            }
        } else {
            //определяем нажатую кнопку или сообщаем о неверной команде
            String command = StringParser.toWordsArray(context.getInput())[0];
            if ("/start".equals(command)) {
                reviewsIndex.keySet().remove(vkId);
                storageService.removeUserStorage(vkId, USER_PASS_REVIEW_ADD_THEME);
                sendUserToNextStep(context, START);
            } else if ("назад".equals(command)) {
                reviewsIndex.keySet().remove(vkId);
                storageService.removeUserStorage(vkId, USER_PASS_REVIEW_ADD_THEME);
                sendUserToNextStep(context, USER_PASS_REVIEW_ADD_THEME);
            } else {
                throw new ProcessInputException("Введена неверная команда...");
            }
        }
    }

    @Override
    public String getDynamicText(BotContext context) {
        Integer vkId = context.getVkId();

        // Из шага USER_PASS_REVIEW_ADD_THEME извлекается ID выбранной темы
        Long selectedThemeId = Long.parseLong(storageService.getUserStorage(vkId, USER_PASS_REVIEW_ADD_THEME).get(0));
        Theme selectedTheme = themeService.getThemeById(selectedThemeId);
        Integer selectedThemePosition = selectedTheme.getPosition();

        // Выполняется проверка, что пользователь сдал все предыдущие темы
        if (selectedThemePosition > 1) { // Для первой темы такая проверка не требуется
            List<Theme> allThemes = themeService.getAllThemes();
            List<Theme> passedThemes = themeService.getPassedThemesByUser(vkId);
            boolean isPassedPreviousThemes = true;
            StringBuilder infoMessage = new StringBuilder();
            infoMessage.append(
                    String.format(
                            "Тема \"%s\" не доступна для защиты. Чтобы получить доступ к защите этой темы сдайте следующие темы:\n\n",
                            selectedTheme.getTitle()
                    )
            );
            for (int i = 0; i < selectedTheme.getPosition() - 1; i++) {
                Theme theme = allThemes.get(i);
                if (!passedThemes.contains(theme)) {
                    isPassedPreviousThemes = false;
                    infoMessage.append(
                            String.format(
                                    "[%s] %s \n",
                                    theme.getPosition(),
                                    theme.getTitle()
                            )
                    );
                }
            }
            if (!isPassedPreviousThemes) {
                infoMessage.append("\nВведите подходящий номер темы или вернитесь в главное меню, нажав \"Назад\"");
                sendUserToNextStep(context, USER_PASS_REVIEW_ADD_THEME);
                return infoMessage.toString();
            }
        }

        // В случае выбора темы со свободной защитой, пользователь должен связаться с экзаменаторами самостоятельно
        if (themeService.isFreeTheme(selectedThemeId)) {
            StringBuilder infoMessage = new StringBuilder();
            infoMessage.append(
                    String.format(
                            "Тема со свободной защитой: \"%s\". Напишите любому проверяющему и договоритесь о созвоне:\n\n",
                            selectedTheme.getTitle()
                    )
            );
            List<User> examiners = themeService.getExaminersByFreeThemeId(selectedThemeId);
            for (User examiner : examiners) {
                infoMessage.append(examiner.getFirstName() + " " + examiner.getLastName() + "\n");
            }
            infoMessage.append("\nНажмите \"Назад\" для возврата в предыдущее меню");
            return infoMessage.toString();
        }

        //с прошлошо шага получаем ID темы и по нему из запроса получаем тему
        //Set<Review> reviewsSetNoAccess = new HashSet<>();
        if (reviewsIndex.get(vkId).isEmpty()) {
            return "К сожалению, сейчас никто не готов принять " +
                    "ревью, напиши в общее обсуждение сообщение с просьбой добавить кого-то " +
                    "ревью, чтобы не ждать пока оно появится. Кто-то обязательно откликнется! " +
                    "Если проверяющего не нашлось сообщи сразу же об этом Станиславу Сорокину или Герману Севостьянову";
        }
        //если пользователь пришел с прошлого шага, ему выводится список ревью по выбранной теме
        //если пользователь повторно заходит в данный шаг, значит выбранное ревью уже занято
        StringBuilder reviewList = new StringBuilder("Выбери удобное время и дату для записи, всё время и дата указывается в МСК часовом поясе, для выбора отправь в " +
                "ответе число соответствующее удобному для тебя времени.\n\n");
        //сохраняю в коллекцию id ревью и присваиваю им порядковые номера, при этом формирую список ревью для вывода
        for (Map.Entry<Integer, Long> indexesMap : reviewsIndex.get(vkId).entrySet()) {
            reviewList.append("[")
                    .append(indexesMap.getKey())
                    .append("]")
                    .append(" дата: ")
                    .append(reviewService.getReviewById(indexesMap.getValue()).getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
            reviewList.append("\n");
        }
        return reviewList.toString();
    }

    @Override
    public String getDynamicKeyboard(BotContext context) {
        return "";
    }
}
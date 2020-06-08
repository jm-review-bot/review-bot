package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.NoNumbersEnteredException;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.Review;
import spring.app.model.StudentReview;
import spring.app.model.Theme;
import spring.app.service.abstraction.StorageService;
import spring.app.util.StringParser;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.BACK_KB;


@Component
public class UserPassReviewGetListReview extends Step {
    private Map<Integer, Map<Integer, Long>> reviewsIndex = new HashMap<>();

    @Override
    public void enter(BotContext context) {
        StorageService storageService = context.getStorageService();
        Integer vkId = context.getVkId();
        //с прошлошо шага получаем ID темы и по нему из запроса получаем тему
        Theme theme = context.getThemeService().getThemeById(Long.parseLong(storageService.getUserStorage(vkId, USER_PASS_REVIEW_ADD_THEME).get(0)));
        // получаю список созданных мною ревью, если они имеется
        List<Review> reviewsMy = context.getReviewService().getMyReview(vkId, LocalDateTime.now());
        Set<Review> reviewsSetNoAccess = new HashSet<>();
        //получаю список ревью по теме
        List<Review> reviewsAll = context.getReviewService().getAllReviewsByTheme(context.getUser().getId(), theme, LocalDateTime.now());

        // закомментировал проблемный функционал при попытке записаться на ревью будучи ревьюером на другом

        /*
        if (reviewsMy.size() > 0) {
            // использую Set, т.к. БД создается с наполнением и чтобы не добавлять в БД те ревью, которые в ней уже есть
            Set<Review> reviewsSetTemp = new HashSet<>();
            reviewsSetNoAccess.addAll(reviewsAll);
            // перебор списка моих ревью
            for (Review reviewOneMy : reviewsMy) {
                // получаю список ревью которые не пересекаются с моим ревью и перебираю их поштучно
                for (Review review : context.getReviewService().getAllReviewsByThemeAndNotMyReviews(context.getUser().getId(), theme, LocalDateTime.now(), reviewOneMy.getDate(), 59)) {
                    // если такое ревью встречалось на прошлом проходе, добавляю его в множество
                    if (reviewsSetNoAccess.contains(review)) {
                        reviewsSetTemp.add(review);
                    }
                }
                reviewsSetNoAccess.clear();
                reviewsSetNoAccess.addAll(reviewsSetTemp);
                reviewsSetTemp.clear();
            }
        }
        */

        //список ревью сортирую по дате
        reviewsAll.sort(Comparator.comparing(Review::getDate));

        //если пользователь пришел с прошлого шага, ему выводится список ревью по выбранной теме
        //если пользователь повторно заходит в данный шаг, значит выбранное ревью уже занято
        StringBuilder reviewList = new StringBuilder();
        if (reviewsIndex.get(vkId) == null) {
            reviewList.append("Выбери удобное время и дату для записи, всё время и дата указывается в МСК часовом поясе, для выбора отправь в " +
                    "ответе число соответствующее удобному для тебя времени.\n\n");
        } else {
            reviewList.append("Выбранное Вами ревью уже заполнено!\n\nВыбери удобное время и дату для записи, всё время и дата указывается в МСК " +
                    "часовом поясе, для выбора отправь в ответе число соответствующее удобному для тебя времени.\n\n");
            reviewsIndex.keySet().remove(vkId);
        }

        //сохраняю в коллекцию id ревью и присваиваю им порядковые номера, при этом формирую список ревью для вывода
        Integer i = 1;
        Map<Integer, Long> indexList = new HashMap<>();
        for (Review review : reviewsAll) {
            if (reviewsSetNoAccess.contains(review)) {
                indexList.put(i, -review.getId());
            } else {
                indexList.put(i, review.getId());
            }

            reviewsIndex.putIfAbsent(vkId, indexList);
            reviewList.append("[")
                    .append(i)
                    .append("]")
                    .append(" дата: ")
                    .append(review.getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
            if (reviewsSetNoAccess.contains(review)) {
                reviewList.append(" (запись невозможна: вы проводите ревью в это время)");
            }
            reviewList.append("\n");
            i++;
        }
        text = reviewList.toString();
        keyboard = BACK_KB;
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException, NoNumbersEnteredException {
        StorageService storageService = context.getStorageService();
        Integer vkId = context.getVkId();
        String currentInput = context.getInput();
        if (StringParser.isNumeric(currentInput)) {
            Integer command = StringParser.toNumbersSet(currentInput).iterator().next();
            //получаю список ревью, которые вывелись пользователю
            Map<Integer, Long> allReviewsAndIndex = reviewsIndex.get(vkId);
            //из списка ревью по порядковому номеру получаю id ревью, если null - значит введен номер ревью не доступный в списке
            Long idReview = allReviewsAndIndex.get(command) != null ? reviewsIndex.get(vkId).get(command) : 0L;
            if (idReview > 0L) {
                Review review = context.getReviewService().getReviewById(idReview);
                //проверяю остались ли свободные места в выбранном ревью
                if (context.getStudentReviewService().getNumberStudentReviewByIdReview(review.getId()) < 3) {
                    context.getStudentReviewService().addStudentReview(new StudentReview(context.getUser(), review));
                    //сохраняю дату ревью для следующего шага и очищаю данные в Storage для этого шага
                    List<String> list = new ArrayList<>();
                    list.add(StringParser.localDateTimeToString(review.getDate()));
                    storageService.updateUserStorage(vkId, USER_PASS_REVIEW_GET_LIST_REVIEW, list);
                    storageService.removeUserStorage(vkId, USER_PASS_REVIEW_ADD_THEME);
                    nextStep = USER_PASS_REVIEW_ADD_STUDENT_REVIEW;
                    //удаляю запись по id из списка, т.к. при удалении записи на ревью и повторной записи, выводится
                    //сообщение: Выбранное Вами ревью уже заполнено!
                    reviewsIndex.keySet().remove(vkId);
                } else {
                    // если количество ревью по выбранной теме меньше однго, возвращаюсь на прошлый шаг
                    if (allReviewsAndIndex.size() == 1) {
                        nextStep = USER_PASS_REVIEW_ADD_THEME;
                        reviewsIndex.keySet().remove(vkId);
                    } else {
                        // если выбранном ревью не осталось свободных мест, занчит повторяем данный шаг
                        nextStep = USER_PASS_REVIEW_GET_LIST_REVIEW;
                    }
                }
                // если idReview равно отрицательному числу, значит запись на данное ревью не доступна,
                // т.к. в это время я сам принимаю ревью
            } else if (idReview < 0L) {
                // получаю ревью на которое нет возможности записаться
                Review reviewNoAccess = context.getReviewService().getReviewById(Math.abs(idReview));
                // получаю список моих ревью, которые пересекаются с выбранным ревью
                List<Review> reviewsMy = context.getReviewService().getMyReviewForDate(vkId, reviewNoAccess.getDate(), 59);

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
                nextStep = START;
                reviewsIndex.keySet().remove(vkId);
                storageService.removeUserStorage(vkId, USER_PASS_REVIEW_ADD_THEME);
            } else if ("назад".equals(command)) {
                nextStep = USER_PASS_REVIEW_ADD_THEME;
                reviewsIndex.keySet().remove(vkId);
                storageService.removeUserStorage(vkId, USER_PASS_REVIEW_ADD_THEME);
            } else {
                throw new ProcessInputException("Введена неверная команда...");
            }
        }
    }
}
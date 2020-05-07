package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.NoNumbersEnteredException;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.Review;
import spring.app.model.StudentReview;
import spring.app.model.Theme;
import spring.app.model.User;
import spring.app.service.abstraction.StorageService;
import spring.app.util.StringParser;

import java.time.LocalDateTime;
import java.util.*;

import static java.util.stream.Collectors.toList;
import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.BACK_KB;
import static spring.app.util.Keyboards.USER_MENU_DELETE_STUDENT_REVIEW;

@Component
public class UserPassReviewAddTheme extends Step {
    private Set<Integer> idsIfCancelStudentReview = new HashSet<>();
    private Map<Integer, Theme> themes = new HashMap<>();

    @Override
    public void enter(BotContext context) {
        StorageService storageService = context.getStorageService();
        Integer vkId = context.getVkId();
        StudentReview studentReview = context.getStudentReviewService().getStudentReviewIfAvailableAndOpen(context.getUser().getId());

        if (studentReview != null) {
            text = String.format("Вы уже записаны на ревью:\n" +
                            "Тема: %s\n" +
                            "Дата: %s\n" +
                            "Вы можете отменить запись на это ревью, нажав на кнопку “отмена записи”", studentReview.getReview().getTheme().getTitle(),
                    StringParser.localDateTimeToString(studentReview.getReview().getDate()));

            keyboard = USER_MENU_DELETE_STUDENT_REVIEW;

        } else {
            StringBuilder themeList = new StringBuilder();
            if (storageService.getUserStorage(vkId, USER_PASS_REVIEW_ADD_THEME) != null){
                themeList.append("Выбранное Вами ревью уже заполнено!\n\n");
                storageService.removeUserStorage(vkId, USER_PASS_REVIEW_ADD_THEME);
            }
            //формирую список тем и вывожу его как нумерованный список
            context.getThemeService().getAllThemes().forEach(theme -> themes.putIfAbsent(theme.getPosition(), theme));
            themeList.append("Выберите тему, которые вы хотите сдать, в качестве ответа пришлите цифру (номер темы):\n\n");

            for (Integer position : themes.keySet()) {
                themeList.append("[")
                        .append(position)
                        .append("] ")
                        .append(themes.get(position).getTitle())
                        .append(", стоимость ")
                        .append(themes.get(position).getReviewPoint())
                        .append(" RP")
                        .append("\n");
            }

            text = themeList.toString();

            if (idsIfCancelStudentReview.contains(vkId)) {
                text = "Ревью отменено.\n\n" + text;
                idsIfCancelStudentReview.remove(vkId);
            }

            keyboard = BACK_KB;
        }
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException, NoNumbersEnteredException {
        Integer vkId = context.getVkId();
        String currentInput = context.getInput();
        StorageService storageService = context.getStorageService();
        StudentReview studentReview = context.getStudentReviewService().getStudentReviewIfAvailableAndOpen(context.getUser().getId());
        //если записи на ревью нету, значит ожидаем номер темы
        if (studentReview == null && StringParser.isNumeric(currentInput)) {
            Integer command = StringParser.toNumbersSet(currentInput).iterator().next();
            //проверяем или номер темы не выходит за рамки
            if (command > 0 & command <= themes.size()) {
                Theme theme = context.getThemeService().getByPosition(command);
                User user = context.getUser();
                //Проверяем сдал ли пользователь предыдущее ревью
                List<String> passedThemesIds = context.getThemeService().getPassedThemesByUser(vkId).stream()
                        .map(themee -> themee.getId().toString())
                        .collect(toList());
                String idd = String.valueOf(themes.get(command-1).getId());
                if (passedThemesIds.contains(idd)) {
                    nextStep = USER_TAKE_REVIEW_ADD_DATE;
                } else {
                    nextStep = USER_TAKE_REVIEW_ADD_THEME;
                    throw new ProcessInputException("Ты пока не можешь сдать эту тему, потому что не сдал ревью по предыдущей теме.\n\n" +
                            "Выбери другую тему ревью или нажми на кнопку \"Назад\" для возврата в главное меню.");
                }

                //проверяем хватает ли РП для сдачи выбранной темы
                if (theme.getReviewPoint() <= user.getReviewPoint()) {
                    //получаю список ревью по теме
                    List<Review> reviewsAll = context.getReviewService().getAllReviewsByTheme(context.getUser().getId(), theme, LocalDateTime.now());
                    //проверяем наличие открытых ревью по данной теме
                    if (reviewsAll.isEmpty()) {
                        nextStep = USER_MENU;
                        throw new ProcessInputException("К сожалению, сейчас никто не готов принять " +
                                "ревью, напиши в общее обсуждение сообщение с просьбой добавить кого-то " +
                                "ревью, чтобы не ждать пока оно появится. Кто-то обязательно откликнется! " +
                                "Если проверяющего не нашлось сообщи сразу же об этом Станиславу Сорокину или Герману Севостьянову");
                    } else {
                        //если нашли хоть одно открытое ревью по выбранной теме, сохраняем ID темы для следующего шага
                        List<String> list = new ArrayList<>();
                        list.add(theme.getId().toString());
                        storageService.updateUserStorage(vkId, USER_PASS_REVIEW_ADD_THEME, list);
                        nextStep = USER_PASS_REVIEW_GET_LIST_REVIEW;
                    }
                } else {
                    throw new ProcessInputException("У Вас не хватает РП. Для того чтобы заработать нужное " +
                            "количество РП, необходимо провести ревью.");
                }
            } else {
                throw new ProcessInputException("Введен неверный номер темы...");
            }
        } else {
            //определяем нажатую кнопку или сообщаем о неверной команде
            String command = StringParser.toWordsArray(currentInput)[0];
            if ("отмена".equals(command) && studentReview != null) {
                context.getStudentReviewService().deleteStudentReviewById(studentReview.getId());
                idsIfCancelStudentReview.add(vkId);
                nextStep = USER_PASS_REVIEW_ADD_THEME;
            } else if ("/start".equals(command)) {
                nextStep = START;
            } else if ("назад".equals(command)) {
                nextStep = USER_MENU;
            } else {
                throw new ProcessInputException("Введена неверная команда...");
            }
        }
    }
}
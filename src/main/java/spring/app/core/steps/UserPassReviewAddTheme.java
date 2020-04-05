package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.NoNumbersEnteredException;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.Review;
import spring.app.model.StudentReview;
import spring.app.model.Theme;
import spring.app.model.User;
import spring.app.util.StringParser;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.USER_MENU_DELETE_STUDENT_REVIEW;
import static spring.app.util.Keyboards.BACK_KB;

@Component
public class UserPassReviewAddTheme extends Step {

    @Override
    public void enter(BotContext context) {
        StudentReview studentReview = context.getStudentReviewService().getStudentReviewIfAvailableAndOpen(context.getUser().getId());

        if (studentReview != null) {
            text = String.format("Вы уже записаны на ревью:\n" +
                            "Тема: %s\n" +
                            "Дата: %s\n" +
                            "Вы можете отменить запись на это ревью, нажав на кнопку “отмена записи”", studentReview.getReview().getTheme().getTitle(),
                    studentReview.getReview().getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

            keyboard = USER_MENU_DELETE_STUDENT_REVIEW;
        } else {
            text = "\"Выберите тему, которые вы хотите сдать, в качестве ответа пришлите цифру (номер темы) \n\n" +
                    "[1] Java Core, стоимость 0 RP\n" +
                    "[2] Многопоточность, стоимость 4 RP\n" +
                    "[3] SQL, стоимость 4 RP\n" +
                    "[4] Hibernate, стоимость 4 RP\n" +
                    "[5] Spring, стоимость 4 RP\n" +
                    "[6] Паттерны, стоимость 4 RP\n" +
                    "[7] Алгоритмы, стоимость 4 RP\n" +
                    "[8] Финальное ревью, стоимость 4 RP\"";

            keyboard = BACK_KB;
        }
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException, NoNumbersEnteredException {
        StudentReview studentReview = context.getStudentReviewService().getStudentReviewIfAvailableAndOpen(context.getUser().getId());
        //если записи на ревью нету, значит ожидаем номер темы
//        if (studentReview == null) {
        if (StringParser.isNumeric(context.getInput())) {
            Integer command = StringParser.toNumbersSet(context.getInput()).iterator().next();
            //проверяем или номер темы не выходит за рамки
            if (command > 0 & command < 9) {
                Theme theme = context.getThemeService().getByPosition(command);
                User user = context.getUser();
                //проверяем хватает ли РП для сдачи выбранной темы
                if (theme.getReviewPoint() <= user.getReviewPoint()) {
                    List<Review> reviews = context.getReviewService().getAllReviewsByTheme(theme);
                    //проверяем наличие открытых ревью по данной теме
                    if (reviews.isEmpty()) {
                        // TODO решить на который шаг отбросить
                        nextStep = USER_MENU;
                        throw new ProcessInputException("К сожалению, сейчас никто не готов принять " +
                                "ревью, напиши в общее обсуждение сообщение с просьбой добавить кого-то " +
                                "ревью, чтобы не ждать пока оно появится. Кто-то обязательно откликнется! " +
                                "Если проверяющего не нашлось сообщи сразу же об этом Станиславу Сорокину или Герману Севостьянову");
                    } else {
                        //если нашли хоть одно открытое ревью по выбранной теме, сохраняем ID темы для следующего шага
                        List<String> list = new ArrayList<>();
                        list.add(theme.getId().toString());
                        updateUserStorage(context.getVkId(), USER_PASS_REVIEW_GET_LIST_REVIEW, list);
                        nextStep = USER_PASS_REVIEW_GET_LIST_REVIEW;
                    }
                } else {
                    throw new ProcessInputException("У Вас не хватает РП. Для того чтобы заработать нужное " +
                            "количество РП, необходимо провести ревью.");
                }
            } else {
                throw new ProcessInputException("Введена неверная команда...");
            }
        } else {
            //определяем нажатую кнопку или сообщаем о неверной команде
            String command = StringParser.toWordsArray(context.getInput())[0];
            if ("отмена".equals(command)) {
                context.getStudentReviewService().deleteStudentReviewById(studentReview.getId());
                nextStep = USER_PASS_REVIEW_ADD_THEME;
            } else if ("start".equals(command)) {
                nextStep = START;
            } else if ("назад".equals(command)) {
                nextStep = USER_MENU;;
            } else {
                throw new ProcessInputException("Введена неверная команда...");
            }
        }
    }
}

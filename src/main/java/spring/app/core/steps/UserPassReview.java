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

import java.util.Comparator;
import java.util.List;

import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.*;

@Component
public class UserPassReview extends Step {

    private StudentReview studentReview;
    private List<Review> reviews;
    private Review reviewForDate;
    private int step = 1;

    @Override
    public void enter(BotContext context) {
        switch (step) {
            case (1):
                enterStepOne(context);
                break;
            case (2):
                enterStepSecond(context);
                step++;
                break;
            case (3):
                enterStepThird(context);
                break;
        }
    }

    private void enterStepOne(BotContext context) {
        studentReview = context.getStudentReviewService().getStudentReviewIfAvailableAndOpen(context.getUser().getId());
        if (studentReview != null) {
            text = String.format("Вы уже записаны на ревью:\n" +
                    "Тема: %s\n" +
                    "Дата: %s\n" +
                    "Вы можете отменить запись на это ревью, нажав на кнопку “отмена записи”", studentReview.getReview().getTheme().getTitle(), studentReview.getReview().getDate().toString());

            keyboard = USER_MENU_DELETE_STUDENT_REVIEW;
        } else {
            text = "\"Выберите тему, которые вы хотите сдать, в качестве ответа пришлите цифру (номер темы) \n" +
                    "[1] Java Core, стоимость 0 RP\n" +
                    "[2] Многопоточность, стоимость 4 RP\n" +
                    "[3] SQL, стоимость 4 RP\n" +
                    "[4] Hibernate, стоимость 4 RP\n" +
                    "[5] Spring, стоимость 4 RP\n" +
                    "[6] Паттерны, стоимость 4 RP\n" +
                    "[7] Алгоритмы, стоимость 4 RP\n" +
                    "[8] Финальное ревью, стоимость 4 RP\"";
            step++;
            keyboard = NO_KB;
        }
    }

    private void enterStepSecond(BotContext context) {
        StringBuilder reviewList = new StringBuilder("Список доступных ревью: \n\n");
        reviews.stream()
                .sorted(Comparator.comparing(Review::getDate))
                .forEach(review -> reviewList
                        .append("[")
                        .append(review.getId())
                        .append("]")
                        .append(" дата: ")
                        .append(review.getDate())
                        .append(", принимающий: ")
                        .append(review.getUser().getFirstName())
                        .append(" ")
                        .append(review.getUser().getLastName())
                        .append("\n")
                );
        text = reviewList.toString();
    }

    private void enterStepThird(BotContext context) {
        text = String.format("Ты записан на ревью в %s, для отмены записи на ревью напиши " +
                "\"отмена записи\" в чат или нажми кнопку \"Отмена записи\". В момент, когда ревью начнётся - " +
                "тебе придёт сюда ссылка для подключения к разговору.", reviewForDate.getDate());
        keyboard = USER_MENU_DELETE_STUDENT_REVIEW;
        step = 1;
        studentReview = context.getStudentReviewService().getStudentReviewIfAvailableAndOpen(context.getUser().getId());
        reviews = null;
        reviewForDate = null;
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException, NoNumbersEnteredException {
        if (reviews == null) {
            processInputStepFirst(context);
        } else {
            processInputStepSecond(context);
        }
    }

    private void processInputStepFirst(BotContext context) throws ProcessInputException, NoNumbersEnteredException {
        if (studentReview == null) {
            Integer command = StringParser.toNumbersSet(context.getInput()).iterator().next();
            if (command > 0 & command < 9) {
                Theme theme = context.getThemeService().getByPosition(command);
                User user = context.getUser();
                if (theme.getReviewPoint() <= user.getReviewPoint()) {
                    reviews = context.getReviewService().getAllReviewsByTheme(theme);
                    if (reviews.isEmpty()) {
                        // TODO решить на который шаг отбросить
                        nextStep = START;
                        throw new ProcessInputException("К сожалению, сейчас никто не готов принять " +
                                "ревью, напиши в общее обсуждение сообщение с просьбой добавить кого-то " +
                                "ревью, чтобы не ждать пока оно появится. Кто-то обязательно откликнется! " +
                                "Если проверяющего не нашлось сообщи сразу же об этом Станиславу Сорокину или Герману Севостьянову");
                    }
                } else {
                    throw new ProcessInputException("Введена неверная команда...");
                }
            } else {
                throw new ProcessInputException("У Вас не хватает РП. Для того чтобы заработать нужное " +
                        "количество РП, необходимо провести ревью.");
            }
        } else {
            String command = StringParser.toWordsArray(context.getInput())[0];
            if ("отмена".equals(command)) {
                context.getStudentReviewService().deleteStudentReviewById(studentReview.getId());
                nextStep = USER_PASS_REVIEW;
            } else if ("начать".equals(command)) {
                nextStep = START;
            } else {
                throw new ProcessInputException("Введена неверная команда...");
            }
        }
    }

    private void processInputStepSecond(BotContext context) throws ProcessInputException, NoNumbersEnteredException {
        if (StringParser.isNumeric(context.getInput())) {
            Long command = StringParser.toNumbersSet(context.getInput()).iterator().next().longValue();
            for (Review review : reviews) {
                if (review.getId() == command) {
                    StudentReview studentReview = new StudentReview();
                    reviewForDate = context.getReviewService().getReviewById(command);
                    studentReview.setReview(reviewForDate);
                    studentReview.setUser(context.getUser());
                    context.getStudentReviewService().addStudentReview(studentReview);
                    break;
                } else {
                    throw new ProcessInputException("Введен неверный номер ревью...");
                }
            }
        } else {
            throw new ProcessInputException("Введена неверная команда...");
        }
    }
}

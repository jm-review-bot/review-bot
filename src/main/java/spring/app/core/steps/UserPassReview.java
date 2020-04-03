package spring.app.core.steps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.*;

@Component
public class UserPassReview extends Step {
    private final static Logger log = LoggerFactory.getLogger(UserPassReview.class);

    private final Map<Integer, StudentReview> savedStudentReviews = new HashMap<>();
    private final Map<Integer, List<Review>> savedAvailableReviews = new HashMap<>();
    private final Map<Integer, Review> savedReviewsForDate = new HashMap<>();
    private final Map<Integer, Integer> userSteps = new HashMap<>();

//    private StudentReview studentReview;
//    private List<Review> reviews;
//    private Review reviewForDate;
//    private int step = 1;

    @Override
    public void enter(BotContext context) {
        Integer vkId = context.getVkId();
        // если записи нет, т.е. юзер тут впервые, помещаем 1 шаг
        userSteps.putIfAbsent(vkId, 1);

        Integer step = userSteps.get(vkId);
        switch (step) {
            case (1):
                enterStepOne(context);
                break;
            case (2):
                enterStepTwo(context);
                userSteps.put(vkId,3);
                break;
            case (3):
                enterStepThree(context);
                 // ?
                break;
        }
    }

    private void enterStepOne(BotContext context) {
        Integer vkId = context.getVkId();
        Integer step = userSteps.get(vkId);
        log.debug("Юзер: {}, метод: enterStepOne, step: {}", context.getVkId(), step);

        savedStudentReviews.putIfAbsent(vkId,
                context.getStudentReviewService().getStudentReviewIfAvailableAndOpen(context.getUser().getId())
        );
        StudentReview studentReview = savedStudentReviews.get(vkId);

        if (studentReview != null) {
            text = String.format("Вы уже записаны на ревью:\n" +
                    "Тема: %s\n" +
                    "Дата: %s\n" +
                    "Вы можете отменить запись на это ревью, нажав на кнопку “отмена записи”",
                    studentReview.getReview().getTheme().getTitle(),
                    studentReview.getReview().getDate().toString());

            keyboard = USER_MENU_DELETE_STUDENT_REVIEW;
        } else {
            text = "Выберите тему, которые вы хотите сдать, в качестве ответа пришлите цифру (номер темы) \n" +
                    "[1] Java Core, стоимость 0 RP\n" +
                    "[2] Многопоточность, стоимость 4 RP\n" +
                    "[3] SQL, стоимость 4 RP\n" +
                    "[4] Hibernate, стоимость 4 RP\n" +
                    "[5] Spring, стоимость 4 RP\n" +
                    "[6] Паттерны, стоимость 4 RP\n" +
                    "[7] Алгоритмы, стоимость 4 RP\n" +
                    "[8] Финальное ревью, стоимость 4 RP";
            userSteps.put(vkId,2);
            keyboard = NO_KB;
        }
    }

    private void enterStepTwo(BotContext context) {
        Integer vkId = context.getVkId();
        Integer step = userSteps.get(vkId);
        log.debug("Юзер: {}, метод: enterStepTwo, step: {}", context.getVkId(), step);

        StringBuilder reviewList = new StringBuilder("Список доступных ревью: \n\n");
        savedAvailableReviews.get(vkId).stream()
                .sorted(Comparator.comparing(Review::getDate))
                .forEach(review -> reviewList
                        .append("[")
                        .append(review.getId())
                        .append("]")
                        .append(" дата: ")
                        .append(review.getDate())
                        .append(", принимающий: ")
                        .append(review.getReviewer().getFirstName())
                        .append(" ")
                        .append(review.getReviewer().getLastName())
                        .append("\n")
                );
        text = reviewList.toString();
    }

    private void enterStepThree(BotContext context) {
        Integer vkId = context.getVkId();
        Integer step = userSteps.get(vkId);
        log.debug("Юзер: {}, метод: enterStepThree, step: {}", context.getVkId(), step);

        text = String.format("Ты записан на ревью в %s, для отмены записи на ревью напиши " +
                "\"отмена записи\" в чат или нажми кнопку \"Отмена записи\". В момент, когда ревью начнётся - " +
                "тебе придёт сюда ссылка для подключения к разговору.", savedReviewsForDate.get(vkId).getDate());
        keyboard = USER_MENU_DELETE_STUDENT_REVIEW;
        userSteps.put(vkId, 1);

        savedStudentReviews.put(vkId,
                context.getStudentReviewService().getStudentReviewIfAvailableAndOpen(context.getUser().getId())
        );
//        savedAvailableReviews.remove(vkId);
        savedReviewsForDate.remove(vkId);
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException, NoNumbersEnteredException {
        if (savedAvailableReviews.get(context.getVkId()) == null) {
            processInputStepFirst(context);
        } else {
            processInputStepSecond(context);
        }
    }

    private void processInputStepFirst(BotContext context) throws ProcessInputException, NoNumbersEnteredException {
        Integer vkId = context.getVkId();
        if (savedStudentReviews.get(vkId) == null) {
            Integer command = StringParser.toNumbersSet(context.getInput()).iterator().next();
            if (command > 0 & command < 9) {
                Theme theme = context.getThemeService().getByPosition(command);
                User user = context.getUser();
                if (theme.getReviewPoint() <= user.getReviewPoint()) {
                    List<Review> availableReviews = context.getReviewService().getAllReviewsByTheme(theme);
                    savedAvailableReviews.put(vkId, availableReviews);

                    if (availableReviews.isEmpty()) {
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
                context.getStudentReviewService()
                            .deleteStudentReviewById(savedStudentReviews.get(vkId).getId()
                        );
                nextStep = USER_PASS_REVIEW;
            } else if ("/start".equals(command)) {
                nextStep = START;
            } else {
                throw new ProcessInputException("Введена неверная команда...");
            }
        }
    }

    private void processInputStepSecond(BotContext context) throws ProcessInputException, NoNumbersEnteredException {
        Integer vkId = context.getVkId();

        if (StringParser.isNumeric(context.getInput())) {
            Long command = StringParser.toNumbersSet(context.getInput()).iterator().next().longValue();
            for (Review review : savedAvailableReviews.get(vkId)) {
                if (review.getId() == command) {
                    StudentReview studentReview = new StudentReview();
                    Review reviewForDate = context.getReviewService().getReviewById(command);
                    savedReviewsForDate.put(vkId, reviewForDate);

                    studentReview.setReview(reviewForDate);
                    studentReview.setStudent(context.getUser());
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

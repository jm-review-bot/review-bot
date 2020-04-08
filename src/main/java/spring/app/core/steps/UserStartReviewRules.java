package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.NoDataEnteredException;
import spring.app.exceptions.NoNumbersEnteredException;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.Review;
import spring.app.model.User;
import spring.app.util.StringParser;

import java.time.LocalDateTime;
import java.util.List;

import static spring.app.core.StepSelector.USER_MENU;
import static spring.app.core.StepSelector.USER_START_REVIEW_CORE;
import static spring.app.util.Keyboards.START_KB;

@Component
public class UserStartReviewRules extends Step {

    @Override
    public void enter(BotContext context) {

        // Получаю reviewId из хранилища и список студентов, записанных на ревью
        Long reviewId = Long.parseLong(getUserStorage(context.getVkId(), USER_MENU).get(0));
        List<User> students = context.getUserService().getStudentsByReviewId(reviewId);
        // формирую список участников
        StringBuilder textBuilder = new StringBuilder("На твоём ревью сегодня присутствуют:\n\n");
        final int[] i = {1};
        students.forEach(user -> {
            textBuilder.append("[").append(i[0]++).append("] ")
                    .append(user.getLastName())
                    .append(" ")
                    .append(user.getFirstName())
                    .append(", https://vk.com/id")
                    .append(user.getVkId())
                    .append("\n");
        });

        //TODO Подгонка инструкции под количество участников ревью

        textBuilder.append("\nСистема будет выдавать вопрос из списка, который тебе необходимо задать," +
                " ты сам выбираешь кому задать данный вопрос," +
                " если человек не отвечает на вопрос - отправь его номер в чат" +
                " и задай вопрос другому человеку," +
                " если он тоже не ответит - так же напиши его номер, если кто-то ответил правильно на вопрос напиши его номер и добавь + после номера. \n" +
                "\n" +
                "Если Андрей и Сергей не ответили на вопрос, а Иван ответил, то надо написать в чат 1 2 3+\n" +
                "Если Андрей ответил на вопрос то остальным мы его не задаём и пишем в чат 1+\n" +
                "Если Андрей не ответил на вопрос, а Сергей ответил то Ивану вопрос не задаём и пишем в чат 1 2+\n" +
                "Если никто не ответил правильно на вопрос - написать в чат 1 2 3 \n" +
                "\n" +
                "Вопрос задаётся до ПЕРВОГО человека ответившего правильно");
        text = textBuilder.toString();
        keyboard = START_KB;
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException, NoNumbersEnteredException, NoDataEnteredException {
        Integer vkId = context.getVkId();
        String userInput = context.getInput();
        // по нажатию на кнопку "Начать" закрываем ревью и переходим на следующий шаг
        if (userInput.equalsIgnoreCase("Начать")) {
            Long reviewId = Long.parseLong(getUserStorage(vkId, USER_MENU).get(0));
            Review review = context.getReviewService().getReviewById(reviewId);
            // не даем начать ревью раньше его официального начала (вдруг кто-то присоединится в последний момент)
            if (LocalDateTime.now().isAfter(review.getDate())) {
                review.setOpen(false);
                context.getReviewService().updateReview(review);
                nextStep = USER_START_REVIEW_CORE;
            } else {
                throw new ProcessInputException("Ты не можешь начать ревью раньше его официального начала.\n Дождись " +
                        StringParser.localDateTimeToString(review.getDate()) + " и нажми на кнопку \"Начать\" снова.");
            }
        } else if (userInput.equalsIgnoreCase("/start")) {
            nextStep = USER_MENU;
        } else {
            throw new ProcessInputException("Неверная команда, нажми на нопку \"Начать\"");
        }
    }
}


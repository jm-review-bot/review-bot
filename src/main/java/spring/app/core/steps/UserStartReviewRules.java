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

import static spring.app.core.StepSelector.*;
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

        // формируем информационное сообщение , которое зависит от кол-ва участников ревью
        textBuilder.append("\nСистема будет выдавать вопрос из списка, который тебе необходимо задать.\n\n");
        if (students.size() == 1) {
            textBuilder.append("Если ")
                    .append(students.get(0).getFirstName())
                    .append(" ответил правильно на вопрос - напиши в чат 1+ \nЕсли ")
                    .append(students.get(0).getFirstName())
                    .append(" неправильно ответил на вопрос - напиши в чат 1 ");
        } else if (students.size() > 1) {
            textBuilder.append("Ты сам выбираешь кому задать данный вопрос, если человек не отвечает на вопрос - отправь его номер в чат " +
                    "и задай вопрос другому человеку, если он тоже не ответит - так же напиши его номер, если кто-то ответил правильно " +
                    "на вопрос напиши его номер и добавь + после номера.\n\n")
                    .append("Если ")
                    .append(students.get(0).getFirstName())
                    .append(" и ")
                    .append(students.get(1).getFirstName())
                    .append(" не ответили на вопрос ");
            if (students.size() == 3) {
                textBuilder.append("а ")
                        .append(students.get(2).getFirstName())
                        .append(" ответил");
            }
            textBuilder.append(", то надо написать в чат 1 2 ");
            if (students.size() == 3) {
                textBuilder.append("3+");
            }
            textBuilder.append("\nЕсли ")
                    .append(students.get(0).getFirstName())
                    .append(" ответил на вопрос то дальше этот вопрос не задаем и пишем в чат 1+\n")
                    .append("Если ")
                    .append(students.get(0).getFirstName())
                    .append(" не ответил на вопрос, а ")
                    .append(students.get(1).getFirstName())
                    .append(" ответил, то ");
            if (students.size() == 3) {
                textBuilder.append(students.get(2).getFirstName()).append(" уже не отвечает на этот вопрос - ");
            }
            textBuilder.append("пишем в чат 1 2+\n");
            if (students.size() == 3) {
                textBuilder.append("Если никто не ответил правильно на вопрос - \nнаписать в чат 1 2 3");
            }
            textBuilder.append("\n\nВопрос задаётся до ПЕРВОГО человека ответившего правильно");
        }
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
            nextStep = START;
        } else {
            throw new ProcessInputException("Неверная команда, нажми на нопку \"Начать\"");
        }
    }
}


package spring.app.core.steps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.NoDataEnteredException;
import spring.app.exceptions.NoNumbersEnteredException;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.Question;
import spring.app.model.User;

import java.util.*;

import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.*;

@Component
public class UserStartReviewStepThree extends Step {
    // Map хранит vkId принимающего ревью и индекс вопроса из List<Question> questions, который сейчас задает принимающий
    private final Map<Integer, Integer> questionIteration = new HashMap<>();

    private final static Logger log = LoggerFactory.getLogger(UserStartReviewStepThree.class);

    @Override
    public void enter(BotContext context) {
        Integer vkId = context.getVkId();
        // Получаю reviewId из хранилища
        Long reviewId = Long.parseLong(getUserStorage(vkId, USER_MENU).get(0));
        // Достаю список студентов, записанных на ревью
        List<User> students = context.getUserService().getStudentsByReviewId(reviewId);
        StringBuilder textBuilder = new StringBuilder("На твоём ревью сегодня присутствуют:\n\n");
        final int[] i = {1};
        students.stream()
                .sorted(Comparator.comparing(User::getLastName))
                .forEach(user -> {
                    textBuilder.append("[").append(i[0]++).append("] ")
                            .append(user.getLastName())
                            .append(" ")
                            .append(user.getFirstName())
                            .append(", https://vk.com/id")
                            .append(user.getVkId())
                            .append("\n");
                });
        // получаем список вопросов, отсортированных по позиции, которые соответствуют теме ревью
        List<Question> questions = context.getQuestionService().getQuestionsByReviewId(reviewId);

        // Если мы первый раз оказались на этом шаге, то в мапе нет ключа с vkId юзера, задаем первый вопрос из списка, сохраняем индекс вопроса в мапу
        if (!questionIteration.containsKey(vkId)) {
            int iteration = 0;
            textBuilder.append("\nВопрос: ")
                    .append(questions.get(iteration).getQuestion())
                    .append("\n\nОтвет: ")
                    .append(questions.get(iteration).getAnswer());
            text = textBuilder.toString();
            questionIteration.put(vkId, iteration);
        } else {
            int iteration = questionIteration.get(vkId);
            if (iteration != questions.size()) {
                textBuilder.append("\nВопрос: ")
                        .append(questions.get(iteration).getQuestion())
                        .append("\n\nОтвет: ")
                        .append(questions.get(iteration).getAnswer());
            } else {
                questionIteration.keySet().remove(vkId); // удаляем ключ с id юзера из локальной мапы итерации вопросов
                // добавляем очки проверяющему
                User user = context.getUserService().getByVkId(vkId);
                user.setReviewPoint(user.getReviewPoint() + 2);
                context.getUserService().updateUser(user);
                // формируем сообщение для отправки в ВК
                String errorMessage = String.format("Вопросы закончились, ревью окончено! Результаты ревью будут отправлены каждому участнику.\nВам начислено 2 RP, Ваш баланс %d RP", user.getReviewPoint());
                textBuilder.delete(0, textBuilder.length())
                        .append(errorMessage);
                // TODO обработка логики
                // TODO после обработки логики очистить хранилище
                //keyboard = ; TODO сделать клавиатуру, с кнопкой выхода в главно меню
            }
        }
        text = textBuilder.toString();
        keyboard = NO_KB;
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException, NoNumbersEnteredException, NoDataEnteredException {
        Integer vkId = context.getVkId();
        String userInput = context.getInput();
        //TODO проверка, что ввод корректный (определить критерии корректного ввода, может сразу в БД писать ответы не сохраняя во временный список)
        // корректный ввод, это только наличие цифр и знаков +-, разделенные пробелом, цифры соответствуют кол-ву участников ревью
        if (!userInput.equals("")) {
            // определяем на какой итерации мы находимся
            int iteration = questionIteration.get(vkId);
            // сохраняем ответы студентов в STORAGE
            if (iteration == 0) {
                List<String> results = new ArrayList<>();
                results.add(iteration, userInput);
                updateUserStorage(vkId, USER_START_REVIEW_STEP_THREE, results);
                log.warn("Сохранен ответ {} на вопрос по индексу {}", userInput, iteration);
            } else {
                List<String> results = getUserStorage(vkId, USER_START_REVIEW_STEP_THREE);
                results.add(iteration, userInput);
                updateUserStorage(vkId, USER_START_REVIEW_STEP_THREE, results);
                log.warn("Сохранен ответ {} на вопрос по индексу {}", userInput, iteration);
            }
            iteration++;
            questionIteration.put(vkId, iteration);
            nextStep = USER_START_REVIEW_STEP_THREE;
        } else {
            throw new ProcessInputException("Неверный ввод");
        }
    }   // TODO добавить обработку кнопочки заверешения ревью и выхода в главное
}

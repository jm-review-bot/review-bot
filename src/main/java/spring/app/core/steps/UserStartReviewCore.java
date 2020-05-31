package spring.app.core.steps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.NoDataEnteredException;
import spring.app.exceptions.NoNumbersEnteredException;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.Question;
import spring.app.model.StudentReview;
import spring.app.model.StudentReviewAnswer;
import spring.app.model.User;
import spring.app.service.abstraction.StorageService;
import spring.app.util.StringParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.NO_KB;
import static spring.app.util.Keyboards.USER_MENU_KB;

@Component
public class UserStartReviewCore extends Step {

    @Value("${review.point_for_take_review}")
    int pointForTakeReview;

    private final static Logger log = LoggerFactory.getLogger(UserStartReviewCore.class);

    // Map хранит vkId ревьюера и индекс вопроса из List<Question> questions, который сейчас задает ревьюер
    private final Map<Integer, Integer> questionNumbers = new HashMap<>();

    @Override
    public void enter(BotContext context) {
        StorageService storageService = context.getStorageService();
        Integer vkId = context.getVkId();
        // Получаю reviewId из хранилища и список студентов, записанных на ревью
        Long reviewId = Long.parseLong(storageService.getUserStorage(vkId, USER_MENU).get(0));
        List<User> students = context.getUserService().getStudentsByReviewId(reviewId);
        // получаем список вопросов, отсортированных по позиции, которые соответствуют теме ревью
        List<Question> questions = context.getQuestionService().getQuestionsByReviewId(reviewId);

        // формирую сообщение со списком участников
        StringBuilder textBuilder = new StringBuilder(storageService.getUserStorage(vkId, USER_START_REVIEW_RULES).get(0));
        // Если мы первый раз оказались на этом шаге, то в Map questionNumbers еще нет ключа, соответствующего vkId ревьюера,
        // поэтому задаем первый вопрос из списка, сохраняем номер вопроса (начинаем с 0) в questionNumbers
        if (!questionNumbers.containsKey(vkId)) {
            int questionNumber = 0;
            textBuilder.append(String.format("\nВопрос: %s", questions.get(questionNumber).getQuestion()))
                    .append(String.format("\n\nОтвет: %s", questions.get(questionNumber).getAnswer()));
            questionNumbers.put(vkId, questionNumber);
            keyboard = NO_KB;
        } else {
            int questionNumber = questionNumbers.get(vkId);
            if (questionNumber != questions.size()) {
                textBuilder.append(String.format("\nВопрос: %s", questions.get(questionNumber).getQuestion()))
                        .append(String.format("\n\nОтвет: %s", questions.get(questionNumber).getAnswer()));
                keyboard = NO_KB;

                // если вопросы кончились
            } else {
                // выгружаем список строк с вводом ревьюера из STORAGE, парсим эти строки, делаем записи в БД в student_review_answer
                // строки корректные, мы их проверяли в processInput()
                List<String> reviewerInput = storageService.getUserStorage(vkId, USER_START_REVIEW_CORE);
                for (int j = 0; j < reviewerInput.size(); j++) {
                    String[] studentAnswers = reviewerInput.get(j).split(" ");
                    for (String answer : studentAnswers) {
                        // если в ячейке массива содержится +, значит этот студент ответил на вопрос правильно
                        if (answer.contains("+")) {
                            int studentNumber = Integer.parseInt(answer.replace("+", ""));
                            // Получаем Юзера, Вопрос и StudentReview, и делаем в БД вставку нового StudentReviewAnswer c isRight = true
                            User student = students.get(studentNumber - 1);
                            Question question = questions.get(j);
                            StudentReview studentReview = context.getStudentReviewService().getStudentReviewByReviewIdAndStudentId(reviewId, student.getId());
                            context.getStudentReviewAnswerService().addStudentReviewAnswer(new StudentReviewAnswer(studentReview, question, true));
                            // Если в ячейке массива нет знака +, значит студент ответил на вопрос неправильно
                            // Если в строке нет номера студента, знасит вопрос ему не задавался, вставку в БД не делаем
                        } else {
                            int studentNumber = Integer.parseInt(answer);
                            User student = students.get(studentNumber - 1);
                            Question question = questions.get(j);
                            StudentReview studentReview = context.getStudentReviewService().getStudentReviewByReviewIdAndStudentId(reviewId, student.getId());
                            context.getStudentReviewAnswerService().addStudentReviewAnswer(new StudentReviewAnswer(studentReview, question, false));
                        }
                    }
                }
                // очищаем ввод ревьюера из STORAGE
                storageService.removeUserStorage(vkId, USER_START_REVIEW_CORE);
                storageService.removeUserStorage(vkId, USER_START_REVIEW_RULES);
                // добавляем очки за прием ревью
                User user = context.getUserService().getByVkId(vkId);
                user.setReviewPoint(user.getReviewPoint() + pointForTakeReview);
                context.getUserService().updateUser(user);
                // формируем сообщение об окончании ревью для ревьюера
                String reviewResultMessage = String.format("Вопросы закончились, ревью окончено!\n Результаты ревью будут отправлены каждому участнику.\nВам начислено 2 RP, теперь Ваш баланс %d RP", user.getReviewPoint());
                textBuilder.delete(0, textBuilder.length()).append(reviewResultMessage);

                // подведение итогов ревью для студентов, рассылка сообщений студентам с результатами ревью, списание очков
                for (User student : students) {
                    Integer weight = new Integer(0);
                    // списываем очки за пройденное ревью
                    Integer reviewPoint = context.getThemeService().getThemeByReviewId(reviewId).getReviewPoint();
                    student.setReviewPoint(student.getReviewPoint() - reviewPoint);
                    context.getUserService().updateUser(student);
                    // проверяем ответы каждого пользователя, если хоть один false или ему не задавались вопросы, то ревью он не сдал
                    StudentReview studentReview = context.getStudentReviewService().getStudentReviewByReviewIdAndStudentId(reviewId, student.getId());
                    List<StudentReviewAnswer> studentReviewAnswers = context.getStudentReviewAnswerService().getStudentReviewAnswersByStudentReviewId(studentReview.getId());
                    if (studentReviewAnswers.isEmpty()) {
                        studentReview.setPassed(false);
                    } else {
                        for (StudentReviewAnswer answer : studentReviewAnswers) {
                            if (!answer.getRight()) {
                                weight += context.getQuestionService().getQuestionByStudentReviewAnswerId(answer.getId()).getWeight();
                            }
                        }
                        if (weight >= context.getThemeService().getThemeByReviewId(reviewId).getCriticalWeight()) {
                            studentReview.setPassed(false);
                        } else {
                            studentReview.setPassed(true);
                        }
                    }
                    context.getStudentReviewService().updateStudentReview(studentReview);
                    // формируем сообщение для студента с результатами ревью
                    StringBuilder reviewResults = new StringBuilder("Ревью окончено!\nТвой результат: ");
                    if (studentReview.getPassed()) {
                        reviewResults.append("Ревью пройдено\n");
                    } else {
                        List<StudentReviewAnswer> incorrectAnswers = studentReviewAnswers.stream().filter(sra -> !(sra.getRight())).collect(Collectors.toList());
                        reviewResults.append("Ревью не пройдено.\n\nВот список проблемных вопросов:\n");
                        final int[] k = {1};
                        for (StudentReviewAnswer answer : incorrectAnswers) {
                            Question incorrectAnswer = context.getQuestionService().getQuestionByStudentReviewAnswerId(answer.getId());
                            reviewResults.append("[").append(k[0]++).append("] ")
                                    .append(incorrectAnswer.getQuestion())
                                    .append("\n");
                        }
                    }
                    reviewResults.append(String.format("\nЗа участие в ревью списано: %d RP, твой баланс теперь составляет: %d RP", reviewPoint, student.getReviewPoint()));
                    // отправляем студенту результаты ревью
                    Step userStep = context.getStepHolder().getSteps().get(user.getChatStep());
                    context.getVkService().sendMessage(reviewResults.toString(), userStep.getKeyboard(), student.getVkId());
                    log.warn("Студенту с id {} отправлено сообщение {}", student.getVkId(), reviewResults.toString());
                }
                keyboard = USER_MENU_KB;
            }
        }
        text = textBuilder.toString();
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException, NoNumbersEnteredException, NoDataEnteredException {
        StorageService storageService = context.getStorageService();
        Integer vkId = context.getVkId();
        String userInput = context.getInput();
        Long reviewId = Long.parseLong(storageService.getUserStorage(vkId, USER_MENU).get(0));
        List<User> students = context.getUserService().getStudentsByReviewId(reviewId);
        List<Question> questions = context.getQuestionService().getQuestionsByReviewId(reviewId);

        if (userInput.equalsIgnoreCase("/start")) { // служебная команда, которая прервет выполнение ревью без возможности возвращения к нему
            nextStep = START;
            questionNumbers.keySet().remove(vkId);
            storageService.removeUserStorage(vkId, USER_MENU);
            // если вопросы закончились, то ждем только нажатия на кнопку выхода в главное меню или ввод /start
        } else if (questionNumbers.get(vkId) == questions.size()) {
            if (userInput.equalsIgnoreCase("главное меню")) {
                nextStep = USER_MENU;
                questionNumbers.keySet().remove(vkId);
                storageService.removeUserStorage(vkId, USER_MENU);
            } else if (userInput.equalsIgnoreCase("/start")) {
                nextStep = START;
                questionNumbers.keySet().remove(vkId);
                storageService.removeUserStorage(vkId, USER_MENU);
            } else {
                throw new ProcessInputException("Для выхода в главное меню нажми кнопку \"Главное меню\"");
            }
            // проверяем, является ли ввод ревьюера корректным
        } else if (StringParser.isValidReviewerInput(userInput, students.size())) {
            // определяем какой вопрос мы задаем
            int questionNumber = questionNumbers.get(vkId);
            // сохраняем результат ответов студентов на вопрос в STORAGE
            if (questionNumber == 0) {
                List<String> results = new ArrayList<>();
                results.add(questionNumber, userInput);
                storageService.updateUserStorage(vkId, USER_START_REVIEW_CORE, results);
                log.warn("Сохранен ответ {} на вопрос по индексу {}", userInput, questionNumber);
            } else {
                List<String> results = storageService.getUserStorage(vkId, USER_START_REVIEW_CORE);
                results.add(questionNumber, userInput);
                storageService.updateUserStorage(vkId, USER_START_REVIEW_CORE, results);
                log.warn("Сохранен ответ {} на вопрос по индексу {}", userInput, questionNumber);
            }
            // увеличиваем номер вопроса на 1, т.о. переходя к следующему
            questionNumber++;
            questionNumbers.put(vkId, questionNumber);
            nextStep = USER_START_REVIEW_CORE;
        } else {
            throw new ProcessInputException("Неверный ввод ответов студентов, повторите ввод");
        }
    }
}

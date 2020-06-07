package spring.app.core.steps;

import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.dto.QuestionNumberAnswererIdDto;
import spring.app.exceptions.NoDataEnteredException;
import spring.app.exceptions.NoNumbersEnteredException;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.*;
import spring.app.service.abstraction.*;
import spring.app.util.StringParser;

import java.util.*;
import java.util.stream.Collectors;

import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.*;

@Component
public class UserStartReviewCore extends Step {

    private final StorageService storageService;
    private final UserService userService;
    private final QuestionService questionService;
    private final StudentReviewService studentReviewService;
    private final StudentReviewAnswerService studentReviewAnswerService;
    private final ThemeService themeService;
    private final VkService vkService;

    @Value("${review.point_for_take_review}")
    private int pointForTakeReview;

    private final static Logger log = LoggerFactory.getLogger(UserStartReviewCore.class);

    // Map хранит по vkId ревьюера текущий вопрос и отвечающего на него
    private final Map<Integer, QuestionNumberAnswererIdDto> questionNumbers = new HashMap<>();
    //Хранит по vkId набор айдишников студентов которым можно задать вопрос. На старте - всем студентам на ревью
    private final Map<Integer, List<Long>> possibleAnswerer = new HashMap<>();

    public UserStartReviewCore(StorageService storageService, UserService userService,
                               QuestionService questionService, StudentReviewService studentReviewService,
                               StudentReviewAnswerService studentReviewAnswerService, ThemeService themeService,
                               VkService vkService) {
        this.storageService = storageService;
        this.userService = userService;
        this.questionService = questionService;
        this.studentReviewService = studentReviewService;
        this.studentReviewAnswerService = studentReviewAnswerService;
        this.themeService = themeService;
        this.vkService = vkService;
    }

    @Override
    public void enter(BotContext context) {
        Integer vkId = context.getVkId();
        // Получаю reviewId из хранилища и список студентов, записанных на ревью//
        Long reviewId = Long.parseLong(storageService.getUserStorage(vkId, USER_MENU).get(0));
        List<User> students = userService.getStudentsByReviewId(reviewId);
        // получаем список вопросов, отсортированных по позиции, которые соответствуют теме ревью
        List<Question> questions = questionService.getQuestionsByReviewId(reviewId);

        // формирую сообщение со списком участников
        StringBuilder textBuilder = new StringBuilder(storageService.getUserStorage(vkId, USER_START_REVIEW_RULES).get(0));
        // Если мы первый раз оказались на этом шаге, то в Map questionNumbers еще нет ключа, соответствующего vkId ревьюера,
        // поэтому задаем первый вопрос из списка, сохраняем номер вопроса (начинаем с 0) в questionNumbers

        questionNumbers.putIfAbsent(vkId, new QuestionNumberAnswererIdDto(0, 0L));
        if (!possibleAnswerer.containsKey(vkId)) { //containsKey ибо иначе каждый раз расчеты были бы
            possibleAnswerer.put(vkId, students.stream().map(User::getId).collect(Collectors.toList()));
        }
        int questionNumber = questionNumbers.get(vkId).getQuestionNumber();
        if (questionNumber != questions.size()) {
            Long nextAnswererId = selectNextRandomAnswererStudent(vkId, context, questionNumber);
            //находим по айдишнику следующего студента в списке студентов.
            User user = students.stream().filter(u -> u.getId().equals(nextAnswererId)).findFirst().get();
            textBuilder
                    .append("\nВопрос для ")
                    .append(user.getFirstName()).append(" ").append(user.getLastName())
                    .append(String.format(": %s", questions.get(questionNumber).getQuestion()))
                    .append(String.format("\n\nОтвет: %s", questions.get(questionNumber).getAnswer()));
            questionNumbers.put(vkId, new QuestionNumberAnswererIdDto(questionNumber, nextAnswererId));
            keyboard = RIGHT_WRONG_ANSWER;

            // если вопросы кончились
        } else {
            // выгружаем список строк с вводом ревьюера из STORAGE, парсим эти строки, делаем записи в БД в student_review_answer
            // строки корректные, мы их проверяли в processInput()
            List<String> reviewerInput = storageService.getUserStorage(vkId, USER_START_REVIEW_CORE);
            //карта с ключом по студенту и списком вопросов на которые он не смог дать ответ
            Map<User, List<String>> problemQuestions = new HashMap<>();
            //карта с ключом по студенту и суммарным весом его кривых вопросов(не стал мудрить и фигачить лист с парами строка-вес)
            Map<User, Integer> studentWrongWeight = new HashMap<>();
            for (String questionString : reviewerInput) { //TODO:вынести в отдельный метод AR
                //вычленяем позицию вопроса, вырезаем её и получаем вопрос.
                String questionPosition = questionString.substring(1, questionString.indexOf(" "));//c 1 потому что строка начинается с q. q23 4 2 1 к примеру. Это удобнее, если когда-то мы будем хранить ревью
                questionString = questionString.substring(questionString.indexOf(" ") + 1);
                Question question = questions.get(Integer.parseInt(questionPosition));

                if (questionString.endsWith("+")) {
                    //вырезаем последнего студента в таком случае. Он нормальный.
                    String luckyStudentPosition;
                    if (questionString.contains(" ")) {
                        luckyStudentPosition = questionString.substring(questionString.lastIndexOf(" ") + 1, questionString.length() - 1);//+ не вытаскиваем
                        questionString = questionString.substring(0, questionString.lastIndexOf(" "));
                    } else {
                        luckyStudentPosition = questionString.substring(0, questionString.length() - 1);
                        questionString = "";
                    }
                    int studentNumber = Integer.parseInt(luckyStudentPosition);
                    User student = students.get(studentNumber);
                    StudentReview studentReview = studentReviewService.getStudentReviewByReviewIdAndStudentId(reviewId, student.getId());
                    studentReviewAnswerService.addStudentReviewAnswer(new StudentReviewAnswer(studentReview, question, true));
                }
                //Обработаем неучей. Мы можем вообще не зайти сюда, если первый же студент ответил верно
                if (!questionString.isEmpty()) {
                    String[] studentAnswers = questionString.split(" ");
                    for (String answer : studentAnswers) {
                        int studentNumber = Integer.parseInt(answer);
                        User student = students.get(studentNumber);
                        StudentReview studentReview = studentReviewService.getStudentReviewByReviewIdAndStudentId(reviewId, student.getId());
                        studentReviewAnswerService.addStudentReviewAnswer(new StudentReviewAnswer(studentReview, question, false));
                        //добавим вопрос в список проблемных для этого студента.
                        problemQuestions.putIfAbsent(student, new ArrayList<>());
                        List<String> problemQuestionList = problemQuestions.get(student);
                        problemQuestionList.add(question.getQuestion());
                        problemQuestions.put(student, problemQuestionList);
                        //да, тут в одну строку, т.к. конкат листов как минимум не удобен для чтения
                        studentWrongWeight.merge(student, question.getWeight(), (oldWeight, newWeight) -> oldWeight += newWeight);
                    }
                }
            }

            // очищаем ввод ревьюера из STORAGE.
            storageService.removeUserStorage(vkId, USER_START_REVIEW_CORE);
            storageService.removeUserStorage(vkId, USER_START_REVIEW_RULES);
            // добавляем очки за прием ревью
            User user = userService.getByVkId(vkId);
            user.setReviewPoint(user.getReviewPoint() + pointForTakeReview);
            userService.updateUser(user);
            // формируем сообщение об окончании ревью для ревьюера
            String reviewResultMessage = String.format("Вопросы закончились, ревью окончено!\n Результаты ревью будут отправлены каждому участнику.\nВам начислено 2 RP, теперь Ваш баланс %d RP", user.getReviewPoint());
            textBuilder.delete(0, textBuilder.length()).append(reviewResultMessage);

            // подведение итогов ревью для студентов, рассылка сообщений студентам с результатами ревью, списание очков
            for (User student : students) {//TODO:разбить и вынести на 2 метода - расчет итогов(сдал/не сдал) и рассылку AR
                // списываем очки за пройденное ревью
                Theme theme = themeService.getThemeByReviewId(reviewId);
                student.setReviewPoint(student.getReviewPoint() - theme.getReviewPoint());
                userService.updateUser(student);
                // проверяем ответы по карте. Если проблемные вопросы есть - значит(до правок по весу) он не сдал.
                // Не быть хотя бы одного ответа у него не может т.к. это возможно только если вопросов на ревью было меньше чем студентов
                StudentReview studentReview = studentReviewService.getStudentReviewByReviewIdAndStudentId(reviewId, student.getId());
                if (studentWrongWeight.get(student) >= theme.getCriticalWeight()) {
                    studentReview.setPassed(false);
                } else {
                    studentReview.setPassed(true);
                }
                studentReviewService.updateStudentReview(studentReview);
                // формируем сообщение для студента с результатами ревью
                StringBuilder reviewResults = new StringBuilder("Ревью окончено!\nТвой результат: ");
                if (studentReview.getPassed()) {
                    reviewResults.append("Ревью пройдено\n");
                } else {
                    reviewResults.append("Ревью не пройдено.\n\nВот список проблемных вопросов:\n");
                }
                final int[] k = {1};
                for (String incorrectAnswer : problemQuestions.get(student)) {
                    reviewResults.append("[").append(k[0]++).append("] ")
                            .append(incorrectAnswer)
                            .append("\n");
                }
                reviewResults.append(String.format("\nЗа участие в ревью списано: %d RP, твой баланс теперь составляет: %d RP", theme.getReviewPoint(), student.getReviewPoint()));
                // отправляем студенту результаты ревью
                Step userStep = context.getStepHolder().getSteps().get(student.getChatStep());
                vkService.sendMessage(reviewResults.toString(), userStep.getKeyboard(), student.getVkId());
                log.warn("Студенту с id {} отправлено сообщение {}", student.getVkId(), reviewResults.toString());
            }
            keyboard = USER_MENU_KB;
        }
        text = textBuilder.toString();
    }

    @Override
    public void processInput(BotContext context) throws
            ProcessInputException, NoNumbersEnteredException, NoDataEnteredException {
        Integer vkId = context.getVkId();
        String userInput = context.getInput();
        Long reviewId = Long.parseLong(storageService.getUserStorage(vkId, USER_MENU).get(0));
        List<User> students = userService.getStudentsByReviewId(reviewId);
        List<Question> questions = questionService.getQuestionsByReviewId(reviewId);

        if (userInput.equalsIgnoreCase("/start")) { // служебная команда, которая прервет выполнение ревью без возможности возвращения к нему
            nextStep = START;
            //очищаем все что накопили.
            questionNumbers.keySet().remove(vkId);
            possibleAnswerer.keySet().remove(vkId);
            storageService.removeUserStorage(vkId, USER_MENU);
            storageService.removeUserStorage(vkId, USER_START_REVIEW_CORE);
            storageService.removeUserStorage(vkId, USER_START_REVIEW_RULES);
            // если вопросы закончились, то ждем только нажатия на кнопку выхода в главное меню или ввод /start
        } else if (questionNumbers.get(vkId).getQuestionNumber() == questions.size()) {
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
        } else if (userInput.equals("Ответ принят") || userInput.equals("Ответ не принят")) {
            // определяем какой вопрос мы задавали
            int questionNumber = questionNumbers.get(vkId).getQuestionNumber();
            //выявляем кому задавали
            Long answererId = questionNumbers.get(vkId).getAnswererId();
            int answererPosition = 0;
            for (User student : students) {
                if (student.getId().equals(answererId)) {
                    answererPosition = students.indexOf(student);
                }
            }
            //Получим текущий набор строк с ответами на вопросы
            List<String> questionStrings = storageService.getUserStorage(vkId, USER_START_REVIEW_CORE);
            if (questionStrings == null) {
                questionStrings = new ArrayList<>();
            }
            //получим строку с текущим вопросом. Если её нет - создадим
            String currentPrefix = new StringBuilder("q").append(questionNumber).append(" ").toString();
            String currentQuestionString = currentPrefix;
            if (!questionStrings.isEmpty()) {
                String lastQuestionString = questionStrings.get(questionStrings.size() - 1);
                if (lastQuestionString.startsWith(currentPrefix)) {
                    questionStrings.remove(lastQuestionString);
                    currentQuestionString = lastQuestionString + " ";
                }
            }
            //доработаем текущую строку. Перейдем к следующему вопросу если нужно.
            currentQuestionString = currentQuestionString + answererPosition;
            if (userInput.equals("Ответ принят")) {
                currentQuestionString = currentQuestionString + "+";
                questionNumber++;
            } else {
                //if не пишем - иных вариантов быть не может
                //проверим, что мы можем еще кому-то задать вопрос
                if (currentQuestionString.split(" ").length == students.size() + 1) { // q7 3 4 2 1 -> некому больше задавать
                    questionNumber++; //огорченные указываем что спрашивать больше не у кого
                }
            }
            //сохраним результат обработки.
            questionStrings.add(currentQuestionString);
            storageService.updateUserStorage(vkId, USER_START_REVIEW_CORE, questionStrings);
            log.info("Сохранен ответ {} на вопрос по индексу {} студента с id {}", userInput, questionNumber, answererId);
            questionNumbers.put(vkId, new QuestionNumberAnswererIdDto(questionNumber, 0L));
            nextStep = USER_START_REVIEW_CORE;
        } else {
            throw new ProcessInputException("Неверный ввод ответов студентов, повторите ввод");
        }
    }

    /**
     * Выбирает кто следующий будет отвечать на вопрос
     *
     * @param vkId           ревьюера
     * @param context        контекст для работы с сервисами и хранилищем
     * @param questionNumber номер вопроса, если выбирать не из кого
     * @return позицию вопроса и отвечающего
     */
    private Long selectNextRandomAnswererStudent(Integer vkId, BotContext context, Integer questionNumber) {
        List<Long> posibleAnswererList = possibleAnswerer.get(vkId);
        if (posibleAnswererList.isEmpty()) {
            Long reviewId = Long.parseLong(storageService.getUserStorage(vkId, USER_MENU).get(0));
            List<User> students = userService.getStudentsByReviewId(reviewId);
            //possibleAnswerer.put(vkId, );
            posibleAnswererList = students.stream().map(User::getId).collect(Collectors.toList());
            List<String> answerList = storageService.getUserStorage(vkId, USER_START_REVIEW_CORE);
            //проверку на пустоту не делаем, т.к. изначально при первом попадании в этот метод сюда не зайдем
            String lastAskedQuestionString = answerList.get(answerList.size() - 1);//получим последнюю строку
            if (lastAskedQuestionString.startsWith("q" + questionNumber)) {
                //значит вопрос не закрыт. Т.к. при закрытии делается автоинкремент. не зависимо от того, был дан правильный ответ или нет
                //Произведем фильтрацию
                String lastAnswererListString = lastAskedQuestionString.substring(questionNumber.toString().length() + 2);//q+ пробел
                List<String> studentPositionThatAnswerBefore = Arrays.asList((lastAnswererListString).split(" "));
                List<Long> studentThatAnswerBefore = new ArrayList<>();
                for (int i = 0; i < posibleAnswererList.size(); i++) {
                    if (studentPositionThatAnswerBefore.contains(Integer.toString(i))) {
                        studentThatAnswerBefore.add(posibleAnswererList.get(i));
                    }
                }
                posibleAnswererList.removeAll(studentThatAnswerBefore);
            }
        }
        Integer nextAnswererPosition = new Random().nextInt(posibleAnswererList.size()); // от 0 до size-1.
        Long nextAnswererId = posibleAnswererList.get(nextAnswererPosition);
        posibleAnswererList.remove(nextAnswererId);
        possibleAnswerer.put(vkId, posibleAnswererList);
        return nextAnswererId;
    }


    /*
    Новый алгоритм работы.
    Данные о ревью хранятся построчно, где одна строка - один вопрос.
    Если строка оканчивается плюсом - то значит последний ответивший дал верный ответ.
    Если строка не оканчивается плюсом - значит никто не дал правильного ответа.
    Создан отдельный метод для выдачи следующего отвечающего. Генерируется рандомно из списка студентов кто еще не отвечал на данный вопрос.
    Если список студентов для ответа пуст - он обновляется из списка студентов.При этом проверяется, отвечал ли кто-то на этот вопрос ранее.
    Если отвечал - то список фильтруется, исключая отвечавших уже на этот вопрос.
    Не стоит опасаться того, что отфильтрованный список будет пустым.
    Т.к. после каждого ввода ревьюера проверяется, можно ли кому-то еще задать этот вопрос.
    Если нет - происходит переход к следующему вопросу

    По итогу парсится набор строк ответов на вопросы. Парсинг происходит следующим образом
    Создается карта студент\проблемные вопросы.
    Вычленяется позиция вопроса, для получения вопроса.
    Строка сплитится по пробелу на ответы студентов.
    Смотрится последний ответ\элемент. Если он содержит плюс - он(элемент!!) удаляется.
    Для всех оставшихся студентов в строке вопрос добавляется в проблемные вопросы
    После прохождения по всем вопросам, происходит проставление статуса - пройдено или нет(есть проблемные вопросы или нет)
    указывается список проблемных вопросов

    Доработки которые необходимо внедрить для веса.
    Создать карту студент\вес
    При проставленнии вопроса как проблемного, студентам - также добавлять соответствующему студенту вес.
    Изменить алгоритм проверки пройденности ревью. Вместо наличия проблемного вопроса - проверка через карту по критичной массе.
     */
}


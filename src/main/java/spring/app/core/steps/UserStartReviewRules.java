package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.NoDataEnteredException;
import spring.app.exceptions.NoNumbersEnteredException;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.Review;
import spring.app.model.User;
import spring.app.service.abstraction.ReviewService;
import spring.app.service.abstraction.StorageService;
import spring.app.service.abstraction.UserService;
import spring.app.util.StringParser;

import java.time.LocalDateTime;
import java.util.List;

import static spring.app.core.StepSelector.USER_MENU;
import static spring.app.core.StepSelector.START;
import static spring.app.core.StepSelector.USER_START_REVIEW_CORE;
import static spring.app.util.Keyboards.START_KB;

@Component
public class UserStartReviewRules extends Step {

    private final StorageService storageService;
    private final ReviewService reviewService;
    private final UserService userService;

    public UserStartReviewRules(StorageService storageService, ReviewService reviewService, UserService userService) {
        super("", START_KB);
        this.storageService = storageService;
        this.reviewService = reviewService;
        this.userService = userService;
    }

    @Override
    public void enter(BotContext context) {
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException, NoNumbersEnteredException, NoDataEnteredException {
        Integer vkId = context.getVkId();
        String userInput = context.getInput();
        // по нажатию на кнопку "Начать" закрываем ревью и переходим на следующий шаг
        if (userInput.equalsIgnoreCase("Начать")) {
            Long reviewId = Long.parseLong(storageService.getUserStorage(vkId, USER_MENU).get(0));
            Review review = reviewService.getReviewById(reviewId);
            // не даем начать ревью раньше его официального начала (вдруг кто-то присоединится в последний момент)
            if (LocalDateTime.now().isAfter(review.getDate())) {
                review.setIsOpen(false);
                reviewService.updateReview(review);
                sendUserToNextStep(context, USER_START_REVIEW_CORE);
            } else {
                throw new ProcessInputException(String.format("Ты не можешь начать ревью раньше его официального начала.\n Дождись %s и нажми на кнопку \"Начать\" снова.", StringParser.localDateTimeToString(review.getDate())));
            }
        } else if (userInput.equalsIgnoreCase("/start")) {
            storageService.removeUserStorage(vkId, USER_MENU);
            sendUserToNextStep(context, START);
        } else {
            throw new ProcessInputException("Неверная команда, нажми на нопку \"Начать\"");
        }
    }

    @Override
    public String getDynamicText(BotContext context) {
        // Получаю reviewId из хранилища и список студентов, записанных на ревью
        Long reviewId = Long.parseLong(storageService.getUserStorage(context.getVkId(), USER_MENU).get(0));
        List<User> students = userService.getStudentsByReviewId(reviewId);
        // формирую список участников
        StringBuilder studentsList = new StringBuilder("На твоём ревью сегодня присутствуют:\n\n");
        final int[] i = {1};
        students.forEach(user -> {
            studentsList.append(String.format("[%d] %s %s, (https://vk.com/id%d)\n", i[0]++, user.getFirstName(), user.getLastName(), user.getVkId()));
        });

        StringBuilder textBuilder = new StringBuilder(studentsList);
        // формируем информационное сообщение , которое зависит от кол-ва участников ревью
        textBuilder.append("\nСистема будет выдавать вопрос из списка, который тебе необходимо задать,")
                .append(" и имя человека, которому данный вопрос ты должен адресовать.")
                .append(" Если человек не отвечает на вопрос - нажимай на кнопку 'ответ не принят'. Если ответ получен верный - 'ответ принят'\n");
        //на мой взгляд строку ниже надо все же расскоментить. Но ТЗ есть ТЗ AG
        //   textBuilder.append("\n\nВопрос задаётся до ПЕРВОГО человека ответившего правильно");
        String text = textBuilder.toString();
        return text;
    }

    @Override
    public String getDynamicKeyboard(BotContext context) {
        return "";
    }
}


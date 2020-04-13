package spring.app.core.steps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.core.StepSelector;
import spring.app.exceptions.NoDataEnteredException;
import spring.app.exceptions.NoNumbersEnteredException;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.User;
import spring.app.util.StringParser;

import java.util.List;

import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.BACK_KB;

@Component
public class UserStartReviewHangoutsLink extends Step {

    private final static Logger log = LoggerFactory.getLogger(UserStartReviewHangoutsLink.class);

    @Override
    public void enter(BotContext context) {
        text = "Чтобы начать ревью, необходимо создать разговор в hangouts, для этого перейди по ссылке https://hangouts.google.com/hangouts/_/ ," +
                " подключись к диалогу нажми \"пригласить участников\" и скопируй ссылку." +
                " Важно! Не копируй ссылку из браузерной строки, копировать надо именно ссылку из модального окна приглашения участников." +
                " Эту ссылку отправь ответным сообщением. ";
        keyboard = BACK_KB;
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException, NoNumbersEnteredException, NoDataEnteredException {
        Integer vkId = context.getVkId();
        String userInput = context.getInput();
        if (userInput.equalsIgnoreCase("назад")) {
            nextStep = USER_MENU;
            removeUserStorage(vkId, USER_MENU);
        } else if (userInput.equalsIgnoreCase("/start")) {
            nextStep = START;
            removeUserStorage(vkId, USER_MENU);
        } else if (StringParser.isHangoutsLink(userInput)) {
            // достаем reviewId, сохраненный на предыдущем шаге, достаем список студентов, записанных на ревью
            Long reviewId = Long.parseLong(getUserStorage(vkId, USER_MENU).get(0));
            List<User> students = context.getUserService().getStudentsByReviewId(reviewId);
            // отправляем ссылку на ревью каждому участнику
            for (User user : students) {
                // получить текущий step пользователя, чтобы отдать ему в сообщении клавиатуру для этого step
                Step userStep = context.getStepHolder().getSteps().get(user.getChatStep());
                String hangoutsLink = "Ревью началось, вот ссылка для подключения: " + userInput;
                context.getVkService().sendMessage(hangoutsLink, userStep.getKeyboard(), user.getVkId());
                log.warn("Студенту с id {} отправлено сообщение: {}", user.getVkId(), hangoutsLink);
            }
            nextStep = USER_START_REVIEW_RULES;
        } else {
            throw new ProcessInputException("Некорректный ввод данных...");
        }
    }
}

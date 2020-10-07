package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.NoDataEnteredException;
import spring.app.exceptions.NoNumbersEnteredException;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.User;
import spring.app.service.abstraction.StorageService;
import spring.app.service.abstraction.UserService;
import spring.app.service.abstraction.VkService;
import spring.app.util.StringParser;

import java.util.List;

import static spring.app.core.StepSelector.USER_MENU;
import static spring.app.core.StepSelector.START;
import static spring.app.core.StepSelector.USER_START_REVIEW_RULES;
import static spring.app.util.Keyboards.DEF_BACK_KB;

@Component
public class UserStartReviewHangoutsLink extends Step {

    private final StorageService storageService;
    private final UserService userService;
    private final VkService vkService;

    public UserStartReviewHangoutsLink(StorageService storageService, UserService userService, VkService vkService) {
        super("Чтобы начать ревью, необходимо создать разговор в hangouts, для этого перейди по ссылке https://hangouts.google.com/hangouts/_/ ," +
                " подключись к диалогу нажми \"пригласить участников\" и скопируй ссылку." +
                " Важно! Не копируй ссылку из браузерной строки, копировать надо именно ссылку из модального окна приглашения участников." +
                " Эту ссылку отправь ответным сообщением. ", DEF_BACK_KB);
        this.storageService = storageService;
        this.userService = userService;
        this.vkService = vkService;
    }

    @Override
    public void enter(BotContext context) {
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException, NoNumbersEnteredException, NoDataEnteredException {
        Integer vkId = context.getVkId();
        String userInput = context.getInput();
        if (userInput.equalsIgnoreCase("назад")) {
            storageService.removeUserStorage(vkId, USER_MENU);
            sendUserToNextStep(context, USER_MENU);
        } else if (userInput.equalsIgnoreCase("/start")) {
            storageService.removeUserStorage(vkId, USER_MENU);
            sendUserToNextStep(context, START);
        } else if (StringParser.isHangoutsLink(userInput)) {
            // достаем reviewId, сохраненный на предыдущем шаге, достаем список студентов, записанных на ревью
            Long reviewId = Long.parseLong(storageService.getUserStorage(vkId, USER_MENU).get(0));
            List<User> students = userService.getStudentsByReviewId(reviewId);
            // отправляем ссылку на ревью каждому участнику
            for (User user : students) {
                // получить текущий step пользователя, чтобы отдать ему в сообщении клавиатуру для этого step
                Step userStep = context.getStepHolder().getSteps().get(user.getChatStep());
                String hangoutsLink = "Ревью началось, вот ссылка для подключения: " + userInput;
                BotContext studentContext = new BotContext(user, user.getVkId(), "",
                        user.getRole(), context.getStepHolder());
                vkService.sendMessage(hangoutsLink, userStep.getComposeKeyboard(studentContext), user.getVkId());
            }
            sendUserToNextStep(context, USER_START_REVIEW_RULES);
        } else {
            throw new ProcessInputException("Некорректный ввод данных...");
        }
    }

    @Override
    public String getDynamicText(BotContext context) {
        return "";
    }

    @Override
    public String getDynamicKeyboard(BotContext context) {
        return "";
    }
}

package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.ProcessInputException;
import spring.app.service.abstraction.StorageService;

import java.util.Arrays;

import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.DEF_ADMIN_MENU_KB;

@Component
public class AdminMenu extends Step {

    private final StorageService storageService;

    public AdminMenu(StorageService storageService) {
        super("", DEF_ADMIN_MENU_KB);
        this.storageService = storageService;
    }

    @Override
    public void enter(BotContext context) {
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException {
        String command = context.getInput();
        switch (command) {
            case "Пользователи":
                switch (context.getRole().getName()) {
                    case "ADMIN":
                        sendUserToNextStep(context, ADMIN_CHOOSE_ACTION_FOR_USER);
                        break;
                    default:
                        sendUserToNextStep(context, USER_MENU);
                        break;
                }
                break;
            case "/start":
                sendUserToNextStep(context, START);
                break;
            case "Ревью":
                switch (context.getRole().getName()) {
                    case "ADMIN":
                        sendUserToNextStep(context, ADMIN_CHOOSE_ACTION_FOR_REVIEW);
                        break;
                    default:
                        sendUserToNextStep(context, USER_MENU);
                        break;
                }
                break;
            case "Главное меню":
                sendUserToNextStep(context, USER_MENU);
                break;
            default:
                throw new ProcessInputException("Введена неверная команда...");
        }
    }

    @Override
    public String getDynamicText(BotContext context) {
        return String.format("Привет %s! Ты в админке", context.getUser().getFirstName());
    }

    @Override
    public String getDynamicKeyboard(BotContext context) {
        return "";
    }
}

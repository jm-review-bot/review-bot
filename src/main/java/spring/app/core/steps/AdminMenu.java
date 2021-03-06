package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.ProcessInputException;
import spring.app.service.abstraction.StorageService;
import spring.app.util.StringParser;

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
        String command = StringParser.toWordsArray(context.getInput())[0];
        switch (command) {
            case "добавить":
                sendUserToNextStep(context, ADMIN_ADD_USER);
                break;
            case "изменить":
                storageService.updateUserStorage(context.getVkId(), ADMIN_MENU, Arrays.asList("edit"));
                sendUserToNextStep(context, ADMIN_USERS_LIST);//в этом шаге все зависит от режима
                break;
            case "удалить":
                storageService.updateUserStorage(context.getVkId(), ADMIN_MENU, Arrays.asList("delete"));
                sendUserToNextStep(context, ADMIN_USERS_LIST);//в этом шаге все зависит от режима
                break;
            case "/start":
                sendUserToNextStep(context, START);
                break;
            case "ревью":
                sendUserToNextStep(context, ADMIN_EDIT_REVIEW_GET_USER_LIST);
                break;
            case "главное":
            case "Главное":
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

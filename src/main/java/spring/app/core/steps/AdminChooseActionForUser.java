package spring.app.core.steps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.NoDataEnteredException;
import spring.app.exceptions.NoNumbersEnteredException;
import spring.app.exceptions.ProcessInputException;
import spring.app.service.abstraction.StorageService;

import java.util.Arrays;

import static spring.app.core.StepSelector.ADMIN_CHOOSE_ACTION_FOR_USER;
import static spring.app.core.StepSelector.ADMIN_USERS_LIST;
import static spring.app.core.StepSelector.ADMIN_ADD_USER;
import static spring.app.core.StepSelector.ADMIN_MENU;
import static spring.app.util.Keyboards.ACTIONS_FOR_USER;


@Component
public class AdminChooseActionForUser extends Step {

    private final StorageService storageService;

    @Autowired
    public AdminChooseActionForUser(StorageService storageService) {
        super("Выберите действие над пользователем", ACTIONS_FOR_USER);
        this.storageService = storageService;
    }

    @Override
    public void enter(BotContext context) {

    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException, NoNumbersEnteredException, NoDataEnteredException {
        String command = context.getInput();
        switch (command) {
            case "Удалить пользователя":
                storageService.updateUserStorage(context.getVkId(), ADMIN_CHOOSE_ACTION_FOR_USER, Arrays.asList("delete"));
                sendUserToNextStep(context, ADMIN_USERS_LIST);//в этом шаге все зависит от режима
                break;
            case "Добавить пользователя":
                sendUserToNextStep(context, ADMIN_ADD_USER);
                break;
            case "Редактировать пользователя":
                storageService.updateUserStorage(context.getVkId(), ADMIN_CHOOSE_ACTION_FOR_USER, Arrays.asList("edit"));
                sendUserToNextStep(context, ADMIN_USERS_LIST);//в этом шаге все зависит от режима
                break;
            case "Назад":
                sendUserToNextStep(context, ADMIN_MENU);
                break;
            default:
                throw new ProcessInputException("Введена неверная команда...");
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

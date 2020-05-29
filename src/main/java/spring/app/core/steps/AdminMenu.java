package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.ProcessInputException;
import spring.app.util.StringParser;

import java.util.Arrays;

import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.*;

@Component
public class AdminMenu extends Step {

    @Override
    public void enter(BotContext context) {
        text = String.format("Привет %s! Ты в админке", context.getUser().getFirstName());
        keyboard = ADMIN_MENU_KB;
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException {
        String command = StringParser.toWordsArray(context.getInput())[0];
        Integer vkId = context.getVkId();
        if ("добавить".equals(command)) {
            nextStep = ADMIN_ADD_USER;
        } else if ("изменить".equals(command)) {
            context.getStorageService().updateUserStorage(vkId, ADMIN_MENU, Arrays.asList("edit"));
            nextStep = ADMIN_USERS_LIST;//в этом шаге все зависит от режима
        } else if ("удалить".equals(command)) {
            context.getStorageService().updateUserStorage(vkId, ADMIN_MENU, Arrays.asList("delete"));
            nextStep = ADMIN_USERS_LIST;//в этом шаге все зависит от режима
        } else if ("/start".equals(command)) {
            nextStep = START;
        } else if ("главное".equalsIgnoreCase(command)) {
            nextStep = USER_MENU;
        } else {
            keyboard = ADMIN_MENU_KB;
            throw new ProcessInputException("Введена неверная команда...");
        }
    }
}

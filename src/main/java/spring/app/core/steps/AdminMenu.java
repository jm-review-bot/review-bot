package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.ProcessInputException;
import spring.app.util.StringParser;

import java.util.Arrays;

import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.DEF_ADMIN_MENU_KB;

@Component
public class AdminMenu extends Step {

    public AdminMenu() {
        super("", DEF_ADMIN_MENU_KB);
    }

    @Override
    public void enter(BotContext context) {
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException {
        String command = StringParser.toWordsArray(context.getInput())[0];
        Integer vkId = context.getVkId();
        if ("добавить".equals(command)) {
            sendUserToNextStep(context, ADMIN_ADD_USER);//в этом шаге все зависит от режима
        } else if ("изменить".equals(command)) {
            context.getStorageService().updateUserStorage(vkId, ADMIN_MENU, Arrays.asList("edit"));
            sendUserToNextStep(context, ADMIN_USERS_LIST);//в этом шаге все зависит от режима
        } else if ("удалить".equals(command)) {
            context.getStorageService().updateUserStorage(vkId, ADMIN_MENU, Arrays.asList("delete"));
            sendUserToNextStep(context, ADMIN_USERS_LIST);//в этом шаге все зависит от режима
        } else if ("/start".equals(command)) {
            sendUserToNextStep(context, START);
        } else if ("главное".equalsIgnoreCase(command)) {
            sendUserToNextStep(context, USER_MENU);
        } else {
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

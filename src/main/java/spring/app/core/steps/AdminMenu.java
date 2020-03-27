package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.ProcessInputException;
import spring.app.util.StringParser;

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
        if ("Добавить".equalsIgnoreCase(command)) {
            nextStep = ADMIN_ADD_USER;
        } else if ("Удалить".equalsIgnoreCase(command)) {
            nextStep = ADMIN_REMOVE_USER;
        } else if ("/start".equalsIgnoreCase(command)) {
            nextStep = START;
        } else {
            keyboard = ADMIN_MENU_KB;
            throw new ProcessInputException("Введена неверная команда...");
        }
    }
}

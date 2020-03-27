package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.ProcessInputException;

import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.*;

@Component
public class AdminMenu extends Step {

    @Override
    public void enter(BotContext context) {
        text = "Привет %username%! Ты в админке";
        keyboard = ADMIN_MENU_KB;
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException {
        String[] words = context.getInput().trim().split(" ");
        String command = words[0];
        if ("Добавить".equals(command)) {
            nextStep = ADMIN_ADD_USER;
        } else if ("Удалить".equals(command)) {
            nextStep = ADMIN_REMOVE_USER;
        } else if ("/start".equals(command)) {
            nextStep = START;
        } else {
            keyboard = ADMIN_MENU_KB;
            throw new ProcessInputException("Введена неверная команда...");
        }
    }
}

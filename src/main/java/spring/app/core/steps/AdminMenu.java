package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.ProcessInputException;
import spring.app.util.Keyboards;

import static spring.app.core.StepSelector.*;

@Component
public class AdminMenu extends Step {
    private String text;
    private String keyboard;

    @Override
    public void enter(BotContext context) {
        text = "Привет %username%! Ты в админке";
        keyboard = Keyboards.ADMIN_MENU_KB;
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
            keyboard = Keyboards.ADMIN_MENU_KB;
            throw new ProcessInputException("Введена неверная команда...");
        }
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public String getKeyboard() {
        return keyboard;
    }
}

package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.core.StepSelector;
import spring.app.exceptions.ProcessInputException;
import spring.app.util.Keyboards;

@Component
public class AdminMenu extends Step {
    private String text;
    private String keyboard;

    @Override
    public void enter(BotContext context) {
        text = "Привет %username%! Ты в админке";
        keyboard = Keyboards.adminMenu;
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException {
        String[] words = context.getInput().trim().split(" ");
        String command = words[0];
        if ("Добавить".equals(command)) {
            nextStep = StepSelector.AdminAddUser;
        } else if ("Удалить".equals(command)) {
            nextStep = StepSelector.AdminRemoveUser;
        } else if ("/start".equals(command)) {
            nextStep = StepSelector.Start;
        } else {
            keyboard = Keyboards.adminMenu;
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

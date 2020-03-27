package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.ProcessInputException;

import static spring.app.util.Keyboards.*;

@Component
public class AdminAddUser extends Step {
    private String text;
    private String keyboard;

    @Override
    public void enter(BotContext context) {
        text = "Тут будем добавлять юзеров";
        keyboard = NO_KB;
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException {

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

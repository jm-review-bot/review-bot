package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.ProcessInputException;

import static spring.app.util.Keyboards.*;

@Component
public class UserMenu extends Step {
    private String text;
    private String keyboard;

    @Override
    public void enter(BotContext context) {
        // TODO проверку из ТЗ
        text = "Пользователь с таким vk id не найден в базе. Обратитесь к Герману Севостьянову или Станиславу Сорокину";
        keyboard = DEFAULT_KB;
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

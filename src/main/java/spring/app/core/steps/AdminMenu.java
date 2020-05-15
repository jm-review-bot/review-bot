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
        switch (command) {
            case "добавить":
                nextStep = ADMIN_ADD_USER;
                break;
            case "удалить":
                nextStep = ADMIN_REMOVE_USER;
                break;
            case "/start":
                nextStep = START;
                break;
            case "ревью":
                nextStep = ADMIN_EDIT_REVIEW;
                break;
            case "главное":
            case "Главное":
                nextStep = USER_MENU;
                break;
            default:
                keyboard = ADMIN_MENU_KB;
                throw new ProcessInputException("Введена неверная команда...");
        }
    }
}

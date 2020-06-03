package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.ProcessInputException;
import spring.app.util.StringParser;

import static spring.app.core.StepSelector.ADMIN_MENU;
import static spring.app.core.StepSelector.USER_MENU;
import static spring.app.util.Keyboards.ADMIN_START_KB;
import static spring.app.util.Keyboards.START_KB;

@Component
public class Start extends Step {

    public Start() {
        //статического контекста нет
        super("", "");
    }

    @Override
    public void enter(BotContext context) {
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException {
        String command = StringParser.toWordsArray(context.getInput())[0];
        if (command.equals("/admin")
                && context.getRole().isAdmin()) { // валидация что юзер имеет роль админ
            this.sendUserToNextStep(context, ADMIN_MENU);
        } else if (command.equals("начать")) {
            sendUserToNextStep(context, USER_MENU);
        } else {
            throw new ProcessInputException("Введена неверная команда...");
        }
    }

    @Override
    public String getDynamicText(BotContext context) {
        if (context.getRole().isAdmin()) {
            return "Этот Бот создан для прохождения ревью. \nНажми \"Начать\" для запуска.";
        } else {
            return "Этот Бот создан для прохождения ревью.\n " +
                    "Нажми \"начать\" для запуска или введи команду /admin для перехода в админку.";
        }
    }

    @Override
    public String getDynamicKeyboard(BotContext context) {
        if (context.getRole().isAdmin()) { // валидация что юзер имеет роль админ
            return ADMIN_START_KB;
        } else {
            return START_KB;
        }
    }
}

package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.core.StepSelector;
import spring.app.exceptions.ProcessInputException;
import spring.app.util.Keyboards;

@Component
public class Start extends Step {
    private String text;
    private String keyboard;

    @Override
    public void enter(BotContext context) {
        text = "Привет! Этот Бот создан для прохождения ревью. \nНажми \"Начать\" для запуска.";
        keyboard = Keyboards.start;
        if (context.getRole().isAdmin()) { // валидация что юзер имеет роль админ
            text = "Привет! Этот Бот создан для прохождения ревью. " +
                    "\nНажми \"Начать\" для запуска или введи команду /admin для перехода в админку.";
            keyboard = Keyboards.noKeyboard;
        }
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException {
        // вызываем правильный внешний обработчик команд (пока его нет)
        String[] words = context.getInput().trim().split(" ");
        String command = words[0];
        if (command.equals("/admin")
                && context.getRole().isAdmin()) { // валидация что юзер имеет роль админ
            nextStep = StepSelector.AdminMenu;
        } else if (command.equals("Начать")) {
            nextStep = StepSelector.UserMenu;
        } else if (command.equals("/start")) {
            nextStep = StepSelector.Start;
        } else {
            keyboard = Keyboards.start;
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

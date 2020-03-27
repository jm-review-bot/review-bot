package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.ProcessInputException;

import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.*;

@Component
public class Start extends Step {

    @Override
    public void enter(BotContext context) {
        text = "Привет! Этот Бот создан для прохождения ревью. \nНажми \"Начать\" для запуска.";
        keyboard = START_KB;
        if (context.getRole().isAdmin()) { // валидация что юзер имеет роль админ
            text = "Привет! Этот Бот создан для прохождения ревью. " +
                    "\nНажми \"Начать\" для запуска или введи команду /admin для перехода в админку.";
            keyboard = NO_KB;
        }
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException {
        // вызываем правильный внешний обработчик команд (пока его нет)
        String[] words = context.getInput().trim().split(" ");
        String command = words[0];
        if (command.equals("/admin")
                && context.getRole().isAdmin()) { // валидация что юзер имеет роль админ
            nextStep = ADMIN_MENU;
        } else if (command.equals("Начать")) {
            nextStep = USER_MENU;
        } else if (command.equals("/start")) {
            nextStep = START;
        } else {
            keyboard = START_KB;
            throw new ProcessInputException("Введена неверная команда...");
        }
    }
}

package spring.app.core.steps;

import spring.app.core.BotContext;
import spring.app.exceptions.ProcessInputException;

import static spring.app.core.StepSelector.USER_MENU;
import static spring.app.util.Keyboards.*;

public class UserTakeReviewSuccess extends Step {
    @Override
    public void enter(BotContext context) {
        text = "Супер! Твоё ревью добавлено в сетку расписания," +
                " в день и время когда оно наступит напиши в чат сообщение \"начать ревью\" или нажми на кнопку \"Начать ревью\"";
        keyboard = GO_MAIN_MENU_KB;
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException {

        // здесть типа сохранять в базу и переходить в главное меню,
        // или на след шаг и там уже выдавать сообщение, что добавлено и для перехода нажмите главное меню

        if (context.getInput().equals("Главное меню")) {
            nextStep = USER_MENU;
            keyboard = USER_START_KB;
        } else {
            keyboard = START_KB;
            throw new ProcessInputException("Введена неверная команда...");
        }
    }
}

package spring.app.core.steps;

import spring.app.core.BotContext;
import spring.app.exceptions.ProcessInputException;

import static spring.app.core.StepSelector.USER_MENU;
import static spring.app.util.Keyboards.*;

public class UserAddReviewDate extends Step {
    @Override
    public void enter(BotContext context) {
        text = "Укажите время и дату для принятия ревью в формате ДД.ММ.ГГГГ ЧЧ:ММ " +
                "по Московскому часовому поясу. Пример корректного ответа 02.06.2020 17:30\n\n" +
                "Или нажмите на кнопку \"Назад\" для возврата к предыдущему меню";
        keyboard = BACK_KB;
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException {
        String currentInput = context.getInput();
        //временная заглушка
        if (currentInput.equalsIgnoreCase("назад")) {
            nextStep = USER_MENU;
            keyboard = USER_START_KB;
        }
    }
}

package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.NoDataEnteredException;
import spring.app.exceptions.NoNumbersEnteredException;
import spring.app.exceptions.ProcessInputException;
import spring.app.util.StringParser;

import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.YES_NO_KB;

@Component
public class UserFeedbackConfirmation extends Step {

    @Override
    public void enter(BotContext context) {
        text = "Для улучшения качества обучения дайте обратную связь после ревью.";
        keyboard = YES_NO_KB;
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException, NoNumbersEnteredException, NoDataEnteredException {
        String currentInput = context.getInput();
        String wordInput = StringParser.toWordsArray(currentInput)[0];

        if (wordInput.equals("нет")) {
            nextStep = USER_MENU;
        } else if (wordInput.equals("да")) {
            nextStep = USER_FEEDBACK_REVIEW_ASSESSMENT;
        } else {
            throw new ProcessInputException("Введена неверная команда. Нажми \"Да\" чтобы оценить !!!!!! или \"Нет\" для выхода в главное меню.");
        }
    }
}

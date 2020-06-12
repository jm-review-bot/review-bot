package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.NoDataEnteredException;
import spring.app.exceptions.NoNumbersEnteredException;
import spring.app.exceptions.ProcessInputException;
import spring.app.util.StringParser;

import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.FEEDBACK_CONFIRM_KB;

@Component
public class UserFeedbackConfirmation extends Step {

    public UserFeedbackConfirmation() {
        super("Для улучшения качества обучения дайте обратную связь после ревью.", FEEDBACK_CONFIRM_KB);
    }

    @Override
    public void enter(BotContext context) {
    }

    @Override
    public void processInput(BotContext context)
            throws ProcessInputException, NoNumbersEnteredException, NoDataEnteredException {

        String currentInput = context.getInput();
        String wordInput = StringParser.toWordsArray(currentInput)[0];

        if (wordInput.equals("отказаться")) {
            //clear cash
            context.getStorageService().removeUserStorage(context.getVkId(), USER_FEEDBACK_CONFIRMATION);

            sendUserToNextStep(context, USER_MENU);
        } else if (wordInput.equals("начать")) {
            sendUserToNextStep(context, USER_FEEDBACK_REVIEW_ASSESSMENT);
        } else {
            throw new ProcessInputException("Введена неверная команда. Нажми \"Начать\" чтобы оставить отзыв" +
                    " или \"Отказаться\" для выхода в главное меню.");
        }
    }

    @Override
    public String getDynamicText(BotContext context) {
        return "";
    }

    @Override
    public String getDynamicKeyboard(BotContext context) {
        return "";
    }
}

package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.NoDataEnteredException;
import spring.app.exceptions.NoNumbersEnteredException;
import spring.app.exceptions.ProcessInputException;

import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.*;

@Component
public class AdminChooseActionForReview extends Step {

    public AdminChooseActionForReview() {
        super("Выберите действие над ревью", ACTIONS_FOR_REVIEW);
    }

    @Override
    public void enter(BotContext context) {

    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException, NoNumbersEnteredException, NoDataEnteredException {
        String command = context.getInput();
        switch (command) {
            case "Редактировать ревью":
                sendUserToNextStep(context, ADMIN_EDIT_REVIEW_GET_USER_LIST);
                break;
            case "Сделать пройденным":
                sendUserToNextStep(context, ADMIN_SET_PASSED_REVIEW_GET_USERS_LIST);
                break;
            case "Назад":
                sendUserToNextStep(context, ADMIN_MENU);
                break;
            default:
                throw new ProcessInputException("Введена неверная команда...");
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

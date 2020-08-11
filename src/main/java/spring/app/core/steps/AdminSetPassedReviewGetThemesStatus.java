package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.NoDataEnteredException;
import spring.app.exceptions.NoNumbersEnteredException;
import spring.app.exceptions.ProcessInputException;
import spring.app.service.abstraction.StorageService;

import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.*;

@Component
public class AdminSetPassedReviewGetThemesStatus extends Step {

    StorageService storageService;

    public AdminSetPassedReviewGetThemesStatus(StorageService storageService) {
        super("", DEF_BACK_KB);
        this.storageService = storageService;
    }

    @Override
    public void enter(BotContext context) {

    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException, NoNumbersEnteredException, NoDataEnteredException {
        String command = context.getInput();
        if (command.equals("Назад")) {
          sendUserToNextStep(context, ADMIN_SET_PASSED_REVIEW_GET_USERS_LIST);
        } else {
            throw new ProcessInputException("Введена неверная команда...");
        }
    }

    @Override
    public String getDynamicText(BotContext context) {
        return "Еее роцк!";
    }

    @Override
    public String getDynamicKeyboard(BotContext context) {
        return "";
    }
}

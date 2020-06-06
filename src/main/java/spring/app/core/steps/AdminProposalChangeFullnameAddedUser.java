package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.NoDataEnteredException;
import spring.app.exceptions.NoNumbersEnteredException;
import spring.app.exceptions.ProcessInputException;

import java.util.List;

import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.CHANGE_OR_NOT_ADDED_USER_FULLNAME;
import static spring.app.util.Keyboards.DEF_BACK_KB;

/**
 * @author AkiraRokudo on 23.05.2020 in one of sun day
 */
@Component
public class AdminProposalChangeFullnameAddedUser extends Step {

    public AdminProposalChangeFullnameAddedUser() {
        super("", "");
    }

    @Override
    public void enter(BotContext context) {
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException, NoNumbersEnteredException, NoDataEnteredException {
        String currentInput = context.getInput();
        if (currentInput.equals("ввести новое имя фамилию")) {
            context.getStorageService().removeUserStorage(context.getVkId(), ADMIN_PROPOSAL_CHANGE_FULLNAME_ADDED_USER);
            sendUserToNextStep(context, ADMIN_CHANGE_ADDED_USER_FULLNAME);
        } else if (currentInput.equals("оставить имя как есть")) {
            sendUserToNextStep(context, ADMIN_SET_THEME_ADDED_USER);
        } else {
            throw new ProcessInputException("Введена неверная команда...");
        }
    }

    @Override
    public String getDynamicText(BotContext context) {

        List<String> savedInput = context.getStorageService().getUserStorage(context.getVkId(), ADMIN_PROPOSAL_CHANGE_FULLNAME_ADDED_USER);

        if (savedInput != null) {
            return savedInput.get(0);
        } else {
            return "Изменение Имени и Фамилии добавленного пользователя невозможно. Нажмите 'Назад' чтобы вернуться на шаг добавления пользователей";
        }
    }

    @Override
    public String getDynamicKeyboard(BotContext context) {

        List<String> savedInput = context.getStorageService().getUserStorage(context.getVkId(), ADMIN_PROPOSAL_CHANGE_FULLNAME_ADDED_USER);

        if (savedInput != null) {
            return CHANGE_OR_NOT_ADDED_USER_FULLNAME;
        } else {
            return DEF_BACK_KB;
        }
    }
}

package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.NoDataEnteredException;
import spring.app.exceptions.NoNumbersEnteredException;
import spring.app.exceptions.ProcessInputException;
import spring.app.service.abstraction.StorageService;

import java.util.List;

import static spring.app.core.StepSelector.ADMIN_PROPOSAL_CHANGE_FULLNAME_ADDED_USER;
import static spring.app.core.StepSelector.ADMIN_CHANGE_ADDED_USER_FULLNAME;
import static spring.app.core.StepSelector.ADMIN_SET_THEME_ADDED_USER;
import static spring.app.util.Keyboards.CHANGE_OR_NOT_ADDED_USER_FULLNAME;
import static spring.app.util.Keyboards.DEF_BACK_KB;

/**
 * @author AkiraRokudo on 23.05.2020 in one of sun day
 */
@Component
public class AdminProposalChangeFullnameAddedUser extends Step {

    private final StorageService storageService;

    public AdminProposalChangeFullnameAddedUser(StorageService storageService) {
        super("", "");
        this.storageService = storageService;
    }

    @Override
    public void enter(BotContext context) {
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException, NoNumbersEnteredException, NoDataEnteredException {
        String currentInput = context.getInput();
        if (currentInput.equals("ввести новое имя фамилию")) {
            storageService.removeUserStorage(context.getVkId(), ADMIN_PROPOSAL_CHANGE_FULLNAME_ADDED_USER);
            sendUserToNextStep(context, ADMIN_CHANGE_ADDED_USER_FULLNAME);
        } else if (currentInput.equals("оставить имя как есть")) {
            sendUserToNextStep(context, ADMIN_SET_THEME_ADDED_USER);
        } else {
            throw new ProcessInputException("Введена неверная команда...");
        }
    }

    @Override
    public String getDynamicText(BotContext context) {

        List<String> savedInput = storageService.getUserStorage(context.getVkId(), ADMIN_PROPOSAL_CHANGE_FULLNAME_ADDED_USER);

        return savedInput != null ? savedInput.get(0) :
                "Изменение Имени и Фамилии добавленного пользователя невозможно. Нажмите 'Назад' чтобы вернуться на шаг добавления пользователей";
    }

    @Override
    public String getDynamicKeyboard(BotContext context) {

        List<String> savedInput = storageService.getUserStorage(context.getVkId(), ADMIN_PROPOSAL_CHANGE_FULLNAME_ADDED_USER);
        return savedInput != null ? CHANGE_OR_NOT_ADDED_USER_FULLNAME : DEF_BACK_KB;
    }
}

package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.NoDataEnteredException;
import spring.app.exceptions.NoNumbersEnteredException;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.Role;
import spring.app.service.abstraction.StorageService;
import spring.app.service.abstraction.UserService;
import spring.app.service.abstraction.VkService;

import java.util.List;

import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.BACK_KB;
import static spring.app.util.Keyboards.CHANGE_OR_NOT_ADDED_USER_FULLNAME;

/**
 * @author AkiraRokudo on 23.05.2020 in one of sun day
 */
@Component
public class AdminProposalChangeFullnameAddedUser extends Step {

    private final StorageService storageService;

    public AdminProposalChangeFullnameAddedUser(StorageService storageService) {
        this.storageService = storageService;
    }

    @Override
    public void enter(BotContext context) {
        Integer vkId = context.getVkId();

        List<String> savedInput = storageService.getUserStorage(vkId, ADMIN_PROPOSAL_CHANGE_FULLNAME_ADDED_USER);
        if (savedInput != null) {
            text = savedInput.get(0);
            storageService.removeUserStorage(vkId, ADMIN_PROPOSAL_CHANGE_FULLNAME_ADDED_USER);
            keyboard = CHANGE_OR_NOT_ADDED_USER_FULLNAME;
        } else {
            text = "Изменение Имени и Фамилии добавленного пользователя невозможно. Нажмите 'Назад' чтобы вернуться на шаг добавления пользователей";
            keyboard = BACK_KB;
        }

    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException, NoNumbersEnteredException, NoDataEnteredException {
        String currentInput = context.getInput();
        if(currentInput.equals("ввести новое имя фамилию")) {
            nextStep = ADMIN_CHANGE_ADDED_USER_FULLNAME;
        } else if(currentInput.equals("оставить имя как есть")) {
            nextStep = ADMIN_SET_THEME_ADDED_USER;
        } else {
            throw new ProcessInputException("Введена неверная команда...");
        }
    }
}

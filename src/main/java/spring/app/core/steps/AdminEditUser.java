package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.NoDataEnteredException;
import spring.app.exceptions.NoNumbersEnteredException;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.User;
import spring.app.service.abstraction.StorageService;

import java.util.Arrays;
import java.util.List;

import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.*;

/**
 * @author AkiraRokudo on 27.05.2020 in one of sun day
 */
@Component
public class AdminEditUser extends Step {

    public AdminEditUser() {
        super("", "");
    }

    @Override
    public void enter(BotContext context) {

    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException, NoNumbersEnteredException, NoDataEnteredException {
        String inputText = context.getInput();
        if ("изменить имя".equals(inputText)) {
            sendUserToNextStep(context, ADMIN_INPUT_NEW_FULLNAME_EDITED_USER);
        } else if ("изменить вкИд".equals(inputText)) {
            sendUserToNextStep(context, ADMIN_INPUT_NEW_VKID_EDITED_USER);
        } else {
            throw new ProcessInputException("Введена неверная команда...");
        }
    }

    @Override
    public String getDynamicText(BotContext context) {
        Integer vkId = context.getVkId();
        List<String> savedInput = context.getStorageService().getUserStorage(vkId, ADMIN_USERS_LIST);
        String text;

        if (savedInput != null) {
            Long userId = Long.parseLong(savedInput.get(0));
            User selectedUser = context.getUserService().getUserById(userId);
            text = String.format("Вы выбрали %s %s (%s). Выберите действие", selectedUser.getFirstName(), selectedUser.getLastName(), selectedUser.getVkId());
        } else {
            text = "Изменение параметров выбранного пользователя невозможно. Нажмите 'Назад' чтобы вернуться к списку пользователей";
        }
        return text;
    }

    @Override
    public String getDynamicKeyboard(BotContext context) {
        Integer vkId = context.getVkId();
        List<String> savedInput = context.getStorageService().getUserStorage(vkId, ADMIN_USERS_LIST);

        if (savedInput != null) {
            return CHANGE_FULLNAME_VKID_EDITING_USER_OR_BACK;
        }
        else {
            return DEF_BACK_KB;
        }
    }
}

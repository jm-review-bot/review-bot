package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.NoDataEnteredException;
import spring.app.exceptions.NoNumbersEnteredException;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.User;
import spring.app.service.abstraction.StorageService;
import spring.app.service.abstraction.UserService;

import java.util.Arrays;
import java.util.List;

import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.*;

/**
 * @author AkiraRokudo on 27.05.2020 in one of sun day
 */
@Component
public class AdminEditUser extends Step {

    private final StorageService storageService;
    private final UserService userService;

    public AdminEditUser(StorageService storageService, UserService userService) {
        this.storageService = storageService;
        this.userService = userService;
    }

    @Override
    public void enter(BotContext context) {
        Integer vkId = context.getVkId();
        List<String> savedInput = storageService.getUserStorage(vkId, ADMIN_USERS_LIST);

        if (savedInput != null) {
            Long userId = Long.parseLong(savedInput.get(0));
            User selectedUser = userService.getUserById(userId);
            text = String.format("Вы выбрали %s %s (%s). Выберите действие", selectedUser.getFirstName(), selectedUser.getLastName(), selectedUser.getVkId());
            keyboard = CHANGE_FULLNAME_VKID_EDITING_USER_OR_BACK;
        } else {
            text = "Изменение параметров выбранного пользователя невозможно. Нажмите 'Назад' чтобы вернуться к списку пользователей";
            keyboard = BACK_KB;
        }
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException, NoNumbersEnteredException, NoDataEnteredException {
        String inputText = context.getInput();

        if ("изменить имя".equals(inputText)) {
            nextStep = ADMIN_INPUT_NEW_FULLNAME_EDITED_USER;
        } else if ("изменить вкИд".equals(inputText)) {
            nextStep = ADMIN_INPUT_NEW_VKID_EDITED_USER;
        } else {
            throw new ProcessInputException("Введена неверная команда...");
        }
    }
}

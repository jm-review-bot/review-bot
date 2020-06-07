package spring.app.core.steps;

import com.vk.api.sdk.actions.Storage;
import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.core.StepSelector;
import spring.app.exceptions.NoDataEnteredException;
import spring.app.exceptions.NoNumbersEnteredException;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.User;
import spring.app.service.abstraction.StorageService;
import spring.app.service.abstraction.UserService;

import java.util.Arrays;
import java.util.List;

import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.YES_OR_CANCEL;

/**
 * @author AkiraRokudo on 27.05.2020 in one of sun day
 */
@Component
public class AdminConfirmChangeEditedUserFullname extends Step {

    private final UserService userService;
    private final StorageService storageService;

    public AdminConfirmChangeEditedUserFullname(UserService userService, StorageService storageService) {
        this.userService = userService;
        this.storageService = storageService;
    }

    @Override
    public void enter(BotContext context) {
        Integer vkId = context.getVkId();
        String userId = storageService.getUserStorage(vkId, StepSelector.ADMIN_USERS_LIST).get(0);
        User editingUser = userService.getUserById(Long.parseLong(userId));
        List<String> userFullname = storageService.getUserStorage(vkId, ADMIN_INPUT_NEW_FULLNAME_EDITED_USER);
        text = String.format("Вы действительно хотите сменить пользователю %s %s (%s) имя на {%s} {%s}? \n",
                editingUser.getFirstName(),
                editingUser.getLastName(),
                editingUser.getVkId(),
                userFullname.get(0),
                userFullname.get(1)
        );
        keyboard = YES_OR_CANCEL;
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException, NoNumbersEnteredException, NoDataEnteredException {
        String input = context.getInput();
        Integer vkId = context.getVkId();

        if ("да".equals(input)) {
            String userId = storageService.getUserStorage(vkId, StepSelector.ADMIN_USERS_LIST).get(0);
            User editingUser = userService.getUserById(Long.parseLong(userId));
            List<String> userFullname = storageService.getUserStorage(vkId, ADMIN_INPUT_NEW_FULLNAME_EDITED_USER);
            String oldFirstName = editingUser.getFirstName();
            String oldLastName = editingUser.getLastName();
            editingUser.setFirstName(userFullname.get(0));
            editingUser.setLastName(userFullname.get(1));
            userService.updateUser(editingUser);
            storageService.removeUserStorage(vkId, ADMIN_INPUT_NEW_FULLNAME_EDITED_USER);
            //подготовим сообщение для вывода после изменения
            storageService.updateUserStorage(vkId, ADMIN_USERS_LIST,
                    Arrays.asList(String.format("Имя пользователя %s %s (%s) успешно изменено на {%s} {%s}\n"
                            , oldFirstName, oldLastName, editingUser.getVkId(), userFullname.get(0), userFullname.get(1))));
            nextStep = ADMIN_USERS_LIST;
        } else if ("отмена".equals(input)) {
            storageService.removeUserStorage(vkId, ADMIN_INPUT_NEW_FULLNAME_EDITED_USER);
            nextStep = ADMIN_EDIT_USER;
        } else {
            throw new ProcessInputException("Введена неверная команда...");
        }
    }
}

package spring.app.core.steps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final static Logger log = LoggerFactory.getLogger(AdminConfirmChangeEditedUserFullname.class);

    private final StorageService storageService;
    private final UserService userService;

    public AdminConfirmChangeEditedUserFullname(StorageService storageService, UserService userService) {
        super("", YES_OR_CANCEL);
        this.storageService = storageService;
        this.userService = userService;
    }

    @Override
    public void enter(BotContext context) {

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
            String newFirstName = userFullname.get(0);
            String newLastName = userFullname.get(1);
            editingUser.setFirstName(newFirstName);
            editingUser.setLastName(newLastName);
            userService.updateUser(editingUser);
            storageService.removeUserStorage(vkId, ADMIN_INPUT_NEW_FULLNAME_EDITED_USER);
            //подготовим сообщение для вывода после изменения
            storageService.updateUserStorage(vkId, ADMIN_USERS_LIST,
                    Arrays.asList(String.format("Имя пользователя %s %s (%s) успешно изменено на {%s} {%s}\n",
                            oldFirstName, oldLastName, editingUser.getVkId(), newFirstName, newLastName)));
            if (vkId.equals(editingUser.getVkId())) {
                context.getUser().setFirstName(newFirstName);
                context.getUser().setLastName(newLastName);
            }
            log.info(
                    "Admin (vkId={}) изменил имя пользователя (vkId={}) с {} {} на {} {}",
                    context.getUser().getVkId(), editingUser.getVkId(), oldLastName, oldFirstName, newLastName, newFirstName
            );
            sendUserToNextStep(context, ADMIN_USERS_LIST);
        } else if ("отмена".equals(input)) {
            storageService.removeUserStorage(vkId, ADMIN_INPUT_NEW_FULLNAME_EDITED_USER);
            sendUserToNextStep(context, ADMIN_EDIT_USER);
        } else {
            throw new ProcessInputException("Введена неверная команда...");
        }
    }

    @Override
    public String getDynamicText(BotContext context) {
        Integer vkId = context.getVkId();
        String userId = storageService.getUserStorage(vkId, StepSelector.ADMIN_USERS_LIST).get(0);
        User editingUser = userService.getUserById(Long.parseLong(userId));
        List<String> userFullname = storageService.getUserStorage(vkId, ADMIN_INPUT_NEW_FULLNAME_EDITED_USER);
        return String.format("Вы действительно хотите сменить пользователю %s %s (%s) имя на {%s} {%s}? \n",
                editingUser.getFirstName(),
                editingUser.getLastName(),
                editingUser.getVkId(),
                userFullname.get(0),
                userFullname.get(1)
        );
    }

    @Override
    public String getDynamicKeyboard(BotContext context) {
        return "";
    }

}

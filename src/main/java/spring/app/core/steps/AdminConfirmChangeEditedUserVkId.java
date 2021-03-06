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

import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.YES_OR_CANCEL;

/**
 * @author AkiraRokudo on 27.05.2020 in one of sun day
 */
@Component
public class AdminConfirmChangeEditedUserVkId extends Step {
    private final static Logger log = LoggerFactory.getLogger(AdminConfirmChangeEditedUserVkId.class);

    private final StorageService storageService;
    private final UserService userService;

    public AdminConfirmChangeEditedUserVkId(StorageService storageService, UserService userService) {
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
            String newUserVkId = storageService.getUserStorage(vkId, ADMIN_INPUT_NEW_VKID_EDITED_USER).get(0);
            Integer oldVkId = editingUser.getVkId();
            editingUser.setVkId(Integer.parseInt(newUserVkId));
            if (vkId.equals(oldVkId)) {
                context.getUser().setVkId(Integer.parseInt(newUserVkId));
            }
            userService.updateUser(editingUser);
            storageService.removeUserStorage(vkId, ADMIN_INPUT_NEW_FULLNAME_EDITED_USER);
            //удалим все его хранилища по старому айдишнику - обращаться к ним будет некорректно, а хранить - глупо
            storageService.clearUsersOfStorage(oldVkId);
            //подготовим сообщение для вывода после изменения
            storageService.updateUserStorage(vkId, ADMIN_USERS_LIST,
                    Arrays.asList(String.format("Vkid пользователя %s %s (%s) успешно изменено на {%s}\n"
                            , editingUser.getFirstName(), editingUser.getLastName(), oldVkId, newUserVkId)));
            log.info(
                    "Admin (vkId={}) изменил vkId пользователя с {} на {}",
                    context.getUser().getVkId(), oldVkId, newUserVkId
            );
            sendUserToNextStep(context, ADMIN_USERS_LIST);
        } else if ("отмена".equals(input)) {
            storageService.removeUserStorage(vkId, ADMIN_INPUT_NEW_VKID_EDITED_USER);
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
        String userVkId = storageService.getUserStorage(vkId, ADMIN_INPUT_NEW_VKID_EDITED_USER).get(0);
        return String.format("Вы действительно хотите сменить пользователю %s %s (%s) vkid на {%s}? \n",
                editingUser.getFirstName(),
                editingUser.getLastName(),
                editingUser.getVkId(),
                userVkId
        );
    }

    @Override
    public String getDynamicKeyboard(BotContext context) {
        return "";
    }

}

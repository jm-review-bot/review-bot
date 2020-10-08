package spring.app.core.steps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
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

import static spring.app.core.StepSelector.ADMIN_USERS_LIST;

@Component
public class AdminInputNewPasswordEditedUser extends Step {
    private final static Logger log = LoggerFactory.getLogger(AdminInputNewPasswordEditedUser.class);


    private final StorageService storageService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AdminInputNewPasswordEditedUser(StorageService storageService, UserService userService, PasswordEncoder passwordEncoder) {
        super("В ответ на данное сообщение отправьте новый пароль", "");
        this.storageService = storageService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void enter(BotContext context) {

    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException, NoNumbersEnteredException, NoDataEnteredException {
        String newPassword = passwordEncoder.encode(context.getInput());
        Integer vkId = context.getVkId();
        Long userId = Long.parseLong(storageService.getUserStorage(vkId, StepSelector.ADMIN_USERS_LIST).get(0));
        User editingUser = userService.getUserById(userId);
        User user = context.getUser();
        if(user.getId().equals(userId)){
            user.setPassword(newPassword);
            userService.updateUser(user);
        }
        else {
            editingUser.setPassword(passwordEncoder.encode(newPassword));
            userService.updateUser(editingUser);
        }
        //подготовим сообщение для вывода после изменения
        storageService.updateUserStorage(vkId, ADMIN_USERS_LIST,
                Arrays.asList("Пароль успешно изменен!\n"));
        log.info(
                "Admin (vkId={}) изменил пароль пользователя (vkId={})",
                context.getUser().getVkId(), editingUser.getVkId()
        );
        sendUserToNextStep(context, ADMIN_USERS_LIST);
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

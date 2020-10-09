package spring.app.core.steps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.NoDataEnteredException;
import spring.app.exceptions.NoNumbersEnteredException;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.User;
import spring.app.service.abstraction.StorageService;
import spring.app.service.abstraction.UserService;

import static spring.app.core.StepSelector.ADMIN_ADD_USER;
import static spring.app.core.StepSelector.ADMIN_SET_THEME_ADDED_USER;

/**
 * @author AkiraRokudo on 23.05.2020 in one of sun day
 */
@Component
public class AdminChangeAddedUserFullname extends Step {
    private final static Logger log = LoggerFactory.getLogger(AdminChangeAddedUserFullname.class);

    private final StorageService storageService;
    private final UserService userService;

    @Autowired
    public AdminChangeAddedUserFullname(StorageService storageService, UserService userService) {
        super("Введите новое имя и фамилию. Например: Иван Иванов", "");
        this.storageService = storageService;
        this.userService = userService;
    }

    @Override
    public void enter(BotContext context) {

    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException, NoNumbersEnteredException, NoDataEnteredException {
        String newFullName = context.getInput();
        Integer vkId = context.getVkId();
        String[] firstAndLastName = newFullName.split(" ");
        if (firstAndLastName.length == 2) {
            //Проверим, что есть только символы алфавитов
            boolean allSymbolAlphabet = newFullName.replaceAll(" ", "").chars().allMatch(Character::isLetter);
            if (allSymbolAlphabet) {
                Long addedUserId = Long.parseLong(storageService.getUserStorage(vkId, ADMIN_ADD_USER).get(0));
                User addedUser = userService.getUserById(addedUserId);
                String oldLastName = addedUser.getLastName();
                String oldFirstName = addedUser.getFirstName();
                String newFirstName = firstAndLastName[0];
                String newLastName = firstAndLastName[1];
                addedUser.setFirstName(newFirstName);
                addedUser.setLastName(newLastName);
                userService.updateUser(addedUser);
                log.info(
                        "Admin (vkId={}) изменил имя пользователя (vkId={}) с {} {} на {} {}",
                        context.getUser().getVkId(), addedUser.getVkId(), oldLastName, oldFirstName, newLastName, newFirstName
                );
                sendUserToNextStep(context, ADMIN_SET_THEME_ADDED_USER);
            } else {
                throw new ProcessInputException("В новом имени фамилии присутствуют не алфавитные символы");
            }
        } else {
            throw new ProcessInputException("Новое имя и фамилия должны состоять из 2 слов");
        }
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

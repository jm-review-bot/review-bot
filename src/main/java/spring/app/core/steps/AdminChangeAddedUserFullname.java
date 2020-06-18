package spring.app.core.steps;

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

    private final StorageService storageService;
    private final UserService userService;

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
                addedUser.setFirstName(firstAndLastName[0]);
                addedUser.setLastName(firstAndLastName[1]);
                userService.updateUser(addedUser);
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

package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.NoDataEnteredException;
import spring.app.exceptions.NoNumbersEnteredException;
import spring.app.exceptions.ProcessInputException;
import spring.app.service.abstraction.StorageService;

import java.util.ArrayList;
import java.util.List;

import static spring.app.core.StepSelector.ADMIN_CONFIRM_CHANGE_EDITED_USER_FULLNAME;
import static spring.app.core.StepSelector.ADMIN_INPUT_NEW_FULLNAME_EDITED_USER;

/**
 * @author AkiraRokudo on 27.05.2020 in one of sun day
 */
@Component
public class AdminInputNewFullnameEditedUser extends Step {

    private final StorageService storageService;

    public AdminInputNewFullnameEditedUser(StorageService storageService) {
        super("В ответ на данное сообщение отправьте имя и фамилию в формате {имя} {фамилия}", "");
        this.storageService = storageService;
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
            // Проверим, что есть только символы алфавитов
            boolean allSymbolAlphabet = newFullName.replaceAll(" ", "").chars().allMatch(Character::isLetter);
            if (allSymbolAlphabet) {
                List<String> newUserFullName = new ArrayList<>();
                newUserFullName.add(firstAndLastName[0]);
                newUserFullName.add(firstAndLastName[1]);
                storageService.updateUserStorage(vkId, ADMIN_INPUT_NEW_FULLNAME_EDITED_USER, newUserFullName);
                sendUserToNextStep(context, ADMIN_CONFIRM_CHANGE_EDITED_USER_FULLNAME);
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

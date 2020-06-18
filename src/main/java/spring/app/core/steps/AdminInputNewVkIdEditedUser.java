package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.NoDataEnteredException;
import spring.app.exceptions.NoNumbersEnteredException;
import spring.app.exceptions.ProcessInputException;
import spring.app.util.StringParser;
import spring.app.service.abstraction.*;

import java.util.Arrays;

import static spring.app.core.StepSelector.ADMIN_CONFIRM_CHANGE_EDITED_USER_VKID;
import static spring.app.core.StepSelector.ADMIN_INPUT_NEW_VKID_EDITED_USER;

/**
 * @author AkiraRokudo on 27.05.2020 in one of sun day
 */
@Component
public class AdminInputNewVkIdEditedUser extends Step {

    private final StorageService storageService;

    public AdminInputNewVkIdEditedUser(StorageService storageService) {
        super("В ответ на данное сообщение отправьте ссылку, или новый vkid", "");
        this.storageService = storageService;
    }

    @Override
    public void enter(BotContext context) {

    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException, NoNumbersEnteredException, NoDataEnteredException {
        String currentInput = context.getInput();
        String parsedInput = null;
        // если пришел не айдишник - значит предполагаем что пришла ссылку и пытаемся из неё получить айдишник
        if (!StringParser.isNumeric(currentInput)) {
            parsedInput = StringParser.toVkId(currentInput);
        } else {
            parsedInput = currentInput;
        }
        if (parsedInput != null) {
            storageService.updateUserStorage(context.getVkId(), ADMIN_INPUT_NEW_VKID_EDITED_USER, Arrays.asList(parsedInput));
            sendUserToNextStep(context, ADMIN_CONFIRM_CHANGE_EDITED_USER_VKID);
        } else {
            throw new ProcessInputException("Введенный текст не является vkid или ссылкой на профиль");
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

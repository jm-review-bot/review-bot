package spring.app.core.steps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.NoDataEnteredException;
import spring.app.exceptions.NoNumbersEnteredException;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.User;
import spring.app.service.abstraction.StorageService;
import spring.app.service.abstraction.UserService;

import java.util.Arrays;

import static spring.app.core.StepSelector.ADMIN_SEARCH;
import static spring.app.core.StepSelector.ADMIN_MENU;
import static spring.app.core.StepSelector.ADMIN_USERS_LIST;
import static spring.app.core.StepSelector.ADMIN_REMOVE_USER;
import static spring.app.core.StepSelector.ADMIN_EDIT_USER;
import static spring.app.util.Keyboards.YES_NO_KB;

/**
 * @author AkiraRokudo on 30.05.2020 in one of sun day
 */
@Component
public class AdminConfirmSearch extends Step {

    private final StorageService storageService;
    private final UserService userService;

    @Autowired
    public AdminConfirmSearch(StorageService storageService, UserService userService) {
        super("", YES_NO_KB);
        this.storageService = storageService;
        this.userService = userService;
    }

    @Override
    public void enter(BotContext context) {
        //Уточняем что это тот юзер, и выводим 2 кнопки - да и нет.

    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException, NoNumbersEnteredException, NoDataEnteredException {
        String input = context.getInput();
        Integer vkId = context.getVkId();
        if ("Нет".equals(input)) {
            storageService.removeUserStorage(vkId, ADMIN_SEARCH);
            sendUserToNextStep(context, ADMIN_SEARCH);
        } else if ("Да".equals(input)) {
            String mode = storageService.getUserStorage(vkId, ADMIN_MENU).get(0);
            String findingUserId = storageService.getUserStorage(vkId, ADMIN_SEARCH).get(0);
            storageService.updateUserStorage(vkId, ADMIN_USERS_LIST, Arrays.asList(findingUserId));
            //определим куда мы пойдем с нашим юзером
            if ("delete".equals(mode)) {
                sendUserToNextStep(context, ADMIN_REMOVE_USER);
            } else if ("edit".equals(mode)) {
                sendUserToNextStep(context, ADMIN_EDIT_USER);
            } else {
                throw new ProcessInputException("Произошла ошибка при обработке ответа - вернитесь на предыдущий шаг выбрав вариант 'нет'");
            }
            //сюда мы не вернемся, так что очищаемся
            storageService.removeUserStorage(context.getVkId(), ADMIN_SEARCH);
        } else {
            throw new ProcessInputException("Введена неверная команда...");
        }
    }

    @Override
    public String getDynamicText(BotContext context) {
        String userIdString = storageService.getUserStorage(context.getVkId(), ADMIN_SEARCH).get(0);
        User user = userService.getUserById(Long.parseLong(userIdString));
        String text = String.format("Найден пользователь %s %s (%s). Это нужный пользователь?", user.getFirstName(), user.getLastName(), user.getVkId());
        return text;
    }

    @Override
    public String getDynamicKeyboard(BotContext context) {
        return "";
    }
}

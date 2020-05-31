package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.User;
import spring.app.service.abstraction.StorageService;
import spring.app.service.abstraction.UserService;
import spring.app.util.StringParser;

import java.util.Arrays;

import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.BACK_KB;

/**
 * @author AkiraRokudo on 30.05.2020 in one of sun day
 */
@Component
public class AdminSearch extends Step {

    //TODO:шаг AdminAddUser - такой же алгоритм, надо бы оптимизировать.

    @Override
    public void enter(BotContext context) {
        text = "Введи ссылку на страницу пользователя.\n";
        keyboard = BACK_KB;
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException {
        UserService userService = context.getUserService();
        StorageService storageService = context.getStorageService();
        Integer vkId = context.getVkId();
        String currentInput = context.getInput();

        String parsedInput = StringParser.toVkId(currentInput);
        // также он  может прислать команду отмены
        String wordInput = StringParser.toWordsArray(currentInput)[0];
        if (wordInput.equals("назад")) {
            storageService.removeUserStorage(vkId, ADMIN_USERS_LIST);
            nextStep = ADMIN_USERS_LIST;
        } else if (parsedInput != null) {
            // мы ожидаем от него ссылки на профиль   юзера
            if (userService.isExistByVkId(Integer.parseInt(parsedInput))) {
                User findingUser = userService.getByVkId(Integer.parseInt(parsedInput));
                storageService.updateUserStorage(vkId, ADMIN_SEARCH, Arrays.asList(Long.toString(findingUser.getId())));
                nextStep = ADMIN_CONFIRM_SEARCH;
            } else {
                throw new ProcessInputException("Пользователь не найден в базе.\n");
            }
        } else {
            throw new ProcessInputException("Некорректная ссылка.\n");
        }
    }
}


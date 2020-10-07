package spring.app.core.steps;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.IncorrectVkIdsException;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.User;
import spring.app.service.abstraction.StorageService;
import spring.app.service.abstraction.UserService;
import spring.app.service.abstraction.VkService;
import spring.app.util.StringParser;

import java.util.Arrays;

import static spring.app.core.StepSelector.ADMIN_USERS_LIST;
import static spring.app.core.StepSelector.ADMIN_SEARCH;
import static spring.app.core.StepSelector.ADMIN_CONFIRM_SEARCH;
import static spring.app.util.Keyboards.DEF_BACK_KB;

/**
 * @author AkiraRokudo on 30.05.2020 in one of sun day
 */
@Component
public class AdminSearch extends Step {

    //TODO:шаг AdminAddUser - такой же алгоритм, надо бы оптимизировать.
    private final UserService userService;
    private final StorageService storageService;
    private final VkService vkService;

    @Autowired
    public AdminSearch(UserService userService, StorageService storageService, VkService vkService) {
        super("Введи ссылку на страницу пользователя.\n", DEF_BACK_KB);
        this.userService = userService;
        this.storageService = storageService;
        this.vkService = vkService;
    }

    @Override
    public void enter(BotContext context) {
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException {
        Integer vkId = context.getVkId();
        String currentInput = context.getInput();
        String parsedInput = StringParser.toVkId(currentInput);
        // также он  может прислать команду отмены
        String wordInput = StringParser.toWordsArray(currentInput)[0];
        if (wordInput.equals("назад")) {
            storageService.removeUserStorage(vkId, ADMIN_USERS_LIST);
            sendUserToNextStep(context, ADMIN_USERS_LIST);
        } else if (parsedInput != null) {
            // мы ожидаем от него ссылки на профиль юзера
            try {
                // получем юзера на основе запроса в VK
                User searchedUser = vkService.newUserFromVk(parsedInput);
                Integer searchedUserVkId = searchedUser.getVkId();
                if (userService.isExistByVkId(searchedUser.getVkId())) {
                    // получем юзера на основе запроса в БД
                    User findingUser = userService.getByVkId(searchedUserVkId).get();
                    storageService.updateUserStorage(vkId, ADMIN_SEARCH, Arrays.asList(Long.toString(findingUser.getId())));
                    sendUserToNextStep(context, ADMIN_CONFIRM_SEARCH);
                } else {
                    throw new ProcessInputException("Пользователь не найден в базе.\n");
                }
            } catch (ClientException | ApiException | IncorrectVkIdsException e) {
                throw new ProcessInputException("Некорректная ссылка.\n");
            }
        } else {
            throw new ProcessInputException("Введены некорректные данные.\n");
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


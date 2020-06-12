package spring.app.core.steps;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.IncorrectVkIdsException;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.Role;
import spring.app.model.User;
import spring.app.service.abstraction.StorageService;
import spring.app.service.abstraction.UserService;
import spring.app.service.abstraction.VkService;
import spring.app.util.StringParser;

import java.util.Arrays;

import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.DEF_BACK_KB;

@Component
public class AdminAddUser extends Step {

    public AdminAddUser() {
        super("Введите ссылку на профиль нового пользователя.\n", DEF_BACK_KB);
    }

    @Override
    public void enter(BotContext context) {
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException {
        VkService vkService = context.getVkService();
        UserService userService = context.getUserService();
        StorageService storageService = context.getStorageService();
        Role userRole = context.getRoleService().getRoleByName("USER");
        Integer vkId = context.getVkId();
        String currentInput = context.getInput();

        String parsedInput = StringParser.toVkId(currentInput);
        // также он  может прислать команду отмены
        String wordInput = StringParser.toWordsArray(currentInput)[0];
        if (wordInput.equals("назад")
                || wordInput.equals("/admin")) {
            storageService.removeUserStorage(vkId, ADMIN_ADD_USER);//т.к. мы на любом последующем шаге все равно придем в этот шаг, то очистку проводим тут.
            sendUserToNextStep(context, ADMIN_MENU);
        } else if (wordInput.equals("/start")) {
            storageService.removeUserStorage(vkId, ADMIN_ADD_USER);
            sendUserToNextStep(context, START);
        } else if (parsedInput != null) {
            // мы ожидаем от него ссылки на профиль добавляемого  юзера
            try {
                // получем юзера на основе запроса в VK
                User addedUser = vkService.newUserFromVk(parsedInput);
                //проверим, что у нас нет такого юзера
                if (!userService.isExistByVkId(addedUser.getVkId())) {
                    // теперь можно начать формировать ответ
                    StringBuilder addedUserText = new StringBuilder("Пользователь ");
                    // сэтим юзерам роль ЮЗЕР, добавляем в базу и формируем строку с оветом
                    addedUser.setRole(userRole);
                    userService.addUser(addedUser);
                    addedUserText
                            .append(addedUser.getFirstName())
                            .append(" ")
                            .append(addedUser.getLastName())
                            .append(" (https://vk.com/id")
                            .append(addedUser.getVkId())
                            .append(") \n Был успешно добавлен в базу. Оставить имя фамилию без изменений?\n");
                    //подготавливаем почву для следующих шагов
                    storageService.updateUserStorage
                            (vkId, ADMIN_ADD_USER, Arrays.asList(Long.toString(addedUser.getId())));
                    storageService.updateUserStorage
                            (vkId, ADMIN_PROPOSAL_CHANGE_FULLNAME_ADDED_USER, Arrays.asList
                                    (addedUserText.toString()));
                    sendUserToNextStep(context, ADMIN_PROPOSAL_CHANGE_FULLNAME_ADDED_USER);
                } else {
                    throw new ProcessInputException("Пользователь уже в базе.\n");
                }
            } catch (ClientException | ApiException | IncorrectVkIdsException e) {
                throw new ProcessInputException("Введены неверные данные. Такой пользователь не найден...");
            }
        } else {
            throw new ProcessInputException("Введена некрректная ссылка.");
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

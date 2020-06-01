package spring.app.core.steps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.List;

import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.BACK_KB;

@Component
public class AdminAddUser extends Step {
    private final static Logger log = LoggerFactory.getLogger(StringParser.class);

    @Override
    public void enter(BotContext context) {
        text = "Введите ссылку на профиль нового пользователя.\n";
        keyboard = BACK_KB;
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
            nextStep = ADMIN_MENU;
        } else if (wordInput.equals("/start")) {
            storageService.removeUserStorage(vkId, ADMIN_ADD_USER);
            nextStep = START;
        } else if(parsedInput != null){
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
                    log.debug("\tlog-message об операции пользователя над экземпляром(ами) сущности:\n" +
                            "Администратор "+context.getUser().getFirstName()+" "+context.getUser().getLastName()+" [id - "+context.getUser().getVkId()+"] добавил пользователя в базу.\n" +
                            "А именно, он добавил следующего пользователя:\n" +
                            addedUser.getFirstName() + " " + addedUser.getLastName() + " (https://vk.com/id" + addedUser.getVkId()+")");
                    //подготавливаем почву для следующих шагов
                    storageService.updateUserStorage(vkId, ADMIN_ADD_USER, Arrays.asList(Long.toString(addedUser.getId())));
                    storageService.updateUserStorage(vkId, ADMIN_PROPOSAL_CHANGE_FULLNAME_ADDED_USER, Arrays.asList(addedUserText.toString()));
                    nextStep = ADMIN_PROPOSAL_CHANGE_FULLNAME_ADDED_USER;
                } else {
                    throw new ProcessInputException("Пользователь уже в базе.\n");
                }
            } catch (ClientException | ApiException | IncorrectVkIdsException e) {
                throw new ProcessInputException("Введены неверные данные. Такой пользователь не найден...");
            }
        }

    }
}

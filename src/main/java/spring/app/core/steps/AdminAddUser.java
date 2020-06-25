package spring.app.core.steps;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.IncorrectVkIdsException;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.Role;
import spring.app.model.User;
import spring.app.service.abstraction.RoleService;
import spring.app.service.abstraction.StorageService;
import spring.app.service.abstraction.UserService;
import spring.app.service.abstraction.VkService;
import spring.app.util.StringParser;

import java.util.Arrays;

import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.DEF_BACK_KB;

@Component
public class AdminAddUser extends Step {
    private final static Logger log = LoggerFactory.getLogger(AdminAddUser.class);

    private final StorageService storageService;
    private final UserService userService;
    private final VkService vkService;
    private final RoleService roleService;

    public AdminAddUser(StorageService storageService, UserService userService,
                        VkService vkService, RoleService roleService) {
        super("Введите ссылку на профиль нового пользователя.\n", DEF_BACK_KB);
        this.storageService = storageService;
        this.userService = userService;
        this.vkService = vkService;
        this.roleService = roleService;
    }

    @Override
    public void enter(BotContext context) {
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException {

        Role userRole = roleService.getRoleByName("USER");
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
                    log.info(
                            "Admin (vkId={}) добавил пользователя (vkId={})",
                            context.getUser().getVkId(), addedUser.getVkId()
                    );
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

package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.NoDataEnteredException;
import spring.app.exceptions.NoNumbersEnteredException;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.User;
import spring.app.service.abstraction.StorageService;
import spring.app.util.StringParser;

import java.util.Arrays;

import static spring.app.core.StepSelector.ADMIN_REMOVE_USER;
import static spring.app.core.StepSelector.ADMIN_USERS_LIST;
import static spring.app.util.Keyboards.DEF_BACK_KB;
import static spring.app.util.Keyboards.YES_NO_KB;

@Component
public class AdminRemoveUser extends Step {

    public AdminRemoveUser() {
        super("", "");
    }

    @Override
    public void enter(BotContext context) {
        Integer vkId = context.getVkId();
        StorageService storageService = context.getStorageService();
        String selectedUserId = storageService.getUserStorage(vkId, ADMIN_USERS_LIST).get(0);
        User selectedUser = context.getUserService().getUserById(Long.parseLong(selectedUserId));

        String userInfo = new StringBuilder().append(selectedUser.getFirstName()).append(" ").append(selectedUser.getLastName())
                .append(" (https://vk.com/id").append(selectedUser.getVkId()).append(").\n").toString();
        storageService.updateUserStorage(vkId, ADMIN_REMOVE_USER, Arrays.asList(userInfo));
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException, NoNumbersEnteredException, NoDataEnteredException {
        String currentInput = context.getInput();
        StorageService storageService = context.getStorageService();
        Integer vkId = context.getVkId();
        String wordInput = StringParser.toWordsArray(currentInput)[0];

        if (wordInput.equals("да")) {
            String selectedUserId = storageService.getUserStorage(vkId, ADMIN_USERS_LIST).get(0);
            storageService.clearUsersOfStorage(context.getUserService().getUserById(Long.parseLong(selectedUserId)).getVkId());
            context.getUserService()
                    .deleteUserById(Long.parseLong(selectedUserId));
            //перекинем инфу с удаления на список юзеров. Для унификации. Данная инфа будет использована при возвращении
            String userInfo = storageService.getUserStorage(vkId, ADMIN_REMOVE_USER).get(0);
            storageService.removeUserStorage(vkId, ADMIN_REMOVE_USER);
            storageService.updateUserStorage(vkId, ADMIN_USERS_LIST, Arrays.asList(userInfo));
            sendUserToNextStep(context, ADMIN_USERS_LIST);
        } else if (wordInput.equals("нет") || wordInput.equals("назад")) {
            //Возвращаемся назад
            storageService.removeUserStorage(vkId, ADMIN_REMOVE_USER);
            //Очищаем список ролей, раз не хотим ничего сообщать в том шаге
            storageService.removeUserStorage(vkId, ADMIN_USERS_LIST);
            sendUserToNextStep(context, ADMIN_USERS_LIST);
        } else {
            throw new NoNumbersEnteredException("Введена неверная команда. Нажмите \"Да\" для удаления или \"Нет\" для возврата.");
        }
    }

    @Override
    public String getDynamicText(BotContext context) {
        StorageService storageService = context.getStorageService();
        String userInfo = storageService.getUserStorage(context.getVkId(), ADMIN_REMOVE_USER).get(0);
        StringBuilder confirmMessage = new StringBuilder("Студент ");
        if (userInfo != null) {
            confirmMessage.append(userInfo)
                    .append("Вы точно хотите удалить данного студента? (Да/Нет)\n");
        } else {
            confirmMessage.append("не выбран.");
        }
        return confirmMessage.toString();
    }

    @Override
    public String getDynamicKeyboard(BotContext context) {
        StorageService storageService = context.getStorageService();

        return storageService.getUserStorage(context.getVkId(), ADMIN_REMOVE_USER).get(0) != null ? YES_NO_KB : DEF_BACK_KB;
    }
}

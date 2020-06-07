package spring.app.core.steps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.NoNumbersEnteredException;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.User;
import spring.app.service.abstraction.StorageService;
import spring.app.util.StringParser;

import java.util.*;

import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.*;

@Component
public class AdminRemoveUser extends Step {
    private final static Logger log = LoggerFactory.getLogger(AdminRemoveUser.class);

    @Override
    public void enter(BotContext context) {
        Integer vkId = context.getVkId();
        StorageService storageService = context.getStorageService();
        String selectedUserId = storageService.getUserStorage(vkId, ADMIN_USERS_LIST).get(0);
        User selectedUser = context.getUserService().getUserById(Long.parseLong(selectedUserId));
        String userInfo = new StringBuilder().append(selectedUser.getFirstName()).append(" ").append(selectedUser.getLastName())
                .append(" (https://vk.com/id").append(selectedUser.getVkId()).append(").\n").toString();
        StringBuilder confirmMessage = new StringBuilder("Студент ");
        confirmMessage.append(userInfo)
                .append("Вы точно хотите удалить данного студента?\n")
                .append("[1] подтвердить\n")
                .append("[2] отменить\n");
        storageService.updateUserStorage(vkId, ADMIN_REMOVE_USER, Arrays.asList(userInfo));
        text = confirmMessage.toString();
        keyboard = NO_KB;
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException, NoNumbersEnteredException {
        String currentInput = context.getInput();
        StorageService storageService = context.getStorageService();
        Integer vkId = context.getVkId();

        String wordInput = StringParser.toWordsArray(currentInput)[0];
        if (StringParser.isNumeric(wordInput)) {
            Integer selectedNumber = Integer.parseInt(wordInput);
            switch (selectedNumber) {
                case 1:
                    //Удаляем.
                    String selectedUserId = storageService.getUserStorage(vkId, ADMIN_USERS_LIST).get(0);
                    User user = context.getUserService().getUserById(Long.parseLong(selectedUserId));
                    String str = new StringBuilder(user.getFirstName()).append(" ").append(user.getLastName()).append(" (https://vk.com/id").append(user.getVkId()).toString();
                    storageService.clearUsersOfStorage(user.getVkId());
                    context.getUserService()
                            .deleteUserById(Long.parseLong(selectedUserId));
                    log.debug("\tlog-message об операции пользователя над экземпляром(ами) сущности:\nАдминистратор {} {} [vkId - {}] удалил пользователя из базы.\nА именно, он удалил следующего пользователя:\n{}", context.getUser().getFirstName(), context.getUser().getLastName(), context.getUser().getId(), str);
                    //перекинем инфу с удаления на список юзеров. Для унификации. Данная инфа будет использована при возвращении
                    String userInfo = storageService.getUserStorage(vkId, ADMIN_REMOVE_USER).get(0);
                    storageService.removeUserStorage(vkId, ADMIN_REMOVE_USER);
                    storageService.updateUserStorage(vkId, ADMIN_USERS_LIST, Arrays.asList(userInfo));
                    nextStep = ADMIN_USERS_LIST;
                    break;
                case 2:
                    //Возвращаемся назад
                    storageService.removeUserStorage(vkId, ADMIN_REMOVE_USER);
                    //Очищаем список ролей, раз не хотим ничего сообщать в том шаге
                    storageService.removeUserStorage(vkId, ADMIN_USERS_LIST);
                    nextStep = ADMIN_USERS_LIST;
                    break;
                default:
                    throw new ProcessInputException("Введено неподходящее число");
            }
        } else {
            throw new NoNumbersEnteredException("Введена неверная команда. Введите 1 для удаления или 2 для возврата");
        }
    }
}

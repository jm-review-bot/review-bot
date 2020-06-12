package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.NoDataEnteredException;
import spring.app.exceptions.NoNumbersEnteredException;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.User;
import spring.app.service.abstraction.StorageService;
import spring.app.util.StringParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.*;

/**
 * @author AkiraRokudo on 19.05.2020 in one of sun day
 */
@Component
public class AdminUserList extends Step {

    public AdminUserList() {
        super("", SEARCH_OR_BACK);
    }

    @Override
    public void enter(BotContext context) {
        //сохраним список юзеров для дальнейшего использования
        List<String> usersIdList = new ArrayList<>();
        context.getUserService().getAllUsers().stream()
                .sorted(Comparator.comparing(User::getLastName))
                .forEach(user ->
                    // сохраняем ID юзера в лист
                    usersIdList.add(user.getId().toString()));
        context.getStorageService().updateUserStorage(context.getVkId(), ADMIN_USERS_LIST, usersIdList);
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException {
        String currentInput = context.getInput();
        StorageService storageService = context.getStorageService();
        Integer vkId = context.getVkId();
        String wordInput = StringParser.toWordsArray(currentInput)[0];
        if (wordInput.equals("назад")) {
            storageService.removeUserStorage(vkId, ADMIN_USERS_LIST);
            storageService.removeUserStorage(vkId, ADMIN_MENU);
            sendUserToNextStep(context, ADMIN_MENU);
        } else if (wordInput.equals("поиск")) {
            sendUserToNextStep(context, ADMIN_SEARCH);
        } else if (StringParser.isNumeric(wordInput)) {
            Integer selectedNumber = Integer.parseInt(wordInput);
            //значит мы выбрали пользователя. Обновим коллекцию
            List<String> users = storageService.getUserStorage(vkId, ADMIN_USERS_LIST);
            if (selectedNumber <= 0 || selectedNumber > users.size()) {
                throw new ProcessInputException("Введено неподходящее число");
            }
            String selectedUserId = users.get(selectedNumber - 1);
            storageService.updateUserStorage(vkId, ADMIN_USERS_LIST, Arrays.asList(selectedUserId));
            String mode = storageService.getUserStorage(vkId, ADMIN_MENU).get(0);
            if ("delete".equals(mode)) {
                sendUserToNextStep(context, ADMIN_REMOVE_USER);
            } else {
                sendUserToNextStep(context, ADMIN_EDIT_USER);
            }
        } else {
            throw new ProcessInputException("Введена неверная команда...");
        }
    }

    @Override
    public String getDynamicText(BotContext context) {
        String text = "";
        Integer vkId = context.getVkId();
        StorageService storageService = context.getStorageService();
        String mode = null;
        if (storageService.getUserStorage(vkId, ADMIN_MENU) != null) {
            mode = storageService.getUserStorage(vkId, ADMIN_MENU).get(0);
        }
        String afterModificationMessage = null;
        //Блок для сообщения после изменения\удаления.
        if (storageService.getUserStorage(vkId, ADMIN_USERS_LIST) != null) {
            String userInfo = storageService.getUserStorage(vkId, ADMIN_USERS_LIST).get(0);
            if ("delete".equals(mode)) {
                afterModificationMessage = new StringBuilder("Студент ").append(userInfo).append("Был успешно удален из базы\n\n").toString();
            } else if ("edit".equals(mode)) {
                afterModificationMessage = userInfo;
            }
            storageService.removeUserStorage(vkId, ADMIN_USERS_LIST);
        }

        StringBuilder userList = new StringBuilder();
        if ("delete".equals(mode)) {
            userList.append("Список всех студентов. Выберете студента для удаления.\n");
        } else {
            userList.append("Выберите пользователя для изменения:\n");
        }
        // создаем лист строк, куда будем складывать Id Юзеров, которых мы показываем админу в боте
        List<String> usersIdList = storageService.getUserStorage(vkId, ADMIN_USERS_LIST);
        if(usersIdList != null) {
            //TODO: работать с листом юзеров, не дергая каждый раз БД
            final int[] i = {1};
            usersIdList.stream().forEach(userId -> {
                User user = context.getUserService().getUserById(Long.parseLong(userId));
                userList.append("[").append(i[0]++).append("] ")
                        .append(user.getFirstName())
                        .append(" ")
                        .append(user.getLastName())
                        .append(", https://vk.com/id")
                        .append(user.getVkId());
                userList.append("\n");
            });
        }
        userList.append("Для возврата в меню, введи \"назад\".\n\n Для поиска по ссылке нажмите кнопку 'Поиск'.\n");

        text = afterModificationMessage == null ? userList.toString() : afterModificationMessage + userList.toString();

        return text;
    }

    @Override
    public String getDynamicKeyboard(BotContext context) {
        return "";
    }

}

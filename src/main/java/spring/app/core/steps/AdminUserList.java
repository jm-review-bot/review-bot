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
import static spring.app.util.Keyboards.BACK_KB;

/**
 * @author AkiraRokudo on 19.05.2020 in one of sun day
 */
@Component
public class AdminUserList extends Step {

    @Override
    public void enter(BotContext context) {
        Integer vkId = context.getVkId();
        StorageService storageService = context.getStorageService();
        String mode = storageService.getUserStorage(vkId, ADMIN_MENU).get(0);
        String afterModificationMessage = null;
        //Блок для сообщения после изменения\удаления.
        if (storageService.getUserStorage(vkId, ADMIN_USERS_LIST) != null) {
            if ("delete".equals(mode)) {
                String userDeletedInfo = storageService.getUserStorage(vkId, ADMIN_USERS_LIST).get(0);
                afterModificationMessage = new StringBuilder("Студент ").append(userDeletedInfo).append("Был успешно удален из базы\n\n").toString();
            } else {
                //TODO:заглушка для редактирования
            }
            storageService.removeUserStorage(vkId, ADMIN_USERS_LIST);
        }

        StringBuilder userList = new StringBuilder();
        if ("delete".equals(mode)) {
            userList.append("Список всех студентов. Выберете студента для удаления.\n");
        } else {
            //TODO:заглушка для редактирования
        }
        // создаем лист строк, куда будем складывать Id Юзеров, которых мы показываем админу в боте
        List<String> users = new ArrayList<>();
        final int[] i = {1};
        context.getUserService().getAllUsers().stream()
                .sorted(Comparator.comparing(User::getLastName))
                .forEach(user -> {
                    userList.append("[").append(i[0]++).append("] ")
                            .append(user.getFirstName())
                            .append(" ")
                            .append(user.getLastName())
                            .append(", https://vk.com/id")
                            .append(user.getVkId());
//                    if (user.getRole().isAdmin()) {
//                        userList.append(" (админ)");
//                    }
                    userList.append("\n");
                    // сохраняем ID юзера в лист
                    users.add(user.getId().toString());
                });
        userList.append("Для возврата в меню, введи \"назад\".\n\n");
        storageService.updateUserStorage(vkId, ADMIN_USERS_LIST, users);
        text = afterModificationMessage == null ? userList.toString() : afterModificationMessage + userList.toString();

        keyboard = BACK_KB;
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException, NoNumbersEnteredException, NoDataEnteredException {
        String currentInput = context.getInput();
        StorageService storageService = context.getStorageService();
        Integer vkId = context.getVkId();

        String wordInput = StringParser.toWordsArray(currentInput)[0];
        if (wordInput.equals("назад")) {
            storageService.removeUserStorage(vkId, ADMIN_USERS_LIST);
            storageService.removeUserStorage(vkId, ADMIN_MENU);
            nextStep = ADMIN_MENU;
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
                nextStep = ADMIN_REMOVE_USER;
            } else {
                //TODO:Заглушка под редактирование
            }
        } else {
            throw new ProcessInputException("Введена неверная команда...");
        }
    }
}

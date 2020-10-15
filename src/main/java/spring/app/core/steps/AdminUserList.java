package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.dto.UserDto;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.User;
import spring.app.service.abstraction.StorageService;
import spring.app.service.abstraction.UserService;
import spring.app.util.StringParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.SEARCH_OR_BACK;

/**
 * @author AkiraRokudo on 19.05.2020 in one of sun day
 */
@Component
public class AdminUserList extends Step {

    private final StorageService storageService;
    private final UserService userService;

    public AdminUserList(StorageService storageService, UserService userService) {
        super("", SEARCH_OR_BACK);
        this.storageService = storageService;
        this.userService = userService;
    }

    @Override
    public void enter(BotContext context) {
        Integer vkId = context.getVkId();
        //определим режим работы - редактирование или удаление.
        String mode = null;
        if (storageService.getUserStorage(vkId, ADMIN_CHOOSE_ACTION_FOR_USER) != null) {
            mode = storageService.getUserStorage(vkId, ADMIN_CHOOSE_ACTION_FOR_USER).get(0);
        }
        //посмотрим, не попали ли мы сюда после редактирования\удаления
        String afterModificationMessage = null;
        List<String> usersInfoList = storageService.getUserStorage(vkId, ADMIN_USERS_LIST);
        if (usersInfoList != null) {
            String userInfo = storageService.getUserStorage(vkId, ADMIN_USERS_LIST).get(0);
            if ("delete".equals(mode)) {
                afterModificationMessage = new StringBuilder("Студент ").append(userInfo).append("Был успешно удален из базы\n\n").toString();
            } else if ("edit".equals(mode)) {
                afterModificationMessage = userInfo;
            }
        }
        //сохраним список юзеров для дальнейшего использования
        List<String> usersIdList = new ArrayList<>();
        userService.getAllUsers().stream()
                .sorted(Comparator.comparing(User::getLastName))
                .forEach(user ->
                        // сохраняем ID юзера в лист
                        usersIdList.add(user.getId().toString()));
        //добавим первым элементом префикс после изменения\удаления
        usersIdList.add(0, afterModificationMessage);
        storageService.updateUserStorage(vkId, ADMIN_USERS_LIST, usersIdList);
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException {
        String currentInput = context.getInput();
        Integer vkId = context.getVkId();
        String wordInput = StringParser.toWordsArray(currentInput)[0];
        if (wordInput.equals("назад")) {
            storageService.removeUserStorage(vkId, ADMIN_USERS_LIST);
            storageService.removeUserStorage(vkId, ADMIN_CHOOSE_ACTION_FOR_USER);
            sendUserToNextStep(context, ADMIN_CHOOSE_ACTION_FOR_USER);
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
            String mode = storageService.getUserStorage(vkId, ADMIN_CHOOSE_ACTION_FOR_USER).get(0);
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
        String mode = null;
        if (storageService.getUserStorage(vkId, ADMIN_CHOOSE_ACTION_FOR_USER) != null) {
            mode = storageService.getUserStorage(vkId, ADMIN_CHOOSE_ACTION_FOR_USER).get(0);
        }
        String afterModificationMessage = null;
        //Блок для сообщения после изменения\удаления.
        // создаем лист строк, куда будем складывать Id Юзеров, которых мы показываем админу в боте
        List<String> usersIdList = storageService.getUserStorage(vkId, ADMIN_USERS_LIST);
        //если мы в динамическом тексте первый раз после удаления - вытащим первый элемент из обработки и удалим его из коллекции айдишников
        if (!usersIdList.isEmpty() && !StringParser.isNumeric(usersIdList.get(0))) {
            afterModificationMessage = usersIdList.get(0);
            usersIdList.remove(0);
        }

        StringBuilder userListString = new StringBuilder();
        if ("delete".equals(mode)) {
            userListString.append("Список всех студентов. Выберете студента для удаления.\n");
        } else {
            userListString.append("Выберите пользователя для изменения:\n");
        }

        List<UserDto> userList;
        if (usersIdList != null) {
            List<Long> usersIdListLong = new ArrayList<>();
            usersIdList.forEach(userId -> {
                usersIdListLong.add(Long.parseLong(userId));
            });
            userList = userService.getUsersDtoByIds(usersIdListLong);
            final int[] i = {1};
            userList.forEach(user -> {
                userListString.append("[").append(i[0]++).append("] ")
                        .append(user.getFirstName())
                        .append(" ")
                        .append(user.getLastName());
                if(user.isDeleted()){
                    userListString.append("(УДАЛЕН)");
                }
                userListString.append(", https://vk.com/id")
                        .append(user.getVkId());
                userListString.append("\n");
            });
        }
        userListString.append("Для возврата в меню, введи \"назад\".\n\n Для поиска по ссылке нажмите кнопку 'Поиск'.\n");

        text = afterModificationMessage == null ? userListString.toString() : afterModificationMessage + userListString.toString();

        return text;
    }

    @Override
    public String getDynamicKeyboard(BotContext context) {
        return "";
    }
}

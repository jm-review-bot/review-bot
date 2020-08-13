package spring.app.core.steps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.NoDataEnteredException;
import spring.app.exceptions.NoNumbersEnteredException;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.User;
import spring.app.service.abstraction.StorageService;
import spring.app.service.abstraction.UserService;
import spring.app.util.StringParser;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.DEF_BACK_KB;

/**
 * Многоступенчатый шаг, для редактирования ревью
 * пользователей админом.
 *
 * @author AkiraRokudo on 13.05.2020 in one of sun day
 */
@Component
public class AdminEditReviewGetUserList extends Step {

    private final UserService userService;
    private final StorageService storageService;

    @Autowired
    public AdminEditReviewGetUserList(UserService userService, StorageService storageService) {
        super("", DEF_BACK_KB);
        this.userService = userService;
        this.storageService = storageService;
    }

    @Override
    public void enter(BotContext context) {
        List<String> userToReviewChange = new ArrayList<>();

        userService.getAllUsers().stream()
                .sorted(Comparator.comparing(User::getLastName))
                .forEach(user -> userToReviewChange.add(user.getId().toString()));

        storageService.updateUserStorage(context.getVkId(), ADMIN_EDIT_REVIEW_GET_USER_LIST, userToReviewChange);
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException, NoNumbersEnteredException, NoDataEnteredException {
        String currentInput = context.getInput();
        Integer vkId = context.getVkId();
        String wordInput = StringParser.toWordsArray(currentInput)[0];
        if (wordInput.equals("назад")) {
            //если назад - смотрим последний шаг,и откатываем один селект.
            storageService.removeUserStorage(vkId, ADMIN_EDIT_REVIEW_GET_USER_LIST);
            sendUserToNextStep(context, ADMIN_CHOOSE_ACTION_FOR_REVIEW);
        } else if (StringParser.isNumeric(wordInput)) {
            Integer selectedNumber = Integer.parseInt(wordInput);
            //значит мы выбрали пользователя. Обновим коллекцию
            List<String> users = storageService.getUserStorage(vkId, ADMIN_EDIT_REVIEW_GET_USER_LIST);
            if (selectedNumber <= 0 || selectedNumber > users.size()) {
                throw new ProcessInputException("Введено неподходящее число");
            }
            String selectedUserId = users.get(selectedNumber - 1);
            users.clear();
            users.add(selectedUserId);
            storageService.updateUserStorage(vkId, ADMIN_EDIT_REVIEW_GET_USER_LIST, users);
            sendUserToNextStep(context, ADMIN_EDIT_REVIEW_GET_THEME_LIST);
        } else {
            throw new ProcessInputException("Введена неверная команда...");
        }
    }


    @Override
    public String getDynamicText(BotContext context) {
        StringBuilder stringBuilder = new StringBuilder("Выберите пользователя, ревью по которому вы хотите редактировать\n");
        List<String> userToReviewChangeList = storageService.getUserStorage(context.getVkId(), ADMIN_EDIT_REVIEW_GET_USER_LIST);
        if (userToReviewChangeList != null) {
            final int[] userCounter = {1}; //обход финальности для лямбды
            userToReviewChangeList.stream().forEach(userId -> {
                User user = userService.getUserById(Long.parseLong(userId));
                stringBuilder.append("[").append(userCounter[0]++).append("] ")
                        .append(user.getLastName())
                        .append(" ")
                        .append(user.getFirstName())
                        .append(", https://vk.com/id")
                        .append(user.getVkId());
                if (user.getRole().isAdmin()) {
                    stringBuilder.append(" (админ)");
                }
                stringBuilder.append("\n");
            });
        }
        return stringBuilder.toString();
    }

    @Override
    public String getDynamicKeyboard(BotContext context) {
        return "";
    }
}

package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.core.StepSelector;
import spring.app.exceptions.NoDataEnteredException;
import spring.app.exceptions.NoNumbersEnteredException;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.Question;
import spring.app.model.StudentReview;
import spring.app.model.Theme;
import spring.app.model.User;
import spring.app.service.abstraction.StorageService;
import spring.app.util.StringParser;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.BACK_AND_EDIT_STATUS_KB;
import static spring.app.util.Keyboards.BACK_KB;

import spring.app.util.StringParser;

/**
 * Многоступенчатый шаг, для редактирования ревью
 * пользователей админом.
 *
 * @author AkiraRokudo on 13.05.2020 in one of sun day
 */
@Component
public class AdminEditReviewGetUserList extends Step {

    @Override
    public void enter(BotContext context) {
        Integer vkId = context.getVkId();
        StorageService storageService = context.getStorageService();
        StringBuilder stringBuilder = new StringBuilder("Выберите пользователя, ревью по которому вы хотите редактировать\n");
        List<String> userToReviewChange = new ArrayList<>();
        final int[] userCounter = {1}; //обход финальности для лямбды
        context.getUserService()
                .getAllUsers().stream()
                .sorted(Comparator.comparing(User::getLastName))
                .forEach(user -> {
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
                    // сохраняем ID юзера в лист
                    userToReviewChange.add(user.getId().toString());
                });
        text = stringBuilder.toString();
        keyboard = BACK_KB;
        storageService.updateUserStorage(vkId, ADMIN_EDIT_REVIEW_GET_USER_LIST, userToReviewChange);
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException, NoNumbersEnteredException, NoDataEnteredException {
        String currentInput = context.getInput();
        StorageService storageService = context.getStorageService();
        Integer vkId = context.getVkId();
        String wordInput = StringParser.toWordsArray(currentInput)[0];
        if (wordInput.equals("назад")) {
            //если назад - смотрим последний шаг,и откатываем один селект.
            storageService.removeUserStorage(vkId, ADMIN_EDIT_REVIEW_GET_USER_LIST);
            nextStep = ADMIN_MENU;
        } else if (StringParser.isNumeric(wordInput)) {
            Integer selectedNumber = Integer.parseInt(wordInput);
            //значит мы выбрали пользователя. Обновим коллекцию
            List<String> users = storageService.getUserStorage(vkId, ADMIN_EDIT_REVIEW_GET_USER_LIST);
            if (selectedNumber <= 0 || selectedNumber > users.size()) {
                throw new ProcessInputException("Введено неподходящее число");
            }
            String selectedUserId = users.get(selectedNumber-1);
            users.clear();
            users.add(selectedUserId);
            storageService.updateUserStorage(vkId, ADMIN_EDIT_REVIEW_GET_USER_LIST, users);
            nextStep = ADMIN_EDIT_REVIEW_GET_THEME_LIST;
        } else {
            throw new ProcessInputException("Введена неверная команда...");
        }
    }

}

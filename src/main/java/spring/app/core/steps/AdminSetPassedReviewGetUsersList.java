package spring.app.core.steps;

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
import java.util.Arrays;
import java.util.List;

import static spring.app.core.StepSelector.ADMIN_SET_PASSED_REVIEW_GET_USERS_LIST;
import static spring.app.core.StepSelector.ADMIN_SET_PASSED_REVIEW_GET_THEMES_STATUS;
import static spring.app.core.StepSelector.ADMIN_CHOOSE_ACTION_FOR_REVIEW;
import static spring.app.util.Keyboards.DEF_BACK_KB;

@Component
public class AdminSetPassedReviewGetUsersList extends Step {

    private final StorageService storageService;
    private final UserService userService;

    public AdminSetPassedReviewGetUsersList(StorageService storageService,
                                            UserService userService) {
        super("", DEF_BACK_KB);
        this.storageService = storageService;
        this.userService = userService;
    }

    @Override
    public void enter(BotContext context) {

    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException, NoNumbersEnteredException, NoDataEnteredException {
        String command = context.getInput();
        if (StringParser.isNumeric(command)) {
            int userNumber = Integer.parseInt(command);
            List<String> idList = storageService.getUserStorage(context.getVkId(), ADMIN_SET_PASSED_REVIEW_GET_USERS_LIST); // При выводе сообщения пользователю в текущем шаге были сохранены ID выведенных в сообщении пользователей
            if (userNumber < 1 || userNumber > idList.size()) {
                throw new ProcessInputException("Введите подходящее число...");
            }
            User user = userService.getUserById(Long.parseLong(idList.get(userNumber - 1)));
            if (user == null) { // Проверка на случай, что пользователя могли уже удалить из БД к текущему моменту.
                throw new ProcessInputException("Возможно, пользователь уже удален из БД. Вернитесь назад и попробуйте еще раз.");
            }
            storageService.updateUserStorage(context.getVkId(), ADMIN_SET_PASSED_REVIEW_GET_USERS_LIST, Arrays.asList(user.getId().toString()));
            sendUserToNextStep(context, ADMIN_SET_PASSED_REVIEW_GET_THEMES_STATUS);
        } else if (command.equals("Назад")) {
            storageService.removeUserStorage(context.getVkId(), ADMIN_SET_PASSED_REVIEW_GET_USERS_LIST);
            sendUserToNextStep(context, ADMIN_CHOOSE_ACTION_FOR_REVIEW);
        } else {
            throw new ProcessInputException("Введена неверная команда...");
        }
    }

    /* Этот метод выводит сообщение, содержащие список всех существующих пользователей в БД, и админу необходимо выбрать одного.
     * Ввиду того, что состояние БД может измениться в процессе выбора, в текущий шаг сохраняется информация обо всех выведенных
     * админу пользователей в виде List<String>, содержащего в себе ID пользователей в том же порядке,
     * в котором они представлены в информационном сообщении. Это также поможет в следующих шагах избежать NPE в случае,
     * когда админ выбрал пользователя, а его в последствии удалили из БД. */
    @Override
    public String getDynamicText(BotContext context) {
        StringBuilder infoMessage = new StringBuilder("Выберите пользователя, ревью у которого вы хотите сделать пройденным:\n\n");
        List<User> allUsers = userService.getAllUsers();
        List<String> idList = new ArrayList<>();
        for (int i = 0; i < allUsers.size(); i++) {
            User user = allUsers.get(i);
            infoMessage
                    .append("[")
                    .append(i + 1)
                    .append("] ")
                    .append(user.getFirstName())
                    .append(" ")
                    .append(user.getLastName())
                    .append("\n");
            idList.add(user.getId().toString());
        }
        storageService.updateUserStorage(context.getVkId(), ADMIN_SET_PASSED_REVIEW_GET_USERS_LIST, idList);
        return infoMessage.toString();
    }

    @Override
    public String getDynamicKeyboard(BotContext context) {
        return "";
    }
}

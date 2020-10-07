package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.NoDataEnteredException;
import spring.app.exceptions.NoNumbersEnteredException;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.Theme;
import spring.app.model.User;
import spring.app.service.abstraction.StorageService;
import spring.app.service.abstraction.ThemeService;
import spring.app.service.abstraction.UserService;
import spring.app.util.StringParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static spring.app.core.StepSelector.EXAMINER_USERS_LIST_FROM_DB;
import static spring.app.core.StepSelector.EXAMINER_GET_INFO_LAST_REVIEW;
import static spring.app.core.StepSelector.EXAMINER_FREE_THEMES_LIST;
import static spring.app.core.StepSelector.EXAMINER_CHOOSE_METHOD_TO_ADD_STUDENT;
import static spring.app.util.Keyboards.DEF_BACK_KB;

@Component
public class ExaminerUsersListFromDB extends Step {

    private final StorageService storageService;
    private final UserService userService;
    private final ThemeService themeService;

    public ExaminerUsersListFromDB(StorageService storageService,
                                   UserService userService,
                                   ThemeService themeService) {
        super("", DEF_BACK_KB);
        this.userService = userService;
        this.storageService = storageService;
        this.themeService = themeService;
    }

    @Override
    public void enter(BotContext context) {

    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException, NoNumbersEnteredException, NoDataEnteredException {
        String command = context.getInput();
        Integer examinerVkId = context.getVkId();

        // Обрабатываются команды пользователя
        if (StringParser.isNumeric(command)) {

            // Из текущего шага извлекается список ID пользователей
            List<String> usersIds = storageService.getUserStorage(examinerVkId, EXAMINER_USERS_LIST_FROM_DB);

            // Проверяется корректность ввода пользователем и из списка извлекается ID выбранного пользователя
            Integer studentNumber = Integer.parseInt(command);
            if (studentNumber <= 0 || studentNumber > usersIds.size()) {
                throw new ProcessInputException("Введено неподходящее число");
            }
            Long studentId = Long.parseLong(usersIds.get(studentNumber - 1));

            // В текущий шаг сохраняется ID студента
            sendUserToNextStep(context, EXAMINER_GET_INFO_LAST_REVIEW);
            storageService.updateUserStorage(examinerVkId, EXAMINER_USERS_LIST_FROM_DB, Arrays.asList(studentId.toString()));

        } else if (command.equalsIgnoreCase("назад")) {
            sendUserToNextStep(context, EXAMINER_CHOOSE_METHOD_TO_ADD_STUDENT);
            storageService.removeUserStorage(examinerVkId, EXAMINER_USERS_LIST_FROM_DB);
        } else {
            throw new ProcessInputException("Введена неверная команда...");
        }
    }

    @Override
    public String getDynamicText(BotContext context) {
        Integer vkId = context.getVkId();
        List<User> allUsers = userService.getAllUsers();

        // Из шага EXAMINER_FREE_THEMES_LIST извлекается ID темы
        Long freeThemeId = Long.parseLong(storageService.getUserStorage(vkId, EXAMINER_FREE_THEMES_LIST).get(0));
        Theme freeTheme = themeService.getThemeById(freeThemeId);

        /* Бот выводит сообщение со списком всех пользователей из БД и формирует список с их ID,
         * чтобы отправить их в хранилище текущего шага */
        List<String> usersIds = new ArrayList<>();
        StringBuilder infoMessage = new StringBuilder();
        infoMessage.append(
                String.format("Защита темы %s.\nВыберите студента из списка:\n",
                        freeTheme.getTitle())
        );
        for (int i = 0; i < allUsers.size(); i++) {
            User user = allUsers.get(i);
            infoMessage.append(
                    String.format("[%d] %s %s (%d)\n",
                            i + 1,
                            user.getFirstName(),
                            user.getLastName(),
                            user.getVkId())
            );
            usersIds.add(user.getId().toString());
        }
        storageService.updateUserStorage(vkId, EXAMINER_USERS_LIST_FROM_DB, usersIds);
        return infoMessage.toString();
    }

    @Override
    public String getDynamicKeyboard(BotContext context) {
        return "";
    }
}

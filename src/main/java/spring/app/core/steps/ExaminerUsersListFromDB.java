package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.NoDataEnteredException;
import spring.app.exceptions.NoNumbersEnteredException;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.Theme;
import spring.app.model.User;
import spring.app.service.abstraction.*;
import spring.app.util.StringParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.*;

@Component
public class ExaminerUsersListFromDB extends Step {

    private StorageService storageService;
    private UserService userService;
    private ThemeService themeService;
    private StudentReviewService studentReviewService;

    public ExaminerUsersListFromDB(StorageService storageService,
                                   UserService userService,
                                   ThemeService themeService,
                                   StudentReviewService studentReviewService) {
        super("", DEF_BACK_KB);
        this.userService = userService;
        this.storageService = storageService;
        this.themeService = themeService;
        this.studentReviewService = studentReviewService;
    }

    @Override
    public void enter(BotContext context) {

    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException, NoNumbersEnteredException, NoDataEnteredException {
        String command = context.getInput();
        Integer vkId = context.getVkId();

        // Обрабатываются команды пользователя
        if (StringParser.isNumeric(command)) {

            // Из текущего шага извлекается список ID пользователей, а из 2х шагов назад - ID темы
            List<String> usersIds = storageService.getUserStorage(vkId, EXAMINER_USERS_LIST_FROM_DB);
            Long freeThemeId = Long.parseLong(storageService.getUserStorage(vkId, EXAMINER_FREE_THEMES_LIST).get(0));

            Integer studentNumber = Integer.parseInt(command);
            if (studentNumber <= 0 || studentNumber > usersIds.size()) {
                throw new ProcessInputException("Введено неподходящее число");
            }

            // Из списка получаем выбранный пользователем ID
            Long studentId = Long.parseLong(usersIds.get(studentNumber - 1));

            // Выполняется проверка, есть ли в таблице student_review запись, связанная с этим студентом и с записью из таблицы free_theme
            Boolean isExistStudentReview = studentReviewService.isExistStudentReviewByStudentIdAndThemeId(studentId, freeThemeId);

            // В текущий шаг сохраняется ID студента, а в следующий передается информация о наличии связи студент-тема
            sendUserToNextStep(context, EXAMINER_GET_INFO_LAST_REVIEW);
            storageService.updateUserStorage(vkId, EXAMINER_USERS_LIST_FROM_DB, Arrays.asList(studentId.toString()));
            storageService.updateUserStorage(vkId, EXAMINER_GET_INFO_LAST_REVIEW, Arrays.asList(freeThemeId.toString(), studentId.toString(), isExistStudentReview.toString()));

        } else if (command.equalsIgnoreCase("назад")) {
            sendUserToNextStep(context, EXAMINER_CHOOSE_METHOD_TO_ADD_STUDENT);
        } else {
            throw new ProcessInputException("Введена неверная команда...");
        }
    }

    @Override
    public String getDynamicText(BotContext context) {
        Integer vkId = context.getVkId();
        List<User> allUsers = userService.getAllUsers();

        // Из 2х шагов извлекается ID темы
        Long freeThemeId = Long.parseLong(storageService.getUserStorage(vkId, EXAMINER_FREE_THEMES_LIST).get(0));
        Theme freeTheme = themeService.getThemeById(freeThemeId);

        /* Бот выводит сообщение со списком всех пользователей из БД и формирует список их ID,
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

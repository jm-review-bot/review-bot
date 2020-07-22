package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.NoDataEnteredException;
import spring.app.exceptions.NoNumbersEnteredException;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.StudentReview;
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

        /* Из текущего шага извлекается ID темы и ID всех пользователей:
        * 0й элемент - ID темы
        * с 1го до последнего - ID пользователей
        * */
        List<String> themeIdAndUsersIds = storageService.getUserStorage(vkId, EXAMINER_USERS_LIST_FROM_DB);
        Long freeThemeId = Long.parseLong(themeIdAndUsersIds.get(0));

        // Обрабатываются команды пользователя
        if (StringParser.isNumeric(command)) {
            Integer studentNumber = Integer.parseInt(command);
            if (studentNumber <= 0 || studentNumber > themeIdAndUsersIds.size() - 1) {
                throw new ProcessInputException("Введено неподходящее число");
            }

            // Из списка получаем выбранный пользователем ID
            Long studentId = Long.parseLong(themeIdAndUsersIds.get(studentNumber));

            // Выполняется проверка, есть ли в таблице student_review запись, связанная с этим студентом и с записью из таблицы free_theme
            Boolean isExistStudentReview = studentReviewService.isExistStudentReviewByStudentIdAndThemeId(studentId, freeThemeId);

            // В следующий шаг передаются: ID темы, ID студента и информация о наличии в таблице student_review связи студент-тема
            sendUserToNextStep(context, EXAMINER_CHANGE_REVIEW_STATUS);
            storageService.updateUserStorage(vkId, EXAMINER_CHANGE_REVIEW_STATUS, Arrays.asList(freeThemeId.toString(), studentId.toString(), isExistStudentReview.toString()));

        } else if (command.equalsIgnoreCase("назад")) {
            sendUserToNextStep(context, EXAMINER_CHOOSE_METHOD_TO_ADD_STUDENT);

            // В следующий шаг передается ID темы
            storageService.updateUserStorage(vkId, EXAMINER_CHOOSE_METHOD_TO_ADD_STUDENT, Arrays.asList(freeThemeId.toString()));

        } else {
            throw new ProcessInputException("Введена неверная команда...");
        }
        storageService.removeUserStorage(vkId, EXAMINER_USERS_LIST_FROM_DB);
    }

    @Override
    public String getDynamicText(BotContext context) {
        Integer vkId = context.getVkId();
        List<User> allUsers = userService.getAllUsers();

        // Из предыдущего шага извлекается ID темы
        Long freeThemeId = Long.parseLong(storageService.getUserStorage(vkId, EXAMINER_USERS_LIST_FROM_DB).get(0));
        Theme freeTheme = themeService.getThemeById(freeThemeId);

        /* В текущий шаг закладывается ID темы и ID всех пользователей:
         * 0й элемент - ID темы
         * с 1го до последнего - ID пользователей
         * */
        List<String> themeIdAndUsersIds = new ArrayList<>();
        themeIdAndUsersIds.add(freeThemeId.toString());
        for (User user : allUsers) {
            themeIdAndUsersIds.add(user.getId().toString());
        }
        storageService.updateUserStorage(vkId, EXAMINER_USERS_LIST_FROM_DB, themeIdAndUsersIds);

        // Бот выводит сообщение со списком всех пользователей из БД
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
        }
        return infoMessage.toString();
    }

    @Override
    public String getDynamicKeyboard(BotContext context) {
        return "";
    }
}

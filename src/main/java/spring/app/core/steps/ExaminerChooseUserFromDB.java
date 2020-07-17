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

import java.util.Arrays;
import java.util.List;

import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.*;

@Component
public class ExaminerChooseUserFromDB extends Step {

    private StorageService storageService;
    private UserService userService;
    private ThemeService themeService;
    private ReviewService reviewService;
    private StudentReviewService studentReviewService;

    public ExaminerChooseUserFromDB(StorageService storageService,
                                    UserService userService,
                                    ThemeService themeService,
                                    ReviewService reviewService,
                                    StudentReviewService studentReviewService) {
        super("", DEF_BACK_KB);
        this.userService = userService;
        this.storageService = storageService;
        this.themeService = themeService;
        this.reviewService = reviewService;
        this.studentReviewService = studentReviewService;
    }

    @Override
    public void enter(BotContext context) {

    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException, NoNumbersEnteredException, NoDataEnteredException {
        String command = context.getInput();
        Integer vkId = context.getVkId();
        // Из предыдущего шага извлекается ID темы, выбранной пользователем
        Long themeId = Long.parseLong(storageService.getUserStorage(vkId, EXAMINER_CHOOSE_USER_FROM_DB).get(0));
        if (command.equalsIgnoreCase("назад")) {
            sendUserToNextStep(context, EXAMINER_CHOOSE_METHOD_TO_ADD_STUDENT);
            // В следующий шаг передается ID темы, выбранной пользователем
            storageService.updateUserStorage(vkId, EXAMINER_CHOOSE_METHOD_TO_ADD_STUDENT, Arrays.asList(themeId.toString()));
            storageService.removeUserStorage(vkId, EXAMINER_CHOOSE_USER_FROM_DB);
        } else if (StringParser.isNumeric(command)) {
            Integer studentNumber = Integer.parseInt(command);
            List<User> allUsers = userService.getAllUsers();
            if (studentNumber <= 0 || studentNumber > allUsers.size()) {
                throw new ProcessInputException("Введено неподходящее число");
            }
            User student = allUsers.get(studentNumber - 1);
            Theme theme = themeService.getThemeById(themeId);
            // Выполняется проверка, есть ли в таблице student_review запись, связанная с этим студентом и с записью из таблицы free_theme
            List<StudentReview> studentReviews = studentReviewService.getAllStudentReviewsByStudentIdAndTheme(student.getId(), theme);
            Boolean studentReviewIsExist = (studentReviews.size() > 0 ? true : false);
            sendUserToNextStep(context, EXAMINER_CHANGE_REVIEW_STATUS);
            // В следующий шаг передаются: ID темы, ID студента и информация о наличии в таблице student_review связи студент-тема
            storageService.updateUserStorage(vkId, EXAMINER_CHANGE_REVIEW_STATUS, Arrays.asList(themeId.toString(), student.getId().toString(), studentReviewIsExist.toString()));
            storageService.removeUserStorage(vkId, EXAMINER_CHOOSE_USER_FROM_DB);
        } else {
            throw new ProcessInputException("Введена неверная команда...");
        }
    }

    @Override
    public String getDynamicText(BotContext context) {
        Integer vkId = context.getVkId();
        List<User> allUsers = userService.getAllUsers();
        List<Theme> themes = themeService.getFreeThemesByExaminerId(context.getUser().getId());
        // Из предыдущего шага извлекается ID темы, выбранной пользователем
        Long themeId = Long.parseLong(storageService.getUserStorage(vkId, EXAMINER_CHOOSE_USER_FROM_DB).get(0));
        Theme theme = themeService.getThemeById(themeId);
        StringBuilder infoMessage = new StringBuilder();
        infoMessage.append(
                String.format("Защита темы %s.\nВыберите студента из списка:\n",
                        theme.getTitle())
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

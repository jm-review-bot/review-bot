package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.NoDataEnteredException;
import spring.app.exceptions.NoNumbersEnteredException;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.StudentReview;
import spring.app.model.Theme;
import spring.app.model.User;
import spring.app.service.abstraction.StorageService;
import spring.app.service.abstraction.StudentReviewService;
import spring.app.service.abstraction.ThemeService;
import spring.app.service.abstraction.UserService;

import java.util.List;

import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.*;

@Component
public class AdminSetPassedReviewGetThemesStatus extends Step {

    StorageService storageService;
    ThemeService themeService;
    UserService userService;
    StudentReviewService studentReviewService;

    public AdminSetPassedReviewGetThemesStatus(StorageService storageService,
                                               ThemeService themeService,
                                               UserService userService,
                                               StudentReviewService studentReviewService) {
        super("", DEF_BACK_KB);
        this.storageService = storageService;
        this.themeService = themeService;
        this.userService = userService;
        this.studentReviewService = studentReviewService;
    }

    @Override
    public void enter(BotContext context) {

    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException, NoNumbersEnteredException, NoDataEnteredException {
        String command = context.getInput();
        if (command.equals("Назад")) {
          sendUserToNextStep(context, ADMIN_SET_PASSED_REVIEW_GET_USERS_LIST);
        } else {
            throw new ProcessInputException("Введена неверная команда...");
        }
    }

    /* Этот метод выводит сообщение, содержащие список всех существующих тем в БД и помечает их статус (пройдена или нет) для выбранного
     * в предыдущем шаге пользователя. */
    @Override
    public String getDynamicText(BotContext context) {
        List<Theme> allThemes = themeService.getAllThemes();
        Long studentId = Long.parseLong(storageService.getUserStorage(context.getVkId(), ADMIN_SET_PASSED_REVIEW_GET_USERS_LIST).get(0));
        User student = userService.getUserById(studentId);
        StringBuilder infoMessage = new StringBuilder(String.format(
                "Студент %s %s. Выберите тему, которую вы хотите сделать пройденной. По данной теме, а также по всем предыдущим темам будут созданы ревью со статусом \"Пройдено\". Проверяющим по данным ревью будет назначен проверяющий по умолчанию.\n" +
                        "Список тем:\n\n",
                student.getFirstName(),
                student.getLastName()
        ));
        for (int i = 0; i < allThemes.size(); i++) {
            Theme theme = allThemes.get(i);
            StudentReview studentReview = studentReviewService.getLastStudentReviewByStudentIdAndThemeId(studentId, theme.getId());
            String lastReviewStatus = "не пройдено";
            if (studentReview != null) {
                lastReviewStatus = (studentReview.getIsPassed() ? "пройдено" : "не пройдено");
            }
            infoMessage.append(String.format(
                    "[%s] %s (Статус: %s)\n",
                    i + 1,
                    theme.getTitle(),
                    lastReviewStatus
            ));
        }
        return infoMessage.toString();
    }

    @Override
    public String getDynamicKeyboard(BotContext context) {
        return "";
    }
}

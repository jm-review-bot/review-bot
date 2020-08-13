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

import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.*;

@Component
public class AdminSetPassedReviewResult extends Step {

    private final StorageService storageService;
    private final UserService userService;
    private final ThemeService themeService;

    public AdminSetPassedReviewResult(StorageService storageService,
                                      UserService userService,
                                      ThemeService themeService) {
        super("", DEF_BACK_KB);
        this.storageService = storageService;
        this.userService = userService;
        this.themeService = themeService;
    }

    @Override
    public void enter(BotContext context) {

    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException, NoNumbersEnteredException, NoDataEnteredException {
        String command = context.getInput();
        switch (command) {
            case "Назад":
                storageService.clearUsersOfStorage(context.getVkId());
                sendUserToNextStep(context, ADMIN_CHOOSE_ACTION_FOR_REVIEW);
                break;
            default:
                throw new ProcessInputException("Введена неверная команда...");
        }
    }

    @Override
    public String getDynamicText(BotContext context) {
        Long studentId = Long.parseLong(storageService.getUserStorage(context.getVkId(), ADMIN_SET_PASSED_REVIEW_GET_USERS_LIST).get(0));
        Long themeId = Long.parseLong(storageService.getUserStorage(context.getVkId(), ADMIN_SET_PASSED_REVIEW_GET_THEMES_STATUS).get(0));
        User student = userService.getUserById(studentId);
        Theme theme = themeService.getThemeById(themeId);
        return String.format(
                "Тема \"%s\" была успешно сделана выполненной у студента %s %s",
                theme.getTitle(),
                student.getFirstName(),
                student.getLastName()
        );
    }

    @Override
    public String getDynamicKeyboard(BotContext context) {
        return "";
    }
}

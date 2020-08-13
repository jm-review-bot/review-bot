package spring.app.core.steps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.NoDataEnteredException;
import spring.app.exceptions.NoNumbersEnteredException;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.Theme;
import spring.app.model.User;
import spring.app.service.abstraction.*;

import java.util.Arrays;

import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.*;

@Component
public class AdminSetPassedReview extends Step {

    private final StorageService storageService;
    private final UserService userService;
    private final ThemeService themeService;
    private final StudentReviewService studentReviewService;

    private final static Logger logger = LoggerFactory.getLogger(AdminSetPassedReview.class);

    public AdminSetPassedReview(StorageService storageService,
                                UserService userService,
                                ThemeService themeService,
                                StudentReviewService studentReviewService) {
        super("", "");
        this.storageService = storageService;
        this.userService = userService;
        this.themeService = themeService;
        this.studentReviewService = studentReviewService;
    }

    @Override
    public void enter(BotContext context) {

    }

    /* В методе, прежде чем будут выполняться какие-либо манипуляции, проверяется:
    * - не удален ли выбранный студент к текущему моменту из БД;
    * - не удалена ли выбранная тема к текущему моменту из БД;
    * - какой текущий статус выбранной темы был отображен пользователю.
    * Только после всех этих проверок производится установка статуса темам */
    @Override
    public void processInput(BotContext context) throws ProcessInputException, NoNumbersEnteredException, NoDataEnteredException {
        String command = context.getInput();
        switch (command) {
            case "Сделать пройденной":
                Long studentId = Long.parseLong(storageService.getUserStorage(context.getVkId(), ADMIN_SET_PASSED_REVIEW_GET_USERS_LIST).get(0));
                Long themeId = Long.parseLong(storageService.getUserStorage(context.getVkId(), ADMIN_SET_PASSED_REVIEW_GET_THEMES_STATUS).get(0));
                User student = userService.getUserById(studentId);
                Theme theme = themeService.getThemeById(themeId);
                if (student == null || theme == null) {
                    throw new ProcessInputException("Выбранные пользователь и/или тема были удалены из БД. Вернитесь назад и попробуйте еще раз.");
                }
                Boolean isPassedThemeByStudent = Boolean.parseBoolean(storageService.getUserStorage(context.getVkId(), ADMIN_SET_PASSED_REVIEW).get(0));
                if (isPassedThemeByStudent) {
                    throw new ProcessInputException("Тема уже имеет статус \"Пройдено\"");
                }
                studentReviewService.setPassedThisAndPreviousThemesForStudent(studentId, themeId);
                logger.info(
                        "Админ (vkId={}) изменил статус тем (включительно до темы (ID={} , Title=\"{}\")) студента (vkId={}, Student={} {}) на \"Пройдено\"",
                        context.getVkId(), themeId, theme.getTitle(), studentId, student.getFirstName(), student.getLastName());
                sendUserToNextStep(context, ADMIN_SET_PASSED_REVIEW_RESULT);
                break;
            case "Назад":
                storageService.removeUserStorage(context.getVkId(), ADMIN_SET_PASSED_REVIEW);
                sendUserToNextStep(context, ADMIN_SET_PASSED_REVIEW_GET_THEMES_STATUS);
                break;
            default:
                throw new ProcessInputException("Введена неверная команда...");
        }
    }

    /* В методе производится проверка текущего статуса выбранной темы для выбранного студента и в соответствии с этим
     * выводится необходимое сообщение. Кроме этого, этот статус сохраняется в текущем шаге для дальнейшего использования. */
    @Override
    public String getDynamicText(BotContext context) {
        Long studentId = Long.parseLong(storageService.getUserStorage(context.getVkId(), ADMIN_SET_PASSED_REVIEW_GET_USERS_LIST).get(0));
        Long themeId = Long.parseLong(storageService.getUserStorage(context.getVkId(), ADMIN_SET_PASSED_REVIEW_GET_THEMES_STATUS).get(0));
        User student = userService.getUserById(studentId);
        Theme theme = themeService.getThemeById(themeId);
        Boolean isPassedThemeByStudent = studentReviewService.isThemePassedByStudent(studentId, themeId);
        if (student == null || theme == null) { // На случай, если к текущему моменту из БД были удалены студент и/или тема
            storageService.updateUserStorage(context.getVkId(), ADMIN_SET_PASSED_REVIEW, Arrays.asList("true"));
            return "Выбранные пользователь и/или тема были удалены из БД. Вернитесь назад и попробуйте еще раз.";
        }
        String infoMessage = String.format(
                "Тема \"%s\", студент %s %s. Статус: %s.\n",
                theme.getTitle(),
                student. getFirstName(),
                student.getLastName(),
                (isPassedThemeByStudent ? "пройдено" : "не пройдено")
        );
        if (!isPassedThemeByStudent) {
            infoMessage.concat("Сделать тему пройденной? Вместе с этой темой также пройденными станут все предыдущие темы.");
        }
        storageService.updateUserStorage(context.getVkId(), ADMIN_SET_PASSED_REVIEW, Arrays.asList(isPassedThemeByStudent.toString()));
        return infoMessage;
    }

    @Override
    public String getDynamicKeyboard(BotContext context) {
        Boolean isPassedThemeByStudent = Boolean.parseBoolean(storageService.getUserStorage(context.getVkId(), ADMIN_SET_PASSED_REVIEW).get(0));
        if (isPassedThemeByStudent) {
            return DEF_BACK_KB;
        } else {
            return REVIEW_SET_PASSED;
        }
    }
}

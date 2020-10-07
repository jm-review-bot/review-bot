package spring.app.core.steps;

import org.springframework.beans.factory.annotation.Autowired;
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

import java.util.Optional;

import static spring.app.core.StepSelector.EXAMINER_CHOOSE_OLD_STUDENT_REVIEW_TO_EDIT;
import static spring.app.core.StepSelector.EXAMINER_ADD_NEW_STUDENT_REVIEW;
import static spring.app.core.StepSelector.EXAMINER_USERS_LIST_FROM_DB;
import static spring.app.core.StepSelector.EXAMINER_FREE_THEMES_LIST;
import static spring.app.core.StepSelector.EXAMINER_GET_INFO_LAST_REVIEW;
import static spring.app.util.Keyboards.EDIT_OLD_OR_ADD_NEW;
import static spring.app.util.Keyboards.ROW_DELIMETER_FR;
import static spring.app.util.Keyboards.DEF_BACK_KB;

@Component
public class ExaminerGetInfoLastReview extends Step{

    private final StorageService storageService;
    private final StudentReviewService studentReviewService;
    private final UserService userService;
    private final ThemeService themeService;

    @Autowired
    public ExaminerGetInfoLastReview(StorageService storageService,
                                     StudentReviewService studentReviewService,
                                     UserService userService,
                                     ThemeService themeService) {
        super("", EDIT_OLD_OR_ADD_NEW + ROW_DELIMETER_FR + DEF_BACK_KB);
        this.storageService = storageService;
        this.studentReviewService = studentReviewService;
        this.userService = userService;
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
        if (command.equalsIgnoreCase("редактировать старое")) {
            sendUserToNextStep(context, EXAMINER_CHOOSE_OLD_STUDENT_REVIEW_TO_EDIT);
        } else if (command.equalsIgnoreCase("добавить новое")) {
            sendUserToNextStep(context, EXAMINER_ADD_NEW_STUDENT_REVIEW);
        } else if (command.equalsIgnoreCase("назад")) {
            sendUserToNextStep(context, EXAMINER_USERS_LIST_FROM_DB);
            storageService.removeUserStorage(examinerVkId, EXAMINER_GET_INFO_LAST_REVIEW);
        } else {
            throw new ProcessInputException("Введена неверная команда...");
        }

    }

    @Override
    public String getDynamicText(BotContext context) {
        Integer examinerVkId = context.getVkId();

        /* Из шага EXAMINER_USERS_LIST_FROM_DB извлекается ID студента, из шага EXAMINER_FREE_THEMES_LIST - ID темы.
         * На остновании полученных данных из БД извлекается последнее ревью студента, если таковое есть */
        Long studentId = Long.parseLong(storageService.getUserStorage(examinerVkId, EXAMINER_USERS_LIST_FROM_DB).get(0));
        User student = userService.getUserById(studentId);
        Long freeThemeId = Long.parseLong(storageService.getUserStorage(examinerVkId, EXAMINER_FREE_THEMES_LIST).get(0));
        Theme freeTheme = themeService.getThemeById(freeThemeId);
        Optional<StudentReview> optionalStudentReview = studentReviewService.getLastStudentReviewByStudentIdAndThemeId(studentId, freeThemeId);

        // Бот выводит сообщение со статусом поледнего ревью студента по выбранной теме
        if (optionalStudentReview.isPresent()) {
            StudentReview lastStudentReview = optionalStudentReview.get();
            return String.format(
                    "Ревью по теме %s. Студент %s %s.\n" +
                            "Статус последнего ревью: %s.\n" +
                            "Выберите действие:",
                    freeTheme.getTitle(),
                    student.getFirstName(),
                    student.getLastName(),
                    (lastStudentReview.getIsPassed() ? "пройдено" : "не пройдено")
            );
        } else {
            return String.format(
                    "Ревью по теме %s. Студент %s %s.\n" +
                            "Статус последнего ревью: не проводилось.\n" +
                            "Выберите действие:",
                    freeTheme.getTitle(),
                    student.getFirstName(),
                    student.getLastName()
            );
        }
    }

    @Override
    public String getDynamicKeyboard(BotContext context) {
        return "";
    }
}

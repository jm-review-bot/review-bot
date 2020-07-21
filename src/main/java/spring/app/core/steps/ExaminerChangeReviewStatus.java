package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.core.StepSelector;
import spring.app.exceptions.NoDataEnteredException;
import spring.app.exceptions.NoNumbersEnteredException;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.*;
import spring.app.service.abstraction.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.*;

@Component
public class ExaminerChangeReviewStatus extends Step {

    private StorageService storageService;
    private ThemeService themeService;
    private UserService userService;
    private StudentReviewService studentReviewService;
    private ReviewService reviewService;

    public ExaminerChangeReviewStatus(StorageService storageService,
                                      ThemeService themeService,
                                      UserService userService,
                                      StudentReviewService studentReviewService,
                                      ReviewService reviewService) {
        super("", PASS_OR_NOT_PASS_OR_BACK);
        this.storageService = storageService;
        this.themeService = themeService;
        this.userService = userService;
        this.studentReviewService = studentReviewService;
        this.reviewService = reviewService;
    }

    @Override
    public void enter(BotContext context) {

    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException, NoNumbersEnteredException, NoDataEnteredException {
        Integer vkId = context.getVkId();
        String command = context.getInput();

        // Из предыдущего шага извлекаютя ID темы и ID студента
        Long freeThemeId = Long.parseLong(storageService.getUserStorage(vkId, StepSelector.EXAMINER_CHANGE_REVIEW_STATUS).get(0));
        Long studentId = Long.parseLong(storageService.getUserStorage(vkId, StepSelector.EXAMINER_CHANGE_REVIEW_STATUS).get(1));
        Theme freeTheme = themeService.getThemeById(freeThemeId);
        User student = userService.getUserById(studentId);

        // Выполняется проверка, есть ли в таблице student_review запись, связанная с этим студентом и с записью из таблицы free_theme
        List<StudentReview> studentReviews = studentReviewService.getAllStudentReviewsByStudentIdAndTheme(studentId, freeTheme);
        Boolean isStudentReviewExists = (studentReviews.size() > 0 ? true : false);

        // Обрабатываются команды пользователя
        boolean commandIsPassed = command.equalsIgnoreCase("пройдено");
        boolean commandIsNotPassed = command.equalsIgnoreCase("не пройдено");
        if (commandIsPassed || commandIsNotPassed) {

            Review review;
            StudentReview studentReview;
            if (!isStudentReviewExists) {
                // Если такой записи нет, т.е. ревью ещё не создано, то создаются новые экземпляры Review...
                review = new Review();
                review.setTheme(freeTheme);
                review.setOpen(false);
                review.setUser(context.getUser());
                review.setDate(LocalDateTime.now());
                reviewService.addReview(review);

                // ... и StudentReview
                studentReview = new StudentReview();
                studentReview.setReview(review);
                studentReview.setUser(student);
            } else {
                // Если такая запись есть, то экземпляр StudentReview извлекается из БД
                studentReview = studentReviews.get(0);
            }

            // Устанавливается статус ревью
            studentReview.setPassed(commandIsPassed);

            // Вносятся соответствующие изменения в БД
            if (isStudentReviewExists) {
                studentReviewService.updateStudentReview(studentReview);
            } else {
                studentReviewService.addStudentReview(studentReview);
            }

        } else if (command.equalsIgnoreCase("назад")) {
            storageService.removeUserStorage(vkId, EXAMINER_CHANGE_REVIEW_STATUS);

            // В следующий шаг передается ID темы, выбранной пользователем
            sendUserToNextStep(context, EXAMINER_USERS_LIST_FROM_DB);
            storageService.updateUserStorage(vkId, EXAMINER_USERS_LIST_FROM_DB, Arrays.asList(freeThemeId.toString()));

        } else {
            throw new ProcessInputException("Введена неверная команда...");
        }

        // В текущем шаге обновляются: ID темы, ID студента и информация о наличии в таблице student_review связи студент-тема
        isStudentReviewExists = true;
        storageService.updateUserStorage(vkId, EXAMINER_CHANGE_REVIEW_STATUS, Arrays.asList(freeThemeId.toString(), studentId.toString(), isStudentReviewExists.toString()));
    }

    @Override
    public String getDynamicText(BotContext context) {
        Integer vkId = context.getVkId();

        // Из предыдущего шага извлекаются ID темы, ID студента и информация о существовании в таблице student_review связи студент-тема
        Long freeThemeId = Long.parseLong(storageService.getUserStorage(vkId, StepSelector.EXAMINER_CHANGE_REVIEW_STATUS).get(0));
        Long studentId = Long.parseLong(storageService.getUserStorage(vkId, StepSelector.EXAMINER_CHANGE_REVIEW_STATUS).get(1));
        Boolean isStudentReviewExist = Boolean.parseBoolean(storageService.getUserStorage(vkId, StepSelector.EXAMINER_CHANGE_REVIEW_STATUS).get(2));
        Theme theme = themeService.getThemeById(freeThemeId);
        User student = userService.getUserById(studentId);

        if (isStudentReviewExist) {
            StudentReview studentReview = studentReviewService.getAllStudentReviewsByStudentIdAndTheme(studentId, theme).get(0);
            return String.format(
                    "Ревью по теме \"%s\". Студент %s %s\n" +
                            "Текущий статус: %s\n" +
                            "Выберите новый статус ревью",
                    theme.getTitle(),
                    student.getFirstName(),
                    student.getLastName(),
                    (studentReview.getPassed() ? "пройдено" : "не пройдено")
            );
        } else {
            return String.format(
                    "Ревью по теме \"%s\". Студент %s %s\n" +
                            "Текущий статус: не проводилось\n" +
                            "Выберите новый статус ревью",
                    theme.getTitle(),
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

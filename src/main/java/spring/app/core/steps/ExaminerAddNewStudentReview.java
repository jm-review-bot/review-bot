package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.NoDataEnteredException;
import spring.app.exceptions.NoNumbersEnteredException;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.Review;
import spring.app.model.StudentReview;
import spring.app.model.Theme;
import spring.app.model.User;
import spring.app.service.abstraction.StorageService;
import spring.app.service.abstraction.StudentReviewService;
import spring.app.service.abstraction.ThemeService;
import spring.app.service.abstraction.UserService;
import spring.app.service.abstraction.ReviewService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static spring.app.core.StepSelector.EXAMINER_FREE_THEMES_LIST;
import static spring.app.core.StepSelector.EXAMINER_USERS_LIST_FROM_DB;
import static spring.app.core.StepSelector.EXAMINER_ADD_NEW_STUDENT_REVIEW;
import static spring.app.util.Keyboards.DEF_BACK_KB;
import static spring.app.util.Keyboards.PASS_OR_NOT_PASS_OR_BACK;

@Component
public class ExaminerAddNewStudentReview extends Step {

    private StorageService storageService;
    private ThemeService themeService;
    private UserService userService;
    private StudentReviewService studentReviewService;
    private ReviewService reviewService;

    public ExaminerAddNewStudentReview(StorageService storageService,
                                       ThemeService themeService,
                                       UserService userService,
                                       StudentReviewService studentReviewService,
                                       ReviewService reviewService) {
        super("", "");
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
        Integer examinerVkId = context.getVkId();
        String command = context.getInput();

        // Обрабатываются команды пользователя
        Boolean commandIsPassed = command.equalsIgnoreCase("пройдено");
        Boolean commandIsNotPassed = command.equalsIgnoreCase("не пройдено");
        if (commandIsPassed || commandIsNotPassed) {

            // Из шага EXAMINER_FREE_THEMES_LIST извлекается ID темы и из шага EXAMINER_USERS_LIST_FROM_DB - ID студента
            Long freeThemeId = Long.parseLong(storageService.getUserStorage(examinerVkId, EXAMINER_FREE_THEMES_LIST).get(0));
            Long studentId = Long.parseLong(storageService.getUserStorage(examinerVkId, EXAMINER_USERS_LIST_FROM_DB).get(0));
            Theme freeTheme = themeService.getThemeById(freeThemeId);
            User student = userService.getUserById(studentId);

            // Создаются новые экземпляры Review ...
            Review review = new Review();
            review.setTheme(freeTheme);
            review.setIsOpen(false);
            review.setUser(context.getUser());
            review.setDate(LocalDateTime.now());
            reviewService.addReview(review);

            // ... и StudentReview
            StudentReview studentReview = new StudentReview();
            studentReview.setReview(review);
            studentReview.setUser(student);
            studentReview.setIsPassed(commandIsPassed);
            studentReviewService.addStudentReview(studentReview);

            // В текущем шаге сохраняется информация о статусе нового ревью
            storageService.updateUserStorage(examinerVkId, EXAMINER_ADD_NEW_STUDENT_REVIEW, Arrays.asList(commandIsPassed.toString()));

        } else if (command.equalsIgnoreCase("назад")) {
            sendUserToNextStep(context, EXAMINER_USERS_LIST_FROM_DB);
            storageService.removeUserStorage(examinerVkId, EXAMINER_ADD_NEW_STUDENT_REVIEW);

            // Перед тем, как вернуться на шаг назад, необходимо !!!ОБЯЗАТЕЛЬНО!!! очистить хранилище текущего шага
            storageService.removeUserStorage(examinerVkId, EXAMINER_ADD_NEW_STUDENT_REVIEW);
        } else {
            throw new ProcessInputException("Введена неверная команда...");
        }
    }

    @Override
    public String getDynamicText(BotContext context) {
       Integer vkId = context.getVkId();

        // Из шага EXAMINER_FREE_THEMES_LIST извлекается ID темы и из шага EXAMINER_USERS_LIST_FROM_DB - ID студента
        Long freeThemeId = Long.parseLong(storageService.getUserStorage(vkId, EXAMINER_FREE_THEMES_LIST).get(0));
        Long studentId = Long.parseLong(storageService.getUserStorage(vkId, EXAMINER_USERS_LIST_FROM_DB).get(0));
        Theme freeTheme = themeService.getThemeById(freeThemeId);
        User student = userService.getUserById(studentId);

        // Выполняется проверка, не было ли только что создано экзаменатором новое ревью
        List<String> isExistNewReview = storageService.getUserStorage(vkId, EXAMINER_ADD_NEW_STUDENT_REVIEW);
        if (isExistNewReview != null) { // Ревью было только что создано
            return String.format(
                    "Ревью успешно сохранено.\n" +
                            "Тема \"%s\". Студент %s %s.\n" +
                            "Текущий статус: %s",
                    freeTheme.getTitle(),
                    student.getFirstName(),
                    student.getLastName(),
                    (Boolean.parseBoolean(isExistNewReview.get(0)) ? "пройдено" : "не пройдено")
            );

        } else { // Был совершен переход на текущий шаг для создания нового ревью
            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            Date currentDate = new Date();
            // Бот предлагает выбрать статус нового ревью
            return String.format(
                    "Ревью по теме \"%s\". Студент %s %s. Дата проведения %s\n" +
                            "Выберите статус ревью",
                    freeTheme.getTitle(),
                    student.getFirstName(),
                    student.getLastName(),
                    dateFormat.format(currentDate)
            );
        }
    }

    @Override
    public String getDynamicKeyboard(BotContext context) {

        // Выполняется проверка, не было ли только что создано экзаменатором новое ревью
        List<String> isExistNewReview = storageService.getUserStorage(context.getVkId(), EXAMINER_ADD_NEW_STUDENT_REVIEW);
        if (isExistNewReview != null) { // Ревью было только что создано
            return DEF_BACK_KB;
        } else { // Был совершен переход на текущий шаг для создания нового ревью
            return PASS_OR_NOT_PASS_OR_BACK;
        }
    }
}

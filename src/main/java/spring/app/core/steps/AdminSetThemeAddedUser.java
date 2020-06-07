package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.Review;
import spring.app.model.StudentReview;
import spring.app.model.Theme;
import spring.app.model.User;
import spring.app.service.abstraction.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static spring.app.core.StepSelector.ADMIN_ADD_USER;
import static spring.app.util.Keyboards.NO_KB;

@Component
public class AdminSetThemeAddedUser extends Step {

    private Map<Integer, Theme> themes = new HashMap<>();
    private final StorageService storageService;
    private final UserService userService;
    private final ThemeService themeService;
    private final ReviewService reviewService;
    private final StudentReviewService studentReviewService;

    public AdminSetThemeAddedUser(StorageService storageService, UserService userService,
                                  ThemeService themeService, ReviewService reviewService,
                                  StudentReviewService studentReviewService) {
        this.storageService = storageService;
        this.userService = userService;
        this.themeService = themeService;
        this.reviewService = reviewService;
        this.studentReviewService = studentReviewService;
    }

    @Override
    public void enter(BotContext context) {

        Integer vkId = context.getVkId();
        Long addedUserId = Long.parseLong(storageService.getUserStorage(vkId, ADMIN_ADD_USER).get(0));
        User addedUser = userService.getUserById(addedUserId);
        // Получение всех тем
        themeService.getAllThemes().forEach(theme -> themes.putIfAbsent(theme.getPosition(), theme));
        StringBuilder themeList = new StringBuilder("Выберите тему, с которой пользователь ");
        themeList
                .append(addedUser.getFirstName())
                .append(" ")
                .append(addedUser.getLastName())
                .append(" ")
                .append(" (https://vk.com/id")
                .append(addedUser.getVkId())
                .append(") ")
                .append("может начать сдачу ревью:\n");

        for (Integer position : themes.keySet()) {
            themeList.append(String.format("[%d] %s\n", position, themes.get(position).getTitle()));
        }
        // Вывод пользователю
        text = themeList.toString();
        keyboard = NO_KB;
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException {

        String userInput = context.getInput();
        List<String> themePositionsList = themes.keySet().stream()
                .map(Object::toString)
                .collect(toList());

        if (themePositionsList.contains(userInput)) {
            // Если выбрана не первая тема, то создаем фейковые ревью
            if (!userInput.equals("1")) {
                // Получение пользователей
                int vkId = context.getVkId();
                long addedUserId = Long.parseLong(storageService.getUserStorage(vkId, ADMIN_ADD_USER).get(0));
                int themePosition = Integer.parseInt(userInput);
                User addedUser = userService.getUserById(addedUserId);
                User admin = context.getUser();
                // Создание всех ревью, которые прошел пользователь
                for (int i = 1; i < themePosition; i++) {
                    Theme currentTheme = themeService.getByPosition(i);

                    Review fakeReview = new Review(admin, currentTheme, false, LocalDateTime.now());
                    reviewService.addReview(fakeReview);
                    StudentReview studentReview = new StudentReview(addedUser, fakeReview, true);
                    studentReviewService.addStudentReview(studentReview);
                }
                // Начисление ревью поинтов
                int reviewCost = themeService.getByPosition(themePosition).getReviewPoint();
                addedUser.setReviewPoint(reviewCost);
                userService.updateUser(addedUser);
            }
            nextStep = ADMIN_ADD_USER;
        } else {
            throw new ProcessInputException("Введена неверная команда...\n\n Введите цифру, соответствующую теме рьвью.");
        }
    }
}
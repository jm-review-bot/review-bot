package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.*;
import spring.app.service.abstraction.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static spring.app.core.StepSelector.ADMIN_ADD_USER;
import static spring.app.core.StepSelector.ADMIN_SET_THEME_ADDED_USER;

@Component
public class AdminSetThemeAddedUser extends Step {

    private final StorageService storageService;
    private final UserService userService;
    private final ThemeService themeService;
    private final ReviewService reviewService;
    private final StudentReviewService studentReviewService;

    public AdminSetThemeAddedUser(StorageService storageService, UserService userService,
                                  ThemeService themeService, ReviewService reviewService,
                                  StudentReviewService studentReviewService) {
        super("", "");
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
        StringBuilder themeList;
        List<String> listTheme = new ArrayList<>();
        List<Theme> themes = themeService.getAllThemes();
        // Получение всех тем
        themes.sort(Comparator.comparing(Theme::getPosition));
        themeList = new StringBuilder("Выберите тему, с которой пользователь ");
        themeList
                .append(addedUser.getFirstName())
                .append(" ")
                .append(addedUser.getLastName())
                .append(" ")
                .append(" (https://vk.com/id")
                .append(addedUser.getVkId())
                .append(") ")
                .append("может начать сдачу ревью:\n");

        for (Theme theme : themes) {
            themeList.append(String.format("[%d] %s\n", theme.getPosition(), theme.getTitle()));
        }

        listTheme.add(themeList.toString());
        storageService.updateUserStorage(vkId, ADMIN_SET_THEME_ADDED_USER, listTheme);
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException {
        String userInput = context.getInput();
        List<Theme> themes = themeService.getAllThemes();

        List<String> themePositionsList = themes.stream()
                .map(Theme::getPosition)
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
            sendUserToNextStep(context, ADMIN_ADD_USER);
        } else {
            throw new ProcessInputException("Введена неверная команда...\n\n Введите цифру, соответствующую теме рьвью.");
        }
    }

    @Override
    public String getDynamicText(BotContext context) {
        List<String> themeList = storageService.getUserStorage(context.getVkId(), ADMIN_SET_THEME_ADDED_USER);
        return themeList.get(0);
    }

    @Override
    public String getDynamicKeyboard(BotContext context) {
        return "";
    }
}

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
import spring.app.service.abstraction.ReviewService;
import spring.app.service.abstraction.StorageService;
import spring.app.service.abstraction.StudentReviewService;
import spring.app.service.abstraction.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.BACK_KB;

@Component
public class AdminAddUserInputTheme extends Step {
    private Map<Integer, Theme> themes = new HashMap<>();
    private final ReviewService reviewService;
    private final StudentReviewService studentReviewService;
    private final UserService userService;

    public AdminAddUserInputTheme(ReviewService reviewService, StudentReviewService studentReviewService, UserService userService) {
        this.reviewService = reviewService;
        this.studentReviewService = studentReviewService;
        this.userService = userService;
    }

    @Override
    public void enter(BotContext context) {
        context.getThemeService().getAllThemes().forEach(theme -> themes.putIfAbsent(theme.getPosition(), theme));
        StringBuilder themeList = new StringBuilder("Выбери тему, с которой пользователь может начинать сдавать ревью, в качестве ответа пришли цифру (номер темы):\n ");
//        themeList.append("Ты можешь принимать ревью только по тем темам, которые успешно сдал.\n\n");
        for (Integer position : themes.keySet()) {
            themeList.append(String.format("[%d] %s\n", position, themes.get(position).getTitle()));
        }
        themeList.append("\nИли нажмите на кнопку \"Назад\" для возврата к предыдущему меню.");
        text = themeList.toString();
        keyboard = BACK_KB;
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException, NoNumbersEnteredException, NoDataEnteredException {
        StorageService storageService = context.getStorageService();
        String userInput = context.getInput();
        Integer vkId = context.getVkId();
        List<String> themePositionsList = themes.keySet().stream()
                .map(Object::toString)
                .collect(toList());
        if (themePositionsList.contains(userInput)) {
            // вытаскиваем themeId по позиции, позиция соответствует пользовательскому вводу
            String themeId = themes.get(Integer.parseInt(userInput)).getId().toString();
            // Проверяем является ли выбранная тема первой

            List<Theme> allThemes = context.getThemeService().getAllThemes();
            if(allThemes.get(0).equals(themes.get(Integer.parseInt(userInput)))){
                // если тема первая, то добавляем пользователя
                nextStep = ADMIN_ADD_USER;
                } else {
                //если нет, то создаем фейковое ревью, ревьюером которого является текущий админ.
                Review fakeReview = new Review();
                fakeReview.setTheme(themes.get(Integer.parseInt(userInput)-1));
                fakeReview.setUser(context.getUser());
                fakeReview.setOpen(false);
                reviewService.addReview(fakeReview);

                StudentReview fakeStudentReview = new StudentReview();
                fakeStudentReview.setReview(fakeReview);
                fakeStudentReview.setPassed(true);
                Long maxId = userService.getMaxId();
                User user = userService.getUserById(maxId);
                // заменить 4 на значение из БД
                user.setReviewPoint(4);

                fakeStudentReview.setUser(user);
                studentReviewService.addStudentReview(fakeStudentReview);
                // обновляем у пользователя RP
                userService.updateUser(user);
                nextStep=ADMIN_ADD_USER;
            }

        } else if (userInput.equalsIgnoreCase("назад")) {
            nextStep = ADMIN_ADD_USER;
            // очищаем данные с этого шага и со следующего, если они есть
            storageService.removeUserStorage(vkId, ADMIN_ADD_USER_INPUT_THEME);
            if (storageService.getUserStorage(vkId, ADMIN_ADD_USER) != null) {
                storageService.removeUserStorage(vkId, ADMIN_ADD_USER);
            }
        } else if (userInput.equalsIgnoreCase("/start")) {
            nextStep = START;
            // очищаем данные с этого шага и со следующего, если они ес    throw new ProcessInputException("Ты пока не можешь принять эту тему, потому что не сдал по ней ревью.\n\n" +
            ////         ть
            storageService.removeUserStorage(vkId, ADMIN_ADD_USER_INPUT_THEME);
            if (storageService.getUserStorage(vkId, ADMIN_ADD_USER) != null) {
                storageService.removeUserStorage(vkId, ADMIN_ADD_USER);
            }
        } else {
            nextStep = USER_TAKE_REVIEW_ADD_THEME;
            throw new ProcessInputException("Введена неверная команда...\n\n Введи цифру, соответствующую теме рьвью или нажми на кнопку \"Назад\" для возврата в главное меню");
        }
    }
}

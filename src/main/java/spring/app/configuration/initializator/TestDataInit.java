package spring.app.configuration.initializator;

import org.springframework.beans.factory.annotation.Autowired;
import spring.app.core.StepSelector;
import spring.app.core.StepHolder;
import spring.app.core.steps.*;
import spring.app.model.Review;
import spring.app.model.Role;
import spring.app.model.Theme;
import spring.app.model.User;
import spring.app.service.abstraction.ReviewService;
import spring.app.service.abstraction.RoleService;
import spring.app.service.abstraction.ThemeService;
import spring.app.service.abstraction.UserService;

import java.time.LocalDateTime;
import java.util.Map;


public class TestDataInit {
    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private ThemeService themeService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private StepHolder stepHolder;

    public TestDataInit() {
    }

    private void init() throws Exception {

        Role roleAdmin = new Role();
        roleAdmin.setName("ADMIN");
        roleService.addRole(roleAdmin);

        Role roleUser = new Role();
        roleUser.setName("USER");
        roleService.addRole(roleUser);

        User admin = new User();
        admin.setFirstName("admin");
        admin.setLastName("admin");
        admin.setReviewPoint(0);
        admin.setVkId(1374221); // change this to your vkId for testing
        admin.setRole(roleAdmin);
        admin.setChatStep("START");
        userService.addUser(admin);

        User user = new User();
        user.setFirstName("Иван");
        user.setLastName("Иванов");
        user.setReviewPoint(4);
        user.setVkId(582532887);
        user.setRole(roleUser);
        user.setChatStep("START");
        userService.addUser(user);

        User user2 = new User();
        user2.setFirstName("Петр");
        user2.setLastName("Петров");
        user2.setReviewPoint(4);
        user2.setVkId(1582532887);
        user2.setRole(roleUser);
        user2.setChatStep("START");
        userService.addUser(user2);

        Map<StepSelector, Step> steps = stepHolder.getSteps();
        steps.put(StepSelector.START, new Start());
        steps.put(StepSelector.USER_MENU, new UserMenu());
        steps.put(StepSelector.ADMIN_MENU, new AdminMenu());
        steps.put(StepSelector.ADMIN_ADD_USER, new AdminAddUser());
        steps.put(StepSelector.ADMIN_REMOVE_USER, new AdminRemoveUser());
        steps.put(StepSelector.USER_TAKE_REVIEW, new UserTakeReview());

        //add theme
        Theme hibernate = new Theme();
        hibernate.setPosition(4);
        hibernate.setReviewPoint(4);
        hibernate.setTitle("Hibernate");
        themeService.addTheme(hibernate);

        Theme core = new Theme();
        core.setPosition(1);
        core.setReviewPoint(0);
        core.setTitle("Java Core");
        themeService.addTheme(core);

        Theme algorithm = new Theme();
        algorithm.setPosition(7);
        algorithm.setReviewPoint(4);
        algorithm.setTitle("Алгоритмы");
        themeService.addTheme(algorithm);

        Theme multithreading = new Theme();
        multithreading.setPosition(2);
        multithreading.setReviewPoint(4);
        multithreading.setTitle("Многопоточность");
        themeService.addTheme(multithreading);

        Theme sql = new Theme();
        sql.setPosition(3);
        sql.setReviewPoint(4);
        sql.setTitle("SQL");
        themeService.addTheme(sql);

        Theme spring = new Theme();
        spring.setPosition(5);
        spring.setReviewPoint(4);
        spring.setTitle("Spring");
        themeService.addTheme(spring);

        Theme finalReview = new Theme();
        finalReview.setPosition(8);
        finalReview.setReviewPoint(4);
        finalReview.setTitle("Финальное ревью");
        themeService.addTheme(finalReview);

        Theme patterns = new Theme();
        patterns.setPosition(6);
        patterns.setReviewPoint(4);
        patterns.setTitle("Паттерны");
        themeService.addTheme(patterns);

        //add review
        Review coreReview = new Review();
        coreReview.setDate(LocalDateTime.of(2020, 3, 27, 23, 20));
        coreReview.setOpen(true);
        coreReview.setTheme(core);
        coreReview.setUser(admin);
        reviewService.addReview(coreReview);

        Review sqlReview = new Review();
        sqlReview.setDate(LocalDateTime.of(2020, 3, 27, 23, 21));
        sqlReview.setOpen(false); // не должен выводить reminder на это сообщение
        sqlReview.setTheme(sql);
        sqlReview.setUser(admin);
        reviewService.addReview(sqlReview);

        Review springReview = new Review();
        springReview.setDate(LocalDateTime.of(2020, 3, 27, 23, 22));
        springReview.setOpen(true);
        springReview.setTheme(spring);
        springReview.setUser(admin);
        reviewService.addReview(springReview);
    }
}

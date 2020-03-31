package spring.app.configuration.initializator;

import org.springframework.beans.factory.annotation.Autowired;
import spring.app.core.StepSelector;
import spring.app.core.StepHolder;
import spring.app.core.steps.*;
import spring.app.model.*;
import spring.app.service.abstraction.*;

import java.util.Map;

import java.time.LocalDateTime;


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
    private StudentReviewService studentReviewService;

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

        // add users test data
        User admin = new User();
        admin.setFirstName("admin");
        admin.setLastName("admin");
        admin.setReviewPoint(999);
        admin.setVkId(1374221); // change this to your vkId for testing
        admin.setRole(roleAdmin);
        admin.setChatStep("START");
        userService.addUser(admin);

        User admin2 = new User();
        admin2.setFirstName("Максим");
        admin2.setLastName("Ботюк");
        admin2.setReviewPoint(0);
        admin2.setVkId(87632583); // change this to your vkId for testing
        admin2.setRole(roleAdmin);
        admin2.setChatStep("START");
        userService.addUser(admin2);

        User user = new User();
        user.setFirstName("Антон");
        user.setLastName("Таврель");
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

        // add steps
        Map<StepSelector, Step> steps = stepHolder.getSteps();
        steps.put(StepSelector.START, new Start());
        steps.put(StepSelector.USER_MENU, new UserMenu());
        steps.put(StepSelector.ADMIN_MENU, new AdminMenu());
        steps.put(StepSelector.ADMIN_ADD_USER, new AdminAddUser());
        steps.put(StepSelector.ADMIN_REMOVE_USER, new AdminRemoveUser());

        //add theme
        Theme core = new Theme();
        core.setPosition(1);
        core.setReviewPoint(0);
        core.setTitle("Java Core");
        themeService.addTheme(core);

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

        Theme hibernate = new Theme();
        hibernate.setPosition(4);
        hibernate.setReviewPoint(4);
        hibernate.setTitle("Hibernate");
        themeService.addTheme(hibernate);

        Theme spring = new Theme();
        spring.setPosition(5);
        spring.setReviewPoint(4);
        spring.setTitle("Spring");
        themeService.addTheme(spring);

        Theme patterns = new Theme();
        patterns.setPosition(6);
        patterns.setReviewPoint(4);
        patterns.setTitle("Паттерны");
        themeService.addTheme(patterns);

        Theme algorithm = new Theme();
        algorithm.setPosition(7);
        algorithm.setReviewPoint(4);
        algorithm.setTitle("Алгоритмы");
        themeService.addTheme(algorithm);

        Theme finalReview = new Theme();
        finalReview.setPosition(8);
        finalReview.setReviewPoint(4);
        finalReview.setTitle("Финальное ревью");
        themeService.addTheme(finalReview);

        //add review
        Review coreReview = new Review();
        coreReview.setDate(LocalDateTime.of(2020, 3, 31, 23, 20));
        coreReview.setOpen(true);
        coreReview.setTheme(core);
        coreReview.setUser(admin);
        reviewService.addReview(coreReview);

        Review sqlReview = new Review();
        sqlReview.setDate(LocalDateTime.of(2020, 3, 31, 23, 21));
        sqlReview.setOpen(false); // не должен выводить reminder на это сообщение
        sqlReview.setTheme(sql);
        sqlReview.setUser(admin);
        reviewService.addReview(sqlReview);

        Review springReview = new Review();
        springReview.setDate(LocalDateTime.of(2020, 3, 31, 23, 22));
        springReview.setOpen(true);
        springReview.setTheme(spring);
        springReview.setUser(admin2);
        reviewService.addReview(springReview);

        StudentReview studentSpringReview = new StudentReview(admin2, springReview);
        studentReviewService.addStudentReview(studentSpringReview);
    }
}

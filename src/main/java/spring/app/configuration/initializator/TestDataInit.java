package spring.app.configuration.initializator;

import org.springframework.beans.factory.annotation.Autowired;
import spring.app.model.Review;
import spring.app.model.Role;
import spring.app.model.Theme;
import spring.app.model.User;
import spring.app.service.abstraction.ReviewService;
import spring.app.service.abstraction.RoleService;
import spring.app.service.abstraction.ThemeService;
import spring.app.service.abstraction.UserService;

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

    private void init() {

        // add roles
        Role roleAdmin = new Role();
        roleAdmin.setRole("ADMIN");
        roleService.addRole(roleAdmin);

        Role roleUser = new Role();
        roleUser.setRole("USER");
        roleService.addRole(roleUser);

        // add users test data
        User admin = new User();
        admin.setFirstName("admin");
        admin.setLastName("admin");
        admin.setReviewPoint(0);
        admin.setVkId("98189"); // поменять
        admin.setRole(roleAdmin);
        userService.addUser(admin);

        User user1 = new User();
        user1.setFirstName("Антон");
        user1.setLastName("Таврель");
        user1.setReviewPoint(4);
        user1.setVkId("582532887");
        user1.setRole(roleUser);
        userService.addUser(user1);

        User user2 = new User();
        user2.setFirstName("user");
        user2.setLastName("2");
        user2.setReviewPoint(8);
        user2.setVkId("7"); // поменять
        user2.setRole(roleUser);
        userService.addUser(user2);

        User user3 = new User();
        user3.setFirstName("user");
        user3.setLastName("3");
        user3.setReviewPoint(20);
        user3.setVkId("999"); // поменять
        user3.setRole(roleUser);
        userService.addUser(user3);

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
        coreReview.setDate(LocalDateTime.of(2020, 3, 26, 23, 46));
        coreReview.setOpen(true);
        coreReview.setTheme(core);
        coreReview.setUser(user1);
        reviewService.addReview(coreReview);

        Review sqlReview = new Review();
        sqlReview.setDate(LocalDateTime.of(2020, 3, 26, 23, 45));
        sqlReview.setOpen(false);
        sqlReview.setTheme(sql);
        sqlReview.setUser(user1); // потом поменять
        reviewService.addReview(sqlReview);

        Review springReview = new Review();
        springReview.setDate(LocalDateTime.of(2020, 3, 26, 23, 44));
        springReview.setOpen(true);
        springReview.setTheme(spring);
        springReview.setUser(user1); // потом поменять
        reviewService.addReview(springReview);


    }
}

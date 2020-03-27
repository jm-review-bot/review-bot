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
    private ReviewService reviewService;

    @Autowired
    private ThemeService themeService;

    private void init() throws Exception {

        Role roleAdmin = new Role();
        roleAdmin.setRole("ADMIN");
        roleService.addRole(roleAdmin);

        Role roleUser = new Role();
        roleUser.setRole("USER");
        roleService.addRole(roleUser);

        User admin = new User();
        admin.setFirstName("admin");
        admin.setLastName("admin");
        admin.setReviewPoint(0);
        admin.setVkId("98189");
        admin.setRole(roleAdmin);
        userService.addUser(admin);

        User user = new User();
        user.setFirstName("user");
        user.setLastName("user");
        user.setReviewPoint(4);
        user.setVkId("582532887");
        user.setRole(roleUser);
        userService.addUser(user);

        Theme theme = new Theme();
        theme.setPosition(1);
        theme.setReviewPoint(1);
        theme.setTitle("");
        themeService.addTheme(theme);

        Review review = new Review();
        review.setDate(LocalDateTime.of(2020, 3, 26, 21, 0, 0));
        review.setOpen(true);
        review.setTheme(theme);
        review.setUser(user);
        reviewService.addReview(review);
    }
}

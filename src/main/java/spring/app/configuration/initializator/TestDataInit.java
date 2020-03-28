package spring.app.configuration.initializator;

import org.springframework.beans.factory.annotation.Autowired;
import spring.app.core.StepSelector;
import spring.app.core.StepHolder;
import spring.app.core.steps.*;
import spring.app.model.Role;
import spring.app.model.User;
import spring.app.service.abstraction.RoleService;
import spring.app.service.abstraction.UserService;

import java.util.Map;


public class TestDataInit {
    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

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

        Map<StepSelector, Step> steps = stepHolder.getSteps();
        steps.put(StepSelector.START, new Start());
        steps.put(StepSelector.USER_MENU, new UserMenu());
        steps.put(StepSelector.ADMIN_MENU, new AdminMenu());
        steps.put(StepSelector.ADMIN_ADD_USER, new AdminAddUser());
        steps.put(StepSelector.ADMIN_REMOVE_USER, new AdminRemoveUser());
    }
}

package spring.app.configuration.initializator;

import org.springframework.beans.factory.annotation.Autowired;
import spring.app.core.StepSelector;
import spring.app.core.StepHolder;
import spring.app.core.steps.*;
import spring.app.model.Role;
import spring.app.model.User;
import spring.app.service.abstraction.RoleService;
import spring.app.service.abstraction.UserService;


public class TestDataInit {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

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
        admin.setChatStep("START");
        userService.addUser(user);

        User user2 = new User();
        user2.setFirstName("Петр");
        user2.setLastName("Петров");
        user2.setReviewPoint(4);
        user2.setVkId(1582532887);
        user2.setRole(roleUser);
        admin.setChatStep("START");
        userService.addUser(user2);

        StepHolder.steps.put(StepSelector.START, new Start());
        StepHolder.steps.put(StepSelector.USER_MENU, new UserMenu());
        StepHolder.steps.put(StepSelector.ADMIN_MENU, new AdminMenu());
        StepHolder.steps.put(StepSelector.ADMIN_ADD_USER, new AdminAddUser());
        StepHolder.steps.put(StepSelector.ADMIN_REMOVE_USER, new AdminRemoveUser());
    }
}

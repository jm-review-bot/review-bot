package spring.app.configuration.initializator;

import org.springframework.beans.factory.annotation.Autowired;
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
        roleAdmin.setRole("ADMIN");
        roleService.addRole(roleAdmin);

        Role roleUser = new Role();
        roleUser.setRole("USER");
        roleService.addRole(roleUser);

        User admin = new User();
        admin.setFirstName("admin");
        admin.setLastName("admin");
        admin.setReviewPoint(0);
        admin.setVkId("1374221"); // change this to your vkId for testing
        admin.setRole(roleAdmin);
        userService.addUser(admin);

        User user = new User();
        user.setFirstName("user");
        user.setLastName("user");
        user.setReviewPoint(4);
        user.setVkId("582532887");
        user.setRole(roleUser);
        userService.addUser(user);
    }
}

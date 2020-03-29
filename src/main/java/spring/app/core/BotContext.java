package spring.app.core;

import spring.app.model.Role;
import spring.app.model.User;
import spring.app.service.abstraction.RoleService;
import spring.app.service.abstraction.UserService;
import spring.app.service.abstraction.VkService;

public class BotContext {
    private final User user;
    private final Integer vkId;
    private final String input;
    private final Role role;
    private final UserService userService;
    private final RoleService roleService;
    private final VkService vkService;

    public BotContext(User user, Integer vkId, String input, Role role, UserService userService, RoleService roleService, VkService vkService) {
        this.user = user;
        this.vkId = vkId;
        this.input = input;
        this.role = role;
        this.userService = userService;
        this.roleService = roleService;
        this.vkService = vkService;
    }

    public Integer getVkId() {
        return vkId;
    }

    public String getInput() {
        return input;
    }

    public Role getRole() {
        return role;
    }

    public UserService getUserService() {
        return userService;
    }

    public User getUser() {
        return user;
    }

    public VkService getVkService() {
        return vkService;
    }

    public RoleService getRoleService() {
        return roleService;
    }
}

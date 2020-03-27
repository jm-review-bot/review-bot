package spring.app.core;

import spring.app.model.Role;
import spring.app.model.User;
import spring.app.service.abstraction.UserService;

public class BotContext {
    private final User user;
    private final Integer vkId;
    private final String input;
    private final Role role;
    private final UserService userService;

    public BotContext(User user, Integer vkId, String input, Role role, UserService userService) {
        this.user = user;
        this.vkId = vkId;
        this.input = input;
        this.role = role;
        this.userService = userService;
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
}

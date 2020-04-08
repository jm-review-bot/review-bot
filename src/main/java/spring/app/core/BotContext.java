package spring.app.core;

import spring.app.model.Role;
import spring.app.model.User;
import spring.app.service.abstraction.*;

public class BotContext {
    private final User user;
    private final Integer vkId;
    private final String input;
    private final Role role;
    private final UserService userService;
    private final RoleService roleService;
    private final VkService vkService;
    private final ThemeService themeService;
    private final ReviewService reviewService;
    private final StudentReviewService studentReviewService;
    private final StudentReviewAnswerService studentReviewAnswerService;

    public BotContext(User user, Integer vkId, String input, Role role, UserService userService,
                      VkService vkService, StudentReviewService studentReviewService, ThemeService themeService,
                      ReviewService reviewService, RoleService roleService, StudentReviewAnswerService studentReviewAnswerService) {
        this.user = user;
        this.vkId = vkId;
        this.input = input;
        this.role = role;
        this.userService = userService;
        this.roleService = roleService;
        this.vkService = vkService;
        this.themeService = themeService;
        this.reviewService = reviewService;
        this.studentReviewService = studentReviewService;
        this.studentReviewAnswerService = studentReviewAnswerService;

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

    public ThemeService getThemeService() {
        return themeService;
    }

    public ReviewService getReviewService() {
        return reviewService;
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

    public StudentReviewService getStudentReviewService() {
        return studentReviewService;
    }

    public StudentReviewAnswerService getStudentReviewAnswerService() {
        return studentReviewAnswerService;
    }
}

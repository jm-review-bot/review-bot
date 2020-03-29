package spring.app.core;

import spring.app.model.Role;
import spring.app.service.abstraction.ReviewService;
import spring.app.service.abstraction.ThemeService;
import spring.app.service.abstraction.UserService;

public class BotContext {
    private final Integer vkId;
    private final String input;
    private final Role role;
    private final UserService userService;
    private final ThemeService themeService;
    private final ReviewService reviewService;

    public BotContext(Integer vkId, String input, Role role, UserService userService, ThemeService themeService, ReviewService reviewService) {
        this.vkId = vkId;
        this.input = input;
        this.role = role;
        this.userService = userService;
        this.themeService = themeService;
        this.reviewService = reviewService;
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
}

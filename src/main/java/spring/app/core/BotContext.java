package spring.app.core;

import spring.app.model.Role;
import spring.app.model.User;
import spring.app.service.abstraction.*;

public class BotContext {
    private final User user;
    private final Integer vkId;
    private final String input;
    private final Role role;

    private final StepHolder stepHolder;

    public BotContext(User user, Integer vkId, String input, Role role, StepHolder stepHolder) {
        this.user = user;
        this.vkId = vkId;
        this.input = input;
        this.role = role;
        this.stepHolder = stepHolder;
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

    public User getUser() {
        return user;
    }

    public StepHolder getStepHolder() {
        return stepHolder;
    }
}

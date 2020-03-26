package spring.app.core;

import spring.app.model.Role;

public class BotContext {
    private final Integer vkId;
    private final String input;
    private final Role role;

    public BotContext(Integer vkId, String input, Role role) {
        this.vkId = vkId;
        this.input = input;
        this.role = role;
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
}

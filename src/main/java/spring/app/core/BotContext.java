package spring.app.core;

import spring.app.model.Role;

public class BotContext {
    private final ChatBot bot;
    private final Integer vkId;
    private final String input;
    private final Role role;

    public BotContext(ChatBot bot, Integer vkId, String input, Role role) {
        this.bot = bot;
        this.vkId = vkId;
        this.input = input;
        this.role = role;
    }

    public ChatBot getBot() {
        return bot;
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

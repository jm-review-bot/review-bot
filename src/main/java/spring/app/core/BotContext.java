package spring.app.core;

import com.vk.api.sdk.objects.messages.Message;

public class BotContext {
    private final ChatBot bot;
    private final Integer vkId;
    private final String input;

    public BotContext(ChatBot bot, Integer vkId, String input) {
        this.bot = bot;
        this.vkId = vkId;
        this.input = input;
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
}

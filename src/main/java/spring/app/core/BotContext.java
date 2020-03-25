package spring.app.core;

import com.vk.api.sdk.objects.messages.Message;

public class BotContext {
    private final ChatBot bot;
    private final Integer vkId;
    private final Message message;

    public BotContext(ChatBot bot, Integer vkId, Message message) {
        this.bot = bot;
        this.vkId = vkId;
        this.message = message;
    }

    public ChatBot getBot() {
        return bot;
    }

    public Integer getVkId() {
        return vkId;
    }

    public Message getMessage() {
        return message;
    }
}

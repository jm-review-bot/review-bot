package spring.app.core;

import com.vk.api.sdk.objects.messages.Message;
import spring.app.model.User;

public class BotContext {
    private final ChatBot bot;
    private final User user;
    private final Message message;

    public BotContext(ChatBot bot, User user, Message message) {
        this.bot = bot;
        this.user = user;
        this.message = message;
    }

    public ChatBot getBot() {
        return bot;
    }

    public User getUser() {
        return user;
    }

    public Message getMessage() {
        return message;
    }
}

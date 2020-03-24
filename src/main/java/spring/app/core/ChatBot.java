package spring.app.core;

import com.vk.api.sdk.objects.messages.Message;

import java.util.List;

public interface ChatBot {

    List<Message> readMessages();

    void replyForMessages(List<Message> messages);

    void sendMessage(String message, int userId);

    void sendMessage(String message, String keyboard, int userId);

//    void sendMessage(String message, List<String> attachment, int userId);

}

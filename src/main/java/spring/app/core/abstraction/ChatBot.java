package spring.app.core.abstraction;

import com.vk.api.sdk.objects.messages.Message;

import java.util.List;

public interface ChatBot {

    List<Message> readMessages();

    void replyForMessages(List<Message> messages);

//    void sendMessage(String text, Integer userId);

    void sendMessage(String text, String keyboard, Integer userId);

//    void sendMessage(String text, List<String> attachment, Integer userId);

}

package spring.app.service.abstraction;

import com.vk.api.sdk.objects.messages.Message;

import java.util.List;

public interface VkService {
    List<Message> getMessages();

    void sendMessage(String text, String keyboard, Integer userId);

}

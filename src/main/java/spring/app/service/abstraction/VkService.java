package spring.app.service.abstraction;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.Message;
import spring.app.exceptions.IncorrectVkIdsException;
import spring.app.model.User;

import java.util.List;

public interface VkService {
    List<Message> getMessages();

    void sendMessage(String text, String keyboard, Integer userId);

    User newUserFromVk(String userId) throws ClientException, ApiException, IncorrectVkIdsException;
}

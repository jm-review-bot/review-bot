package spring.app.dao.impl;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.messages.Dialog;
import com.vk.api.sdk.objects.messages.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import spring.app.service.abstraction.VkService;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@PropertySource("classpath:vkconfig.properties")
public class VkServiceImpl implements VkService {
    private final static Logger log = LoggerFactory.getLogger(VkServiceImpl.class);
    @Value("${group_id}")
    private int groupID;
    @Value("${access_token}")
    private String accessToken;
    private VkApiClient apiClient;
    private GroupActor groupActor;


    @PostConstruct
    public void init() {
        this.groupActor = new GroupActor(groupID, accessToken);
        this.apiClient = new VkApiClient(HttpTransportClient.getInstance());
    }

    @Override
    public List<Message> getMessages() {
        List<Message> result = new ArrayList<>();
        try {
            List<Dialog> dialogs = apiClient.messages().getDialogs(groupActor).unanswered1(true).execute().getItems();
            for (Dialog item : dialogs) {
                result.add(item.getMessage());
            }
        } catch (ApiException | ClientException e) {
            log.error("Ошибка получении сообщений", e);
        }
        return result;
    }

    @Override
    public void sendMessage(String text, String keyboard, Integer userId) {
        Random random = new Random();
        try {
            this.apiClient.messages()
                    .send(groupActor)
                    .message(text)
                    .unsafeParam("keyboard", keyboard)
                    .userId(userId).randomId(random.nextInt())
                    .execute();
        } catch (ApiException | ClientException e) {
            log.error("Исключение при отправке сообщения", e);
        }
    }
}

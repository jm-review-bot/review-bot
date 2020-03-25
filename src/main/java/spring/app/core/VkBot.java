package spring.app.core;

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
import org.springframework.stereotype.Component;
import spring.app.service.abstraction.UserService;
import spring.app.util.Keyboards;
import spring.app.util.StringParser;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

@Component
@PropertySource("classpath:vkconfig.properties")
public class VkBot implements ChatBot {
    private final static Logger log = LoggerFactory.getLogger(VkBot.class);
    private final VkApiClient apiClient = new VkApiClient(HttpTransportClient.getInstance());
    private GroupActor groupActor;
    private UserService userService;
    @Value("${group_id}")
    private int groupID;
    @Value("${access_token}")
    private String accessToken;
    private Properties chatProperties = new Properties();


    public VkBot(UserService userService) {
        this.userService = userService;
    }

    // читаем chat.properties
    @PostConstruct
    public void init() throws IOException {
        this.groupActor = new GroupActor(groupID, accessToken);
        InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream("chat.properties");
        assert stream != null;
        Reader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
        chatProperties.load(reader);
        stream.close();
    }

    @Override
    public List<Message> readMessages() {
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
    public void replyForMessages(List<Message> messages) {
        String body;
        Integer userId;

        for (Message message : messages) {
            body = message.getBody();
            userId = message.getUserId();

            userService.getByVkId();

            //----------------
            if (body.equals("Начать")){
                // отправить пользователю две кнопки "Ответить на вопросы" и "Посмотреть результаты"
                sendMessage("Привет! Выбери один из вариантов", Keyboards.defaultKeyboard, message.getUserId());
                return;
            }
            if (body.equals("Пройти опрос")) {
                sendMessage("Укажите ваш возраст.", message.getUserId());
                return;
            }
            if (StringParser.isNumeric(body)) {
                if (Integer.parseInt(body) < 100) {
                    if (Integer.parseInt(body) < 4) {
                        // ответ на опрос
                        sendMessage("Спасибо за участие в опросе!", message.getUserId());
                        return;
                    }
                    // сохранить возраст
//                    userId = message.getUserId();
//                    User user = userService.getById()
//                    userService.add(new User(userId,));
                    sendMessage(poll, message.getUserId());
                    return;

                }
            }
            if (body.equals("Посмотреть результаты")) {
               // возврат из БД
                sendMessage("Результаты из БД:", message.getUserId());
                return;

            }

            sendMessage("Команда не распознана:" + message.getBody(), Keyboards.startKeyboard, message.getUserId());
        }
    }

    @Override
    public void sendMessage(String message, Integer userId) {
        Random random = new Random();
        try {
            this.apiClient.messages()
                    .send(groupActor)
                    .message(message)
                    .userId(userId)
                    .randomId(random.nextInt())
                    .execute();
        } catch (ApiException | ClientException e) {
            log.error("Исключение при отправке сообщения", e);
        }
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

    String poll = "Выберите интересную вам тему:\n" +
            "[1] Туризм и путешествия\n" +
            "[2] Программирование и cs\n" +
            "[3] Финансы и бизнес ";
}
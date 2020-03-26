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
import spring.app.model.User;
import spring.app.service.abstraction.RoleService;
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
    private RoleService roleService;
    @Value("${group_id}")
    private int groupID;
    @Value("${access_token}")
    private String accessToken;
    private Properties chatProperties = new Properties();


    public VkBot(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
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
                System.out.println("-----------");
                System.out.println(item.getMessage().getUserId());
                System.out.println("-----------");
                result.add(item.getMessage());
            }
        } catch (ApiException | ClientException e) {
            log.error("Ошибка получении сообщений", e);
        }
        return result;
    }


    @Override
    public void replyForMessages(List<Message> messages) {
        BotContext context;
        String input;
        Integer userVkId;
        String userState;
        BotState state;

        for (Message message : messages) {
            input = message.getBody();
            userVkId = message.getUserId();
            // проверяем есть ли юзер у нас в БД
            User user = userService.getByVkId(userVkId);
            // Если нет - добавляем нового юзера в БД и присваиваем ему стейт Start и роль User
            // TODO ту логику потом нужно доработать под нужды ТЗ
            if (user == null) {
                user = new User(
                        "Роман",
                        "Евсеев",
                        userVkId,
                        "Start",
                        roleService.getRoleByName("USER"));
                userService.addUser(user);
            }
            context = new BotContext(this, userVkId, input);
            // выясняем стейт в котором находится User
            userState = user.getChatState();
            System.out.println("-----------");
            System.out.println(userState);
            System.out.println("-----------");
            state = BotState.valueOf(userState);
            // заходим в этот контекст
            state.enter(context);

            do {
                state.processInput(context);
                state = state.nextState();
//                state.enter(context);
            }
            while (!state.isInputNeeded()); //true

            user.setChatState(state.name());
            // viewed false
            userService.updateUser(user);
            //----------------
//            if (body.equals("Начать")){
//                // отправить пользователю две кнопки "Ответить на вопросы" и "Посмотреть результаты"
//                sendMessage("Привет! Выбери один из вариантов", Keyboards.defaultKeyboard, message.getUserId());
//                return;
//            }
//            if (body.equals("Пройти опрос")) {
//                sendMessage("Укажите ваш возраст.", message.getUserId());
//                return;
//            }
//            if (StringParser.isNumeric(body)) {
//                if (Integer.parseInt(body) < 100) {
//                    if (Integer.parseInt(body) < 4) {
//                        // ответ на опрос
//                        sendMessage("Спасибо за участие в опросе!", message.getUserId());
//                        return;
//                    }
//                    // сохранить возраст
////                    userId = message.getUserId();
////                    User user = userService.getById()
////                    userService.add(new User(userId,));
//                    sendMessage(poll, message.getUserId());
//                    return;
//
//                }
//            }
//            if (body.equals("Посмотреть результаты")) {
//               // возврат из БД
//                sendMessage("Результаты из БД:", message.getUserId());
//                return;
//            sendMessage("Команда не распознана:" + message.getBody(), Keyboards.startKeyboard, message.getUserId());
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
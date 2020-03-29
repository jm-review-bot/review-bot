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
import spring.app.core.steps.Step;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.Role;
import spring.app.model.User;
import spring.app.service.abstraction.ReviewService;
import spring.app.service.abstraction.RoleService;
import spring.app.service.abstraction.ThemeService;
import spring.app.service.abstraction.UserService;

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
    private ThemeService themeService;
    private ReviewService reviewService;
    private StepHolder stepHolder;
    @Value("${group_id}")
    private int groupID;
    @Value("${access_token}")
    private String accessToken;
    private Properties chatProperties = new Properties();


    public VkBot(UserService userService, RoleService roleService, ThemeService themeService, ReviewService reviewService, StepHolder stepHolder) {
        this.userService = userService;
        this.roleService = roleService;
        this.stepHolder = stepHolder;
        this.themeService = themeService;
        this.reviewService = reviewService;
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

    /**
     * Получаем сообщение из вк
     * Берем vkid пользователя
     * Достаём самого пользователя
     * Получаем у пользователя step
     * Получаем is_viewed
     * Если is_viewed == false то вызываем у curretStep.getText, curretStep.getKeyBoard и отдаем вк то что получили из step, пользователю ставим user.setStep(curretStep), user.setViewed(true) и выходим из цикла
     * Если is_viewed == true, то берем step.proccessInput, затем мы берем getNextStep и user.setStep(nextStep), user.isViewed(false) и выходим из цикла
     * Если мы словили ProccessInputException, то берем текст сообщения и отправляем его пользовтаелю. Пользователь остается на этом же шаге с тем же состоянием view
     */
    @Override
    public void replyForMessages(List<Message> messages) {
        BotContext context;
        Integer userVkId;
        String input;
        String userStep;
        boolean isViewed;
        Step currentStep;

//            Получаем сообщение из вк
        for (Message message : messages) {
//            Берем vkid пользователя
            userVkId = message.getUserId();
            input = message.getBody();
            // проверяем есть ли юзер у нас в БД
            User user = userService.getByVkId(userVkId);
            // Если нет - добавляем нового юзера в БД и присваиваем ему стейт Start и роль User
            // TODO эту логику потом нужно доработать под нужды ТЗ
            if (user == null) {
                user = new User(
                        "левый",
                        "юзер",
                        userVkId,
                        "Start",
                        roleService.getRoleByName("USER"));
                userService.addUser(user);
            }
            Role role = user.getRole();
            context = new BotContext(userVkId, input, role, userService, themeService, reviewService);
            // выясняем степ в котором находится User
            userStep = user.getChatStep();
            // видел ли User этот шаг
            isViewed = user.isViewed();
            log.debug("VK_ID юзера: {}, роль: {},  шаг: {}, ранее просмотрен: {}", userVkId, role.getName(),
                    userStep, isViewed);
            currentStep = stepHolder.getSteps().get(StepSelector.valueOf(userStep));

            if (!isViewed) {
                // если шаг не просмотрен, заходим в этот контекст и отправляем первое сообщение шага
                currentStep.enter(context);
                sendMessage(currentStep.getText(), currentStep.getKeyboard(), userVkId);
                // юзеру ставим флаг просмотра true
                user.setViewed(true);
            } else { // если шаг просмотрен, то обрабатываем его инпут
                try {
                    currentStep.processInput(context);
                    // меняем юзеру на следующий шаг только, если не выпало исключения
                    StepSelector nextStep = currentStep.nextStep();
                    user.setChatStep(nextStep.name());
                    user.setViewed(false);
                } catch (ProcessInputException e) {
                    // отправляем сообщение об ошибке ввода
                    sendMessage(e.getMessage(), currentStep.getKeyboard(), userVkId);
                }
            }
            // Юзеру сеттим следующий шаг и меняем флаг просмотра на противоположный
            userService.updateUser(user);
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
}
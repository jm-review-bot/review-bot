package spring.app.core;

import com.vk.api.sdk.objects.messages.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import spring.app.core.abstraction.ChatBot;
import spring.app.core.steps.Step;
import spring.app.exceptions.NoNumbersEnteredException;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.Role;
import spring.app.model.User;
import spring.app.service.abstraction.ReviewService;
import spring.app.service.abstraction.RoleService;
import spring.app.service.abstraction.ThemeService;
import spring.app.service.abstraction.UserService;
import spring.app.service.abstraction.VkService;
import spring.app.util.Keyboards;

import javax.persistence.NoResultException;
import java.util.List;

@Component
public class VkBot implements ChatBot {
    private final static Logger log = LoggerFactory.getLogger(VkBot.class);
    private VkService vkService;
    private UserService userService;
    private RoleService roleService;
    private ThemeService themeService;
    private ReviewService reviewService;
    private StepHolder stepHolder;


    public VkBot(ThemeService themeService, ReviewService reviewService, VkService vkService, UserService userService, RoleService roleService, StepHolder stepHolder) {
        this.vkService = vkService;
        this.userService = userService;
        this.roleService = roleService;
        this.stepHolder = stepHolder;
        this.reviewService = reviewService;
        this.themeService = themeService;
    }

    @Override
    public List<Message> readMessages() {
        return vkService.getMessages();
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

        // Получаем сообщение из вк
        for (Message message : messages) {
            // Берем vkid пользователя
            userVkId = message.getUserId();
            input = message.getBody();
            User user;
            // проверяем есть ли юзер у нас в БД, если нет, получаем исключение и отправляем Юзеру сообщение и выходим из цикла
            try {
                user = userService.getByVkId(userVkId);
            } catch (NoResultException e) {
                log.warn("Пришло сообщение от незарегистрированного пользователя c vkId: {}", userVkId);
                sendMessage("Пользователь с таким vkId не найден в базе. Обратитесь к Герману Севостьянову или Станиславу Сорокину\n", Keyboards.NO_KB, userVkId);
                return;
            }

            Role role = user.getRole();
            context = new BotContext(user, userVkId, input, role, userService, themeService, reviewService, roleService, vkService);
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
                    // Юзеру сеттим следующий шаг и меняем флаг просмотра на противоположный
                    user.setChatStep(nextStep.name());
                    user.setViewed(false);
                } catch (ProcessInputException | NoNumbersEnteredException e) {
                    // отправляем сообщение об ошибке ввода
                    log.info("Пользователь с vkId: {} ввел неверные данные", userVkId);
                    sendMessage(e.getMessage(), currentStep.getKeyboard(), userVkId);
                }
            }
            // сохраняем изменения
            userService.updateUser(user);
        }
    }

    @Override
    public void sendMessage(String text, String keyboard, Integer userId) {
        vkService.sendMessage(text, keyboard, userId);
    }
}
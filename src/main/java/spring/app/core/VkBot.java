package spring.app.core;

import com.vk.api.sdk.objects.messages.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import spring.app.core.abstraction.ChatBot;
import spring.app.core.steps.Step;
import spring.app.exceptions.NoDataEnteredException;
import spring.app.exceptions.NoNumbersEnteredException;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.Role;
import spring.app.model.User;
import spring.app.service.abstraction.*;
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
    private QuestionService questionService;
    private StepHolder stepHolder;
    private StudentReviewService studentReviewService;
    private StudentReviewAnswerService studentReviewAnswerService;
    private StorageService storageService;


    public VkBot(ThemeService themeService, ReviewService reviewService, VkService vkService, UserService userService, RoleService roleService, QuestionService questionService, StepHolder stepHolder, StudentReviewAnswerService studentReviewAnswerService, StudentReviewService studentReviewService, StorageService storageService) {
        this.vkService = vkService;
        this.userService = userService;
        this.roleService = roleService;
        this.stepHolder = stepHolder;
        this.reviewService = reviewService;
        this.themeService = themeService;
        this.questionService = questionService;
        this.studentReviewService = studentReviewService;
        this.studentReviewAnswerService = studentReviewAnswerService;
        this.storageService = storageService;
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
        StepSelector userStep;
        boolean isViewed;
        Step currentStep;
        // Получаем сообщение из вк
        for (Message message : messages) {
            // Берем vkid пользователя
            userVkId = message.getUserId();
            // Первая проверка: Если пользователь выслал смайл, заменяем его на пустую строку
            // Вторая проверка: Если в сообщении есть вложение, проверяю на наличие ссылки на хэнгаутс
            if (message.getEmoji() == true) {
                input = "";
            } else if (message.getAttachments() != null) {
                // в качестве пользовательского ввода берем ссылку из вложения к сообщению
                if (message.getAttachments().get(0).getLink() != null) {
                    input = message.getAttachments().get(0).getLink().getUrl();
                } else {
                    input = message.getBody();
                }
            } else {
                input = message.getBody();
            }
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
            context = new BotContext(user, userVkId, input, role, userService, themeService, reviewService, roleService, vkService, questionService, stepHolder, studentReviewAnswerService, studentReviewService, storageService);
            // выясняем степ в котором находится User
            userStep = user.getChatStep();
            // видел ли User этот шаг
            isViewed = user.isViewed();
            log.debug("VK_ID юзера: {}, роль: {},  шаг: {}, ранее просмотрен: {}", userVkId, role.getName(),
                    userStep, isViewed);
            currentStep = stepHolder.getSteps().get(userStep);
            if (!isViewed) {
                // если шаг не просмотрен, заходим в этот контекст и отправляем первое сообщение шага
                currentStep.enter(context);
                sendMessage(currentStep.getText(), currentStep.getKeyboard(), userVkId);
                // подтягиваем юзера из БД, чтобы обновить РП
                user = context.getUserService().getByVkId(userVkId);
                // юзеру ставим флаг просмотра true
                user.setViewed(true);
                // сохраняем изменения
                userService.updateUser(user);
            } else { // если шаг просмотрен, то обрабатываем его инпут
                try {
                    currentStep.processInput(context);
                    // меняем юзеру на следующий шаг только, если не выпало исключения
                    StepSelector nextStep = currentStep.nextStep();
                    // Юзеру сеттим следующий шаг и меняем флаг просмотра на противоположный
                    user.setChatStep(nextStep);
                    user.setViewed(false);
                    // сохраняем изменения
                    userService.updateUser(user);
                } catch (ProcessInputException | NoNumbersEnteredException | NoDataEnteredException e) {
                    // отправляем сообщение об ошибке ввода
                    log.info("Пользователь с vkId: {} ввел неверные данные", userVkId);
                    sendMessage(e.getMessage(), currentStep.getKeyboard(), userVkId);
                }
            }
        }
    }

    @Override
    public void sendMessage(String text, String keyboard, Integer userId) {
        vkService.sendMessage(text, keyboard, userId);
    }
}
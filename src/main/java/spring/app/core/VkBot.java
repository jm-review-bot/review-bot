package spring.app.core;

import com.vk.api.sdk.objects.messages.Message;
import org.springframework.stereotype.Component;
import spring.app.core.abstraction.ChatBot;
import spring.app.core.steps.Step;
import spring.app.exceptions.NoDataEnteredException;
import spring.app.exceptions.NoNumbersEnteredException;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.Role;
import spring.app.model.User;
import spring.app.service.abstraction.UserService;
import spring.app.service.abstraction.VkService;
import spring.app.util.Keyboards;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

@Component
public class VkBot implements ChatBot {

    private VkService vkService;
    private UserService userService;
    private StepHolder stepHolder;

    public VkBot(VkService vkService, UserService userService, StepHolder stepHolder) {
        this.vkService = vkService;
        this.userService = userService;
        this.stepHolder = stepHolder;
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
            // проверяем есть ли юзер у нас в БД, если нет, получаем исключение и отправляем Юзеру сообщение и выходим из цикла
            Optional<User> optionalUser = userService.getByVkId(userVkId);
            if (!optionalUser.isPresent()) {
                sendMessage("Пользователь с таким vkId не найден в базе. Обратитесь к Герману Севостьянову или Станиславу Сорокину\n", Keyboards.NO_KB, userVkId);
                return;
            }
            User user = optionalUser.get();
            Role role = user.getRole();
            context = new BotContext(user, userVkId, input, role, stepHolder);
            // выясняем степ в котором находится User
            userStep = user.getChatStep();
            // видел ли User этот шаг
            isViewed = user.isViewed();
            currentStep = stepHolder.getSteps().get(userStep);

            if (!isViewed) {
                // если шаг не просмотрен, заходим в этот контекст и отправляем первое сообщение шага
                currentStep.enter(context);
                sendMessage(currentStep.getComposeText(context), currentStep.getComposeKeyboard(context), userVkId);
                // юзеру ставим флаг просмотра true
                user.setViewed(true);
                // сохраняем изменения
                userService.updateUser(user);
            } else { // если шаг просмотрен, то обрабатываем его инпут
                try {
                    currentStep.processInput(context);
                    //меняем флаг просмотра на противоположный, если ввод оказался корректным
                    user.setViewed(false);
                    // сохраняем изменения
                    userService.updateUser(user);
                } catch (ProcessInputException | NoNumbersEnteredException | NoDataEnteredException e) {
                    // отправляем сообщение об ошибке ввода
                    sendMessage(e.getMessage(), currentStep.getComposeKeyboard(context), userVkId);
                }
            }
        }
    }

    @Override
    public void sendMessage(String text, String keyboard, Integer userId) {
        vkService.sendMessage(text, keyboard, userId);
    }
}
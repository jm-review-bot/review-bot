package spring.app.core;

import com.vk.api.sdk.objects.messages.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import spring.app.core.abstraction.ChatBot;
import spring.app.core.steps.Step;
import spring.app.model.User;
import spring.app.service.abstraction.ReviewService;
import spring.app.service.abstraction.StorageService;
import spring.app.service.abstraction.UserService;
import spring.app.service.abstraction.VkService;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class BotScheduler {
    private final VkService vkService;
    private final ReviewService reviewService;
    private final UserService userService;
    private final StepHolder stepHolder;
    private final ChatBot bot;
    private final StorageService storageService;
    private long timeCounter;

    public BotScheduler(VkService vkService, ReviewService reviewService, ChatBot bot, UserService userService, StepHolder stepHolder, StorageService storageService) {
        this.vkService = vkService;
        this.reviewService = reviewService;
        this.bot = bot;
        this.userService = userService;
        this.stepHolder = stepHolder;
        this.storageService = storageService;
    }

    @Scheduled(fixedDelayString = "${bot.operations_interval}")
    public void scheduleFixedDelayTask() {
        List<Message> messages = vkService.getMessages();
        bot.replyForMessages(messages);
    }

    @Scheduled(cron = "${bot.expired_review_check_time}")
    public void scheduleCloseExpiredReview() {
        reviewService.updateAllExpiredReviewsByDate(LocalDateTime.now());
    }

    /**
     * Бот каждую минуту проверяет время всех существующих ревью
     * и за 3 минуты сообщает принимающему о том, что ему пора начинать ревью.
     */

    @Scheduled(fixedDelayString = "${bot.review_reminder_interval}")
    public void sendReviewReminder() {

        LocalDateTime periodStart = LocalDateTime.now().plusMinutes(2).plusNanos(1);
        LocalDateTime periodEnd = LocalDateTime.now().plusMinutes(3);

        List<User> users = userService.getUsersByReviewPeriod(periodStart, periodEnd);
        if (!users.isEmpty()) {
            for (User user : users) {
                // получить текущий step пользователя, чтобы отдать ему в сообщении клавиатуру для этого step
                Step step = stepHolder.getSteps().get(user.getChatStep());
                bot.sendMessage("Напоминание! Если ты готов начать ревью, то в главном меню нажми кнопку \"Начать прием ревью\"", step.getKeyboard(), user.getVkId());
            }
        }
    }
}

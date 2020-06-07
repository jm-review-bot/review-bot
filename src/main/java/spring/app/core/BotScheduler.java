package spring.app.core;

import com.vk.api.sdk.objects.messages.Message;
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
import java.util.ArrayList;
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

    @Value("${bot.minutes_remainder_reviewers}")
    private int minutesRemainderReviewers;

    @Value("${bot.minutes_remainder_students_reviewers}")
    private int minutesRemainderStudentsReviewers;

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

        LocalDateTime periodStart = LocalDateTime.now().plusMinutes(minutesRemainderReviewers - 1);
        LocalDateTime periodEnd = LocalDateTime.now().plusMinutes(minutesRemainderReviewers);

        List<User> users = userService.getUsersByReviewPeriod(periodStart, periodEnd);
        if (!users.isEmpty()) {
            for (User user : users) {
                // получить текущий step пользователя, чтобы отдать ему в сообщении клавиатуру для этого step
                Step step = stepHolder.getSteps().get(user.getChatStep());
                bot.sendMessage("Напоминание! Если ты готов начать ревью, то в главном меню нажми кнопку \"Начать прием ревью\"", step.getKeyboard(), user.getVkId());
            }
        }
    }

    @Scheduled(fixedDelayString = "${bot.review_reminder_interval}")
    public void sendReviewHourReminder() {

        LocalDateTime periodStart = LocalDateTime.now().plusMinutes(minutesRemainderStudentsReviewers - 1);
        LocalDateTime periodEnd = LocalDateTime.now().plusMinutes(minutesRemainderStudentsReviewers);

        List<User> reviewers = userService.getUsersByReviewPeriod(periodStart, periodEnd);
        List<User> students = userService.getStudentsByReviewPeriod(periodStart, periodEnd);

        List<User> users = new ArrayList<>();
        users.addAll(reviewers);
        users.addAll(students);

        if (!users.isEmpty()) {
            for (User user : users) {
                // получить текущий step пользователя, чтобы отдать ему в сообщении клавиатуру для этого step
                Step step = stepHolder.getSteps().get(user.getChatStep());
                bot.sendMessage("Напоминание! Через час у тебя ревью.", step.getKeyboard(), user.getVkId());
            }
        }
    }

    // задание для полного очищения кэша в заданное время
    @Scheduled(cron = "${bot.clear_cache}")
    public void scheduleClearCache() {
        storageService.clearStorage();
    }
}

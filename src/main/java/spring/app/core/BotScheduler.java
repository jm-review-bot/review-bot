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
import spring.app.service.abstraction.UserService;
import spring.app.service.abstraction.VkService;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class BotScheduler {
    private final static Logger log = LoggerFactory.getLogger(BotScheduler.class);
    private final VkService vkService;
    private final ReviewService reviewService;
    private final UserService userService;
    private final StepHolder stepHolder;
    private final ChatBot bot;
    private long timeCounter;

    public BotScheduler(VkService vkService, ReviewService reviewService, ChatBot bot, UserService userService, StepHolder stepHolder) {
        this.vkService = vkService;
        this.reviewService = reviewService;
        this.bot = bot;
        this.userService = userService;
        this.stepHolder = stepHolder;
    }

    @Scheduled(fixedDelayString = "${bot.operations_interval}")
    public void scheduleFixedDelayTask() {
        log.trace("Бот работает уже " + (timeCounter++) + " с.");
        List<Message> messages = vkService.getMessages();
        bot.replyForMessages(messages);
    }

    @Scheduled(cron = "${bot.expired_review_check_time}")
    public void scheduleCloseExpiredReview() {
        log.info("Скрипт запущен. Началась проверка просроченных ревью.");
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
                Step step = stepHolder.getSteps().get(StepSelector.valueOf(user.getChatStep()));
                //todo пришли сообщение \"начать ревью\" в чат"
                // разобраться с этим, либо написать сообщение о переходе в главное меню по команде /старт и нажания на кнопку начать ревью
                // либо на каждом шаге реализовать эту команду
                bot.sendMessage("Напоминание! Если ты готов начать ревью пришли сообщение \"начать ревью\" в чат", step.getKeyboard(), user.getVkId());
                log.debug("В {} пользователю с id {} отправлено напоминание о ревью.", LocalDateTime.now(), user.getVkId());
            }
        }
    }
}

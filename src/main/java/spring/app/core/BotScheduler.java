package spring.app.core;

import com.vk.api.sdk.objects.messages.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import spring.app.service.abstraction.ReviewService;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class BotScheduler {
    private final static Logger log = LoggerFactory.getLogger(BotScheduler.class);
    private final ChatBot bot;
    private long timeCounter;

    @Autowired
    private ReviewService reviewService;

    public BotScheduler(ChatBot bot) {
        this.bot = bot;
    }

    @Scheduled(fixedDelayString = "${bot.operations_interval}")
    public void scheduleFixedDelayTask() {
        log.trace("Бот работает уже " + (timeCounter++) + " с.");
        List<Message> messages = bot.readMessages();
        bot.replyForMessages(messages);
    }

    @Scheduled(cron = "${bot.expired_review_check_time}")
    public void scheduleCloseExpiredReview() {
        log.info("Скрипт запущен. Началась проверка просроченных ревью.");
        reviewService.updateAllExpiredReviewsBy(LocalDateTime.now());
    }

}

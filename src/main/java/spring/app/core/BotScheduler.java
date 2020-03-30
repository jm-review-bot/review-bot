package spring.app.core;

import com.vk.api.sdk.objects.messages.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import spring.app.core.abstraction.ChatBot;
import spring.app.service.abstraction.VkService;
import spring.app.service.abstraction.ReviewService;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class BotScheduler {
    private final static Logger log = LoggerFactory.getLogger(BotScheduler.class);
    private final VkService vkService;
    private ReviewService reviewService;
    private final ChatBot bot;
    private long timeCounter;

    public BotScheduler(VkService vkService, ReviewService reviewService, ChatBot bot){
        this.vkService = vkService;
        this.reviewService = reviewService;
        this.bot = bot;
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
        reviewService.updateAllExpiredReviewsBy(LocalDateTime.now());
    }

}

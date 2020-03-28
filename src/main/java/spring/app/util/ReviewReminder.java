package spring.app.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import spring.app.core.ChatBot;
import spring.app.core.StepHolder;
import spring.app.core.StepSelector;
import spring.app.core.steps.Step;
import spring.app.model.User;
import spring.app.service.abstraction.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ReviewReminder {

    private final ChatBot bot;
    private UserService userService;
    private StepHolder stepHolder;
    private final static Logger log = LoggerFactory.getLogger(ReviewReminder.class);

    @Autowired
    public ReviewReminder(ChatBot bot, UserService userService, StepHolder stepHolder) {
        this.bot = bot;
        this.userService = userService;
        this.stepHolder = stepHolder;
    }

    @Scheduled(fixedDelayString = "60000")
    public void sendReviewReminder() {

        LocalDateTime periodStart = LocalDateTime.now().plusMinutes(2).plusNanos(1);
        LocalDateTime periodEnd = LocalDateTime.now().plusMinutes(3);

        List<User> users = userService.getUsersByReviewPeriod(periodStart, periodEnd);
        if (!users.isEmpty()) {
            for (User user : users) {
                // получить текущий step пользователя, чтобы отдать ему в сообщении клавиатуру для этого step
                Step step = stepHolder.getSteps().get(StepSelector.valueOf(user.getChatStep()));
                bot.sendMessage(user.getFirstName() + ", пора начинать ревью!", step.getKeyboard(), user.getVkId());
                log.debug("В {} пользователю с id {} отправлено напоминание о ревью.", LocalDateTime.now(), user.getVkId());
            }
        }
    }
}

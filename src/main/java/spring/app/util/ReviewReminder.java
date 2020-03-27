package spring.app.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import spring.app.core.ChatBot;
import spring.app.model.User;
import spring.app.service.abstraction.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ReviewReminder {

    private final ChatBot bot;
    private UserService userService;

    @Autowired
    public ReviewReminder(ChatBot bot, UserService userService) {
        this.bot = bot;
        this.userService = userService;
    }

    @Scheduled(fixedDelayString = "60000")
    public void sendReviewReminder() {

        LocalDateTime periodStart = LocalDateTime.now().plusMinutes(2).minusSeconds(1);
        LocalDateTime periodEnd = LocalDateTime.now().plusMinutes(3).minusSeconds(0);

        List<User> users = userService.getUsersByReviewPeriod(periodStart, periodEnd);
        if (!users.isEmpty()) {
            for (User user : users) {
                bot.sendMessage(user.getFirstName() + ", пора начинать ревью!", Integer.parseInt(user.getVkId()));
            }
        }
    }
}

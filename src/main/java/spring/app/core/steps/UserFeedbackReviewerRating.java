package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.core.StepSelector;
import spring.app.service.abstraction.StorageService;

import java.util.Arrays;

@Component
public class UserFeedbackReviewerRating extends UserBaseFeedbackStep {

    private final StorageService storageService;

    public UserFeedbackReviewerRating(StorageService storageService) {
        super();
        this.storageService = storageService;
    }

    @Override
    public void ratingHandler(String input, BotContext context) {
        storageService.updateUserStorage(context.getVkId(), StepSelector.USER_FEEDBACK_REVIEWER_RATING, Arrays.asList(input));
        sendUserToNextStep(context, StepSelector.USER_FEEDBACK_COMMENT);
    }

    @Override
    public String getDynamicText(BotContext context) {
        return "Оцените насколько объективен и корректен был принимающий от " + getBoundsString();
    }
}

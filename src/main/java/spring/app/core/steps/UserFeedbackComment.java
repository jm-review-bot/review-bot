package spring.app.core.steps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.NoDataEnteredException;
import spring.app.exceptions.NoNumbersEnteredException;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.Feedback;
import spring.app.service.abstraction.FeedbackService;
import spring.app.service.abstraction.StorageService;
import spring.app.service.abstraction.StudentReviewService;
import spring.app.util.StringParser;

import static spring.app.core.StepSelector.USER_FEEDBACK_REVIEWER_RATING;
import static spring.app.core.StepSelector.USER_FEEDBACK_REVIEW_RATING;
import static spring.app.core.StepSelector.USER_FEEDBACK_CONFIRMATION;
import static spring.app.core.StepSelector.USER_MENU;
import static spring.app.util.Keyboards.USER_FEEDBACK_ENDING_KB;

@Component
public class UserFeedbackComment extends Step {

    private final FeedbackService feedbackService;
    private final StorageService storageService;
    private final StudentReviewService studentReviewService;

    @Autowired
    public UserFeedbackComment(FeedbackService feedbackService, StorageService storageService,
                               StudentReviewService studentReviewService) {
        super("Дайте ваш развернутый комментарий, замечания, предложения (необязательно). " +
                "Если не хотите заполнять это поле - нажмите кнопку \"Закончить\".", USER_FEEDBACK_ENDING_KB);
        this.feedbackService = feedbackService;
        this.storageService = storageService;
        this.studentReviewService = studentReviewService;
    }

    @Override
    public void enter(BotContext context) {
    }

    @Override
    public void processInput(BotContext context)
            throws ProcessInputException, NoNumbersEnteredException, NoDataEnteredException {

        String command = StringParser.toWordsArray(context.getInput())[0];
        Feedback userFeedback = new Feedback();

        if (!"закончить".equals(command)) {
            userFeedback.setComment(context.getInput());
        }

        // добавляем в бд
        userFeedback.setUser(context.getUser());
        userFeedback.setRatingReview(Integer.valueOf(storageService
                .getUserStorage(context.getVkId(), USER_FEEDBACK_REVIEW_RATING).get(0)));

        userFeedback.setRatingReviewer(Integer.valueOf(storageService
                .getUserStorage(context.getVkId(), USER_FEEDBACK_REVIEWER_RATING).get(0)));

        userFeedback.setStudentReview(studentReviewService
                .getStudentReviewById(Long.valueOf(storageService.getUserStorage(context.getVkId(),
                        USER_FEEDBACK_CONFIRMATION).get(0))));
        feedbackService.addFeedback(userFeedback);
        // очищаем storageService
        storageService.removeUserStorage(context.getVkId(), USER_FEEDBACK_REVIEW_RATING);
        storageService.removeUserStorage(context.getVkId(), USER_FEEDBACK_REVIEWER_RATING);
        storageService.removeUserStorage(context.getVkId(), USER_FEEDBACK_CONFIRMATION);

        sendUserToNextStep(context, USER_MENU);
    }

    @Override
    public String getDynamicText(BotContext context) {
        return "";
    }

    @Override
    public String getDynamicKeyboard(BotContext context) {
        return "";
    }
}

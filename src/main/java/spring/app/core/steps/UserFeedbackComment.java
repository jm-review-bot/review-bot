package spring.app.core.steps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.NoDataEnteredException;
import spring.app.exceptions.NoNumbersEnteredException;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.Feedback;
import spring.app.service.abstraction.FeedbackService;
import spring.app.util.StringParser;

import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.USER_FEEDBACK_ENDING_KB;

@Component
public class UserFeedbackComment extends Step {

    @Autowired
    private FeedbackService feedbackService;

    private Feedback addFeedback = new Feedback();

    public UserFeedbackComment() {
        super("Дайте ваш развернутый комментарий, замечания, предложения (необязательно). " +
                "Если не хотите заполнять это поле - нажмите кнопку 'Главное меню'.", USER_FEEDBACK_ENDING_KB);
    }

    @Override
    public void enter(BotContext context) {
    }

    @Override
    public void processInput(BotContext context)
            throws ProcessInputException, NoNumbersEnteredException, NoDataEnteredException {

        String command = StringParser.toWordsArray(context.getInput())[0];

        if (!"закончить".equals(command)) {
            addFeedback.setComment(context.getInput());
        }

        //добавляем в бд
        addFeedback.setUser(context.getUser());

        addFeedback.setRatingReview(Integer.valueOf(context.getStorageService()
                .getUserStorage(context.getVkId(), USER_FEEDBACK_REVIEW_ASSESSMENT).get(0)));

        addFeedback.setRatingReviewer(Integer.valueOf(context.getStorageService()
                .getUserStorage(context.getVkId(), USER_FEEDBACK_REVIEWER_ASSESSMENT).get(0)));

        addFeedback.setStudentReview(context.getStudentReviewService()
                .getStudentReviewById(Long.valueOf(context.getStorageService()
                        .getUserStorage(context.getVkId(), USER_FEEDBACK_CONFIRMATION).get(0))));

        feedbackService.addFeedback(addFeedback);

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

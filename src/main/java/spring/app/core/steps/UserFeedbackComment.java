package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.NoDataEnteredException;
import spring.app.exceptions.NoNumbersEnteredException;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.Feedback;
import spring.app.util.StringParser;

import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.USER_MENU_KB;

@Component
public class UserFeedbackComment extends Step {

    @Override
    public void enter(BotContext context) {

        text = "Дайте ваш развернутый комментарий, замечания, предложения (необязательно). " +
                "Если не хотите заполнять это поле - нажмите кнопку 'Главное меню'.";

        keyboard = USER_MENU_KB;
    }

    @Override
    public void processInput(BotContext context)
            throws ProcessInputException, NoNumbersEnteredException, NoDataEnteredException {

        Feedback newFeedback = new Feedback();
        String command = StringParser.toWordsArray(context.getInput())[0];

        if (!"главное".equals(command)) {
            newFeedback.setComment(context.getInput());
        }

        //добавляем в бд
        newFeedback.setUser(context.getUser());

        newFeedback.setRatingReview(Integer.valueOf(context.getStorageService()
                .getUserStorage(context.getVkId(), USER_FEEDBACK_REVIEW_ASSESSMENT).get(0)));

        newFeedback.setRatingReviewer(Integer.valueOf(context.getStorageService()
                .getUserStorage(context.getVkId(), USER_FEEDBACK_REVIEWER_ASSESSMENT).get(0)));

        newFeedback.setStudentReview(context.getStudentReviewService()
                .getStudentReviewById(Long.valueOf(context.getStorageService()
                        .getUserStorage(context.getVkId(), USER_FEEDBACK_CONFIRMATION).get(0))));

        context.getFeedbackService().addFeedback(newFeedback);

        //clear cash
        context.getStorageService().removeUserStorage(context.getVkId(), USER_FEEDBACK_REVIEW_ASSESSMENT);
        context.getStorageService().removeUserStorage(context.getVkId(), USER_FEEDBACK_REVIEWER_ASSESSMENT);
        context.getStorageService().removeUserStorage(context.getVkId(), USER_FEEDBACK_CONFIRMATION);

        nextStep = USER_MENU;
    }
}

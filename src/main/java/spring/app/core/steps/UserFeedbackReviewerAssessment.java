package spring.app.core.steps;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.NoDataEnteredException;
import spring.app.exceptions.NoNumbersEnteredException;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.Feedback;

import static spring.app.core.StepSelector.USER_FEEDBACK_COMMENT;
import static spring.app.util.Keyboards.NO_KB;

@Component
public class UserFeedbackReviewerAssessment extends Step {

    @Value("${lower.bound}")
    int lowBound;

    @Value("${upper.bound}")
    int upBound;

    @Override
    public void enter(BotContext context) {
        text = "Оцените насколько объективен и корректен был принимающий " + lowBound + " до " + upBound + "?";
        keyboard = NO_KB;
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException, NoNumbersEnteredException, NoDataEnteredException {

        Feedback newFeedback = new Feedback();


        try {
            int userAssessment = Integer.parseInt(context.getInput());
            if ((userAssessment >= lowBound) && (userAssessment <= upBound)) {
                newFeedback.setRatingReviewer(context.getInput());
                nextStep = USER_FEEDBACK_COMMENT;
            }
        } catch (NumberFormatException e) {
            throw new NoNumbersEnteredException("Некорректный ввод, введите оценку в диапазоне от " + lowBound + " до " + upBound + " числом!");
        }
    }
}

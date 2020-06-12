package spring.app.core.steps;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.NoDataEnteredException;
import spring.app.exceptions.NoNumbersEnteredException;
import spring.app.exceptions.ProcessInputException;
import spring.app.service.abstraction.StorageService;
import spring.app.util.StringParser;

import java.util.Arrays;

import static spring.app.core.StepSelector.USER_FEEDBACK_REVIEWER_ASSESSMENT;
import static spring.app.core.StepSelector.USER_FEEDBACK_REVIEW_ASSESSMENT;

@Component
public class UserFeedbackReviewAssessment extends Step {

    @Value("${lower.bound}")
    private int lowBound;

    @Value("${upper.bound}")
    private int upBound;

    public UserFeedbackReviewAssessment() {
        super("", "");
    }

    @Override
    public void enter(BotContext context) {
    }

    @Override
    public void processInput(BotContext context)
            throws ProcessInputException, NoNumbersEnteredException, NoDataEnteredException {

        StorageService storageService = context.getStorageService();
        String currentInput = context.getInput();

        if (StringParser.isNumeric(currentInput)) {
            Integer userAssessment = Integer.parseInt(currentInput);

            if ((userAssessment >= lowBound) && (userAssessment <= upBound)) {
                storageService.updateUserStorage
                        (context.getVkId(), USER_FEEDBACK_REVIEW_ASSESSMENT, Arrays.asList(currentInput));

                sendUserToNextStep(context, USER_FEEDBACK_REVIEWER_ASSESSMENT);
            } else {
                throw new NoNumbersEnteredException("Некорректный ввод, введите оценку в диапазоне от " + lowBound +
                        " до " + upBound + " числом!");
            }
        } else {
            throw new NoNumbersEnteredException("Введите числовое значение!");
        }
    }

    @Override
    public String getDynamicText(BotContext context) {
        return "Оцените насколько для вас было полезным сдача ревью от " + lowBound + " до " + upBound + "?";
    }

    @Override
    public String getDynamicKeyboard(BotContext context) {
        return "";
    }
}

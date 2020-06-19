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
import java.util.List;

import static spring.app.core.StepSelector.*;

@Component
public class UserFeedbackAssessment extends Step {

    private final StorageService storageService;
    @Value("${lower.bound}")
    private int lowBound;
    @Value("${upper.bound}")
    private int upBound;

    public UserFeedbackAssessment(StorageService storageService) {
        super("", "");
        this.storageService = storageService;
    }

    @Override
    public void enter(BotContext context) {
    }

    @Override
    public void processInput(BotContext context)
            throws ProcessInputException, NoNumbersEnteredException, NoDataEnteredException {

        String currentInput = context.getInput();

        if (StringParser.isNumeric(currentInput)) {
            Integer userAssessment = Integer.parseInt(currentInput);
            // проверяем принадлежит ли число указанному интервалу
            if ((userAssessment >= lowBound) && (userAssessment <= upBound)) {
                // проверяем существует ли фидбэк о ревью
                if (storageService.getUserStorage(context.getVkId(), USER_FEEDBACK_ASSESSMENT) == null) {
                    storageService.updateUserStorage
                            (context.getVkId(), USER_FEEDBACK_ASSESSMENT, Arrays.asList(currentInput));
                }
                else {
                    String reviewFeedback = storageService.getUserStorage(context.getVkId(), USER_FEEDBACK_ASSESSMENT).get(0);
                    storageService.updateUserStorage
                            (context.getVkId(), USER_FEEDBACK_ASSESSMENT, Arrays.asList(reviewFeedback, currentInput));
                    sendUserToNextStep(context, USER_FEEDBACK_COMMENT);
                }
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

        if (storageService.getUserStorage(context.getVkId(), USER_FEEDBACK_ASSESSMENT) == null) {
            return "Оцените насколько для вас было полезным сдача ревью от " + lowBound + " до " + upBound + "?";
        }
        else {
            return "Оцените насколько объективен и корректен был принимающий от " + lowBound + " до " + upBound + "?";
        }
    }

    @Override
    public String getDynamicKeyboard(BotContext context) {
        return "";
    }
}

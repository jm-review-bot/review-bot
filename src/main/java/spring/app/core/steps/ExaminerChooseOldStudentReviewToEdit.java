package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.NoDataEnteredException;
import spring.app.exceptions.NoNumbersEnteredException;
import spring.app.exceptions.ProcessInputException;
import spring.app.service.abstraction.StorageService;

import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.*;

@Component
public class ExaminerChooseOldStudentReviewToEdit extends Step {

    StorageService storageService;

    public ExaminerChooseOldStudentReviewToEdit(StorageService storageService) {
        super("Данный функционал ещё находится в разработке", DEF_BACK_KB);
        this.storageService = storageService;
    }

    @Override
    public void enter(BotContext context) {
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException, NoNumbersEnteredException, NoDataEnteredException {
        String command = context.getInput();
        Integer examinerVkId = context.getVkId();

        // Обрабатываются команды пользователя
        if (command.equalsIgnoreCase("назад")) {
            sendUserToNextStep(context, EXAMINER_GET_INFO_LAST_REVIEW);
            storageService.removeUserStorage(examinerVkId, EXAMINER_CHOOSE_OLD_STUDENT_REVIEW_TO_EDIT);
        } else {
            throw new ProcessInputException("Введена неверная команда...");
        }
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

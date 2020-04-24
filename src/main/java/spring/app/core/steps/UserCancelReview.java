package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.Review;
import spring.app.service.abstraction.StorageService;
import spring.app.util.StringParser;

import java.util.Arrays;
import java.util.List;

import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.USER_MENU_KB;
import static spring.app.util.Keyboards.YES_NO_KB;


@Component
public class UserCancelReview extends Step {

    @Override
    public void enter(BotContext context) {
        //======
        System.out.println("BEGIN_STEP::"+"UserCancelReview");
        //======
        Integer vkId = context.getVkId();
        StorageService storageService = context.getStorageService();
        List<String> savedInput = storageService.getUserStorage(vkId, USER_CANCEL_REVIEW);

        if (savedInput == null || savedInput.isEmpty()) {
            Review review = context.getReviewService().getOpenReviewByStudentVkId(vkId);

            // показываем юзеру ревью, на которое он записан, Тему, дату и время
            StringBuilder message = new StringBuilder("Вы записаны на ревью:\n");
            message.append("Тема: ")
                    .append(review.getTheme().getTitle())
                    .append(".\n")
                    .append("Дата: ")
                    .append(StringParser.localDateTimeToString(review.getDate()))
                    .append(". \n\n")
                    .append("Вы действительно хотите отменить запись? (Да/Нет)");
            text = message.toString();
            // показываем клаву c кнопками Да и Нет
            keyboard = YES_NO_KB;
        } else {
            // если зафиксирован инпут на этом шаге, и это "да"
            // говорим, что запись на данное ревью удалена
            text = savedInput.get(0);
            keyboard = USER_MENU_KB;
        }
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException {
        Integer vkId = context.getVkId();
        String currentInput = context.getInput();
        StorageService storageService = context.getStorageService();
        // на этом шаге мы ждем либо "Да" либо "Нет"
        // + стандартные команды на выход в начало или назад

        String wordInput = StringParser.toWordsArray(currentInput)[0];
        if (storageService.getUserStorage(vkId, USER_CANCEL_REVIEW) == null) {
            if (wordInput.equals("нет")) {
                storageService.removeUserStorage(vkId, USER_CANCEL_REVIEW);
                nextStep = USER_MENU;
            } else if (wordInput.equals("/start")) {
                storageService.removeUserStorage(vkId, USER_CANCEL_REVIEW);
                nextStep = START;
            } else if (wordInput.equals("да")) {
                context.getStudentReviewService().deleteStudentReviewByVkId(vkId);
                List<String> savedMessage = Arrays.asList("Запись на ревью была удалена.");
                storageService.updateUserStorage(vkId, USER_CANCEL_REVIEW, savedMessage);
                nextStep = USER_CANCEL_REVIEW;
            } else {
                throw new ProcessInputException("Введена неверная команда. Нажми \"Да\" для удаления записи на ревью или \"Нет\" для выхода в главное меню.");
            }
        } else {
            if (wordInput.equals("главное")) {
                storageService.removeUserStorage(vkId, USER_CANCEL_REVIEW);
                nextStep = USER_MENU;
            } else if (wordInput.equals("/start")) {
                storageService.removeUserStorage(vkId, USER_CANCEL_REVIEW);
                nextStep = START;
            } else {
                throw new ProcessInputException("Введена неверная команда. Для выхода нажми на кнопку \"Главное меню\"");
            }
        }
    }
}

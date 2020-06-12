package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.NoDataEnteredException;
import spring.app.exceptions.NoNumbersEnteredException;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.Review;
import spring.app.model.StudentReview;
import spring.app.service.abstraction.StorageService;
import spring.app.util.StringParser;

import java.util.Arrays;
import java.util.List;

import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.USER_MENU_KB;
import static spring.app.util.Keyboards.YES_NO_KB;


@Component
public class UserCancelReview extends Step {

    public UserCancelReview() {
        super("", "");
    }

    @Override
    public void enter(BotContext context) {

    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException, NoNumbersEnteredException, NoDataEnteredException {
        Integer vkId = context.getVkId();
        String currentInput = context.getInput();
        StorageService storageService = context.getStorageService();
        // на этом шаге мы ждем либо "Да" либо "Нет"
        // + стандартные команды на выход в начало или назад
        String wordInput = StringParser.toWordsArray(currentInput)[0];
        if (wordInput.equals("главное")) {
            storageService.removeUserStorage(vkId, USER_CANCEL_REVIEW);
            sendUserToNextStep(context, USER_MENU);
        } else if (wordInput.equals("/start")) {
            storageService.removeUserStorage(vkId, USER_CANCEL_REVIEW);
            sendUserToNextStep(context, START);
        } else if (storageService.getUserStorage(vkId, USER_CANCEL_REVIEW) == null) {
            if (wordInput.equals("нет")) {
                storageService.removeUserStorage(vkId, USER_CANCEL_REVIEW);
                sendUserToNextStep(context, USER_MENU);
            } else if (wordInput.equals("да")) {
                context.getStudentReviewService().deleteStudentReviewByVkId(vkId);
                List<String> savedMessage = Arrays.asList("Запись на ревью была удалена.");
                storageService.updateUserStorage(vkId, USER_CANCEL_REVIEW, savedMessage);
                sendUserToNextStep(context, USER_CANCEL_REVIEW);
            } else {
                throw new ProcessInputException("Введена неверная команда. Нажми \"Да\" для удаления записи на ревью или \"Нет\" для выхода в главное меню.");
            }
        } else {
            throw new ProcessInputException("Введена неверная команда. Нажми \"Да\" для удаления записи на ревью или \"Нет\" для выхода в главное меню.");
        }
    }

    @Override
    public String getDynamicText(BotContext context) {
        Integer vkId = context.getVkId();
        StorageService storageService = context.getStorageService();
        List<String> savedInput = storageService.getUserStorage(vkId, USER_CANCEL_REVIEW);
        String text;

        if (savedInput == null || savedInput.isEmpty()) {
            Review review = null;
            List<StudentReview> openStudentReview = context.getStudentReviewService().getOpenReviewByStudentVkId(vkId);
            if (!openStudentReview.isEmpty()) {
                if (openStudentReview.size() > 1) {
                    //TODO:впилить запись в логи - если у нас у студента 2 открытых ревью которые он сдает - это не нормально
                }
                review = openStudentReview.get(0).getReview();
            }
            // показываем юзеру ревью, на которое он записан, Тему, дату и время
            //если оно еще есть.
            StringBuilder message = new StringBuilder();
            if (review != null) {
                message.append("Вы записаны на ревью:\n")
                        .append("Тема: ")
                        .append(review.getTheme().getTitle())
                        .append(".\n")
                        .append("Дата: ")
                        .append(StringParser.localDateTimeToString(review.getDate()))
                        .append(". \n\n")
                        .append("Вы действительно хотите отменить запись? (Да/Нет)");
            } else {
                message.append("Ревью для отмены не найдено. Возможно его только что отменил ревьюер");
            }
            text = message.toString();
        } else {
            // если зафиксирован инпут на этом шаге, и это "да"
            // говорим, что запись на данное ревью удалена
            text = savedInput.get(0);
        }

        return text;
    }

    @Override
    public String getDynamicKeyboard(BotContext context) {
        Integer vkId = context.getVkId();
        StorageService storageService = context.getStorageService();
        List<String> savedInput = storageService.getUserStorage(vkId, USER_CANCEL_REVIEW);

        if (savedInput == null || savedInput.isEmpty()) {
            Review review = null;
            List<StudentReview> openStudentReview = context.getStudentReviewService().getOpenReviewByStudentVkId(vkId);
            if (!openStudentReview.isEmpty()) {
                review = openStudentReview.get(0).getReview();
            }
            if (review != null) {
                return YES_NO_KB;
            } else {
                return USER_MENU_KB;
            }
        } else {
            return USER_MENU_KB;
        }
    }
}

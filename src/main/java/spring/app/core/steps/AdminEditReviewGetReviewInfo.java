package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.core.StepSelector;
import spring.app.exceptions.NoDataEnteredException;
import spring.app.exceptions.NoNumbersEnteredException;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.StudentReview;
import spring.app.service.abstraction.StorageService;
import spring.app.util.StringParser;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.BACK_AND_EDIT_STATUS_KB;

/**
 * @author AkiraRokudo on 15.05.2020 in one of sun day
 */
@Component
public class AdminEditReviewGetReviewInfo extends Step {

    @Override
    public void enter(BotContext context) {
        Integer vkId = context.getVkId();
        StorageService storageService = context.getStorageService();
        StringBuilder stringBuilder = new StringBuilder();
        String sStudentReviewToChange = storageService.getUserStorage(vkId, StepSelector.ADMIN_EDIT_REVIEW_GET_REVIEW_INFO).get(0);
        StudentReview studentReview = context.getStudentReviewService().getStudentReviewsByIdWithFetchReviewUserThemeAndReviewer(Long.parseLong(sStudentReviewToChange));
        List<String> selectedReviewList = new ArrayList<>();
        stringBuilder
                .append("Ревью по теме '").append(studentReview.getReview().getTheme().getTitle()).append("' ").append("за ").append(studentReview.getReview().getDate()).append(".\n")
                .append("Студент: ").append(studentReview.getUser().getFirstName()).append(" ").append(studentReview.getUser().getLastName()).append("\n")
                .append("Принимающий: ").append(studentReview.getReview().getUser().getFirstName()).append(" ").append(studentReview.getReview().getUser().getLastName()).append("\n");
        context.getStudentReviewAnswerService()
                .getStudentReviewAnswersByStudentReviewId(studentReview.getId()).stream()
                .sorted(Comparator.comparingInt(sra -> sra.getQuestion().getPosition())) //TODO:проверить необходимость сортировки
                .forEach(sra -> {
                    stringBuilder
                            .append(sra.getQuestion().getPosition())
                            .append(". ")
                            .append(sra.getQuestion().getQuestion())
                            .append(" ")
                            .append(sra.getRight() ? "+" : "-")
                            .append("\n");
                });
        selectedReviewList.add(sStudentReviewToChange);
        text = stringBuilder.toString();
        keyboard = BACK_AND_EDIT_STATUS_KB;
    }


    @Override
    public void processInput(BotContext context) throws ProcessInputException, NoNumbersEnteredException, NoDataEnteredException {
        String currentInput = context.getInput();
        StorageService storageService = context.getStorageService();
        Integer vkId = context.getVkId();
        String wordInput = StringParser.toWordsArray(currentInput)[0];
        if (wordInput.equals("назад")) {
            storageService.removeUserStorage(vkId, ADMIN_EDIT_REVIEW_GET_REVIEW_INFO);
            nextStep = ADMIN_EDIT_REVIEW_GET_REVIEW_LIST;
        } else if (wordInput.equals("изменить")) {
            nextStep = ADMIN_EDIT_REVIEW_CHANGE_REVIEW;
        } else {
            throw new ProcessInputException("Введена неверная команда...");
        }
    }
}

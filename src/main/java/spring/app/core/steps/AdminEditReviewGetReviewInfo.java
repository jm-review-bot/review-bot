package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.core.StepSelector;
import spring.app.exceptions.NoDataEnteredException;
import spring.app.exceptions.NoNumbersEnteredException;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.StudentReview;
import spring.app.service.abstraction.StorageService;
import spring.app.service.abstraction.StudentReviewAnswerService;
import spring.app.service.abstraction.StudentReviewService;
import spring.app.util.StringParser;

import java.util.Arrays;
import java.util.Comparator;

import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.BACK_AND_EDIT_STATUS_KB;

/**
 * @author AkiraRokudo on 15.05.2020 in one of sun day
 */
@Component
public class AdminEditReviewGetReviewInfo extends Step {

    private StorageService storageService;
    private StudentReviewService studentReviewService;
    private StudentReviewAnswerService studentReviewAnswerService;

    public AdminEditReviewGetReviewInfo(
            StorageService storageService,
            StudentReviewService studentReviewService,
            StudentReviewAnswerService studentReviewAnswerService) {
        super("", BACK_AND_EDIT_STATUS_KB);
        this.storageService = storageService;
        this.studentReviewService = studentReviewService;
        this.studentReviewAnswerService = studentReviewAnswerService;
    }

    @Override
    public void enter(BotContext context) {
        Integer vkId = context.getVkId();
        StringBuilder stringBuilder = new StringBuilder();
        String sStudentReviewToChange = storageService.getUserStorage(vkId, StepSelector.ADMIN_EDIT_REVIEW_GET_REVIEW_LIST).get(0);
        StudentReview studentReview = studentReviewService.getStudentReviewsByIdWithFetchReviewUserThemeAndReviewer(Long.parseLong(sStudentReviewToChange));

        //извлечем инфу из ревью
        stringBuilder
                .append("Ревью по теме '").append(studentReview.getReview().getTheme().getTitle()).append("' ").append("за ").append(studentReview.getReview().getDate()).append(".\n")
                .append("Студент: ").append(studentReview.getUser().getFirstName()).append(" ").append(studentReview.getUser().getLastName()).append("\n")
                .append("Принимающий: ").append(studentReview.getReview().getUser().getFirstName()).append(" ").append(studentReview.getReview().getUser().getLastName()).append("\n");

        //добавим инфу по ответам
        studentReviewAnswerService.getStudentReviewAnswersByStudentReviewId(studentReview.getId()).stream()
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

        storageService.updateUserStorage(vkId, ADMIN_EDIT_REVIEW_GET_REVIEW_INFO, Arrays.asList(stringBuilder.toString()));
    }


    @Override
    public void processInput(BotContext context) throws ProcessInputException, NoNumbersEnteredException, NoDataEnteredException {
        String currentInput = context.getInput();
        Integer vkId = context.getVkId();
        String wordInput = StringParser.toWordsArray(currentInput)[0];
        if (wordInput.equals("назад")) {
            storageService.removeUserStorage(vkId, ADMIN_EDIT_REVIEW_GET_REVIEW_INFO);
            sendUserToNextStep(context, ADMIN_EDIT_REVIEW_GET_REVIEW_LIST);
        } else if (wordInput.equals("изменить")) {
            String sStudentReviewToChange = storageService.getUserStorage(vkId, StepSelector.ADMIN_EDIT_REVIEW_GET_REVIEW_LIST).get(0);
            StudentReview studentReview = studentReviewService.getStudentReviewById(Long.parseLong(sStudentReviewToChange));
            if (studentReview.getPassed() == null) {
                throw new ProcessInputException("Это ревью еще не было проведено. Его нельзя изменить");
            }
            sendUserToNextStep(context, ADMIN_EDIT_REVIEW_CHANGE_REVIEW);
        } else {
            throw new ProcessInputException("Введена неверная команда...");
        }
    }

    @Override
    public String getDynamicText(BotContext context) {
        return storageService.getUserStorage(context.getVkId(), ADMIN_EDIT_REVIEW_GET_REVIEW_INFO).get(0);
    }

    @Override
    public String getDynamicKeyboard(BotContext context) {
        return "";
    }
}

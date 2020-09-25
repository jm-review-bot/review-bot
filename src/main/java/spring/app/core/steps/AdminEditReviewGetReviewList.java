package spring.app.core.steps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.NoDataEnteredException;
import spring.app.exceptions.NoNumbersEnteredException;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.StudentReview;
import spring.app.model.Theme;
import spring.app.service.abstraction.StorageService;
import spring.app.service.abstraction.StudentReviewService;
import spring.app.service.abstraction.ThemeService;
import spring.app.util.StringParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.DEF_BACK_KB;

/**
 * @author AkiraRokudo on 15.05.2020 in one of sun day
 */
@Component
public class AdminEditReviewGetReviewList extends Step {

    private StorageService storageService;
    private ThemeService themeService;
    private StudentReviewService studentReviewService;

    @Autowired
    public AdminEditReviewGetReviewList(
            StorageService storageService,
            ThemeService themeService,
            StudentReviewService studentReviewService) {
        super("", DEF_BACK_KB);
        this.storageService = storageService;
        this.themeService = themeService;
        this.studentReviewService = studentReviewService;
    }

    @Override
    public void enter(BotContext context) {
        Integer vkId = context.getVkId();

        String selectedUserIds = storageService.getUserStorage(vkId, ADMIN_EDIT_REVIEW_GET_USER_LIST).get(0);
        Long selectedUserId = Long.parseLong(selectedUserIds);
        String selectedThemePosition = storageService.getUserStorage(vkId, ADMIN_EDIT_REVIEW_GET_THEME_LIST).get(0);
        Theme selectedTheme = themeService.getByPosition(Integer.parseInt(selectedThemePosition)).get();
        List<String> reviewToChange = new ArrayList<>();
        studentReviewService.getAllStudentReviewsByStudentIdAndTheme(selectedUserId, selectedTheme).stream()
                .forEach(sreview ->
                        reviewToChange.add(sreview.getId().toString())
                );

        storageService.updateUserStorage(vkId, ADMIN_EDIT_REVIEW_GET_REVIEW_LIST, reviewToChange);
        //если мы вернулись сюда после изменения ревью

    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException, NoNumbersEnteredException, NoDataEnteredException {
        String currentInput = context.getInput();
        Integer vkId = context.getVkId();
        String wordInput = StringParser.toWordsArray(currentInput)[0];
        if (wordInput.equals("назад")) {
            storageService.removeUserStorage(vkId, ADMIN_EDIT_REVIEW_GET_REVIEW_LIST);
            sendUserToNextStep(context, ADMIN_EDIT_REVIEW_GET_THEME_LIST);
        } else if (StringParser.isNumeric(wordInput)) {
            Integer selectedNumber = Integer.parseInt(wordInput);
            List<String> reviews = storageService.getUserStorage(vkId, ADMIN_EDIT_REVIEW_GET_REVIEW_LIST);
            if (selectedNumber <= 0 || selectedNumber > reviews.size()) {
                throw new ProcessInputException("Введено неподходящее число");
            }
            String selectedReviewId = reviews.get(selectedNumber - 1);
            reviews.clear();
            reviews.add(selectedReviewId);
            storageService.updateUserStorage(vkId, ADMIN_EDIT_REVIEW_GET_REVIEW_LIST, reviews);
            sendUserToNextStep(context, ADMIN_EDIT_REVIEW_GET_REVIEW_INFO);
        } else {
            throw new ProcessInputException("Введена неверная команда...");
        }
    }

    @Override
    public String getDynamicText(BotContext context) {
        Integer vkId = context.getVkId();
        StringBuilder stringBuilder = new StringBuilder();
        if (storageService.getUserStorage(vkId, ADMIN_EDIT_REVIEW_CHANGE_REVIEW) != null) {
            storageService.removeUserStorage(vkId, ADMIN_EDIT_REVIEW_CHANGE_REVIEW);
            stringBuilder.append("Статус ревью успешно изменен.\n\n");
        }
        List<String> reviewToChange = storageService.getUserStorage(vkId, ADMIN_EDIT_REVIEW_GET_REVIEW_LIST);
        stringBuilder.append("Выберите ревью для редактирования\n");
        final int[] reviewCounter = {1}; //обход финальности для лямбды
        reviewToChange.stream().forEach(sreviewId -> {
            StudentReview sreview =
                    studentReviewService.getStudentReviewsByIdWithFetchReviewUserThemeAndReviewer(Long.parseLong(sreviewId)).orElseGet(StudentReview::new);
            stringBuilder.append("[").append(reviewCounter[0]++).append("] ")
                    .append(StringParser.localDateTimeToString(sreview.getReview().getDate()))
                    .append(" (");
            if (sreview.getIsPassed() != null) {
                if (!sreview.getIsPassed()) {
                    stringBuilder.append("не ");
                }
                stringBuilder.append("пройдено)\n");
            } else {
                stringBuilder.append("еще не проводилось)\n");
            }
        });

        return stringBuilder.toString();
    }

    @Override
    public String getDynamicKeyboard(BotContext context) {
        return "";
    }
}

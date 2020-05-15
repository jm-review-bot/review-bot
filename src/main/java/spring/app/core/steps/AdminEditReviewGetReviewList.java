package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.NoDataEnteredException;
import spring.app.exceptions.NoNumbersEnteredException;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.StudentReview;
import spring.app.model.Theme;
import spring.app.service.abstraction.StorageService;
import spring.app.util.StringParser;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.BACK_AND_EDIT_STATUS_KB;
import static spring.app.util.Keyboards.BACK_KB;

/**
 * @author AkiraRokudo on 15.05.2020 in one of sun day
 */
@Component
public class AdminEditReviewGetReviewList extends Step {

    @Override
    public void enter(BotContext context) {
        Integer vkId = context.getVkId();
        StorageService storageService = context.getStorageService();
        StringBuilder stringBuilder = new StringBuilder("Выберите ревью для редактирования\n");
        String selectedUserVKIds = storageService.getUserStorage(vkId, ADMIN_EDIT_REVIEW_GET_USER_LIST).get(0);
        Long selectedUserVKId = Long.parseLong(selectedUserVKIds);
        String selectedThemePosition = storageService.getUserStorage(vkId, ADMIN_EDIT_REVIEW_GET_THEME_LIST).get(0);
        Theme selectedTheme = context.getThemeService().getByPosition(Integer.parseInt(selectedThemePosition));

        List<String> reviewToChange = new ArrayList<>();
        final int[] reviewCounter = {1}; //обход финальности для лямбды
        context.getStudentReviewService()
                .getAllStudentReviewsByStudentVkIdAndTheme(selectedUserVKId, selectedTheme).stream()
                .forEach(sreview -> {
                    stringBuilder.append("[").append(reviewCounter[0]++).append("] ")
                            .append(sreview.getReview().getDate())
                            .append(" (");
                    if (!sreview.getPassed()) {
                        stringBuilder.append("не ");
                    }
                    stringBuilder.append("пройдено)");
                    stringBuilder.append("\n");
                    // сохраняем ID юзера в лист
                    reviewToChange.add(sreview.getId().toString());
                });
        text = stringBuilder.toString();
        keyboard = BACK_KB;
        storageService.updateUserStorage(vkId, ADMIN_EDIT_REVIEW_GET_REVIEW_LIST, reviewToChange);
        //если мы вернулись сюда после изменения ревью
        if (storageService.getUserStorage(vkId, ADMIN_EDIT_REVIEW_CHANGE_REVIEW) != null) {
            storageService.removeUserStorage(vkId, ADMIN_EDIT_REVIEW_CHANGE_REVIEW);
            text = "Статус ревью успешно изменен.\n\n" + text;
        }
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException, NoNumbersEnteredException, NoDataEnteredException {
        String currentInput = context.getInput();
        StorageService storageService = context.getStorageService();
        Integer vkId = context.getVkId();
        String wordInput = StringParser.toWordsArray(currentInput)[0];
        if (wordInput.equals("назад")) {
            storageService.removeUserStorage(vkId, ADMIN_EDIT_REVIEW_GET_REVIEW_LIST);
            nextStep = ADMIN_EDIT_REVIEW_GET_THEME_LIST;
        } else if (StringParser.isNumeric(wordInput)) {
            Integer selectedNumber = Integer.parseInt(wordInput);
            List<String> reviews = storageService.getUserStorage(vkId, ADMIN_EDIT_REVIEW_GET_REVIEW_LIST);
            if (selectedNumber <= 0 || selectedNumber > reviews.size()) {
                throw new ProcessInputException("Введено неподходящее число");
            }
            String selectedReviewId = reviews.get(selectedNumber-1);
            reviews.clear();
            reviews.add(selectedReviewId);
            storageService.updateUserStorage(vkId, ADMIN_EDIT_REVIEW_GET_REVIEW_INFO, reviews);
            nextStep = ADMIN_EDIT_REVIEW_GET_REVIEW_INFO;
        } else {
            throw new ProcessInputException("Введена неверная команда...");
        }
    }
}

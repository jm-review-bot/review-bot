package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.NoDataEnteredException;
import spring.app.exceptions.NoNumbersEnteredException;
import spring.app.exceptions.ProcessInputException;
import spring.app.service.abstraction.StorageService;
import spring.app.util.StringParser;

import java.util.ArrayList;
import java.util.List;

import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.BACK_KB;

/**
 * @author AkiraRokudo on 15.05.2020 in one of sun day
 */
@Component
public class AdminEditReviewGetThemeList extends Step {

    @Override
    public void enter(BotContext context) {
        Integer vkId = context.getVkId();
        StorageService storageService = context.getStorageService();
        StringBuilder stringBuilder = new StringBuilder("Выберите тему ревью\n");
        List<String> themesToReview = new ArrayList<>();
        context.getThemeService()
                .getAllThemes().stream()
                .forEach(theme -> {
                    stringBuilder.append("[").append(theme.getPosition()).append("] ")
                            .append(theme.getTitle());
                    stringBuilder.append("\n");
                    // сохраняем ID юзера в лист
                    themesToReview.add(theme.getPosition().toString());
                });
        text = stringBuilder.toString();
        keyboard = BACK_KB;
        storageService.updateUserStorage(vkId, ADMIN_EDIT_REVIEW_GET_THEME_LIST, themesToReview);
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException, NoNumbersEnteredException, NoDataEnteredException {
        String currentInput = context.getInput();
        StorageService storageService = context.getStorageService();
        Integer vkId = context.getVkId();
        String wordInput = StringParser.toWordsArray(currentInput)[0];
        if (wordInput.equals("назад")) {
            storageService.removeUserStorage(vkId, ADMIN_EDIT_REVIEW_GET_THEME_LIST);
            nextStep = ADMIN_EDIT_REVIEW_GET_USER_LIST;
        } else if (StringParser.isNumeric(wordInput)) {
            Integer selectedNumber = Integer.parseInt(wordInput);
            List<String> themes = storageService.getUserStorage(vkId, ADMIN_EDIT_REVIEW_GET_THEME_LIST);
            if (selectedNumber <= 0 || selectedNumber > themes.size()) {
                throw new ProcessInputException("Введено неподходящее число");
            }
            String selectedThemesPosition = themes.get(selectedNumber - 1);
            themes.clear();
            themes.add(selectedThemesPosition);
            storageService.updateUserStorage(vkId, ADMIN_EDIT_REVIEW_GET_THEME_LIST, themes);
            nextStep = ADMIN_EDIT_REVIEW_GET_REVIEW_LIST;
        } else {
            throw new ProcessInputException("Введена неверная команда...");
        }
    }

}

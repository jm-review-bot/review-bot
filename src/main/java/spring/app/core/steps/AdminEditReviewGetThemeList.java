package spring.app.core.steps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.NoDataEnteredException;
import spring.app.exceptions.NoNumbersEnteredException;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.Theme;
import spring.app.service.abstraction.StorageService;
import spring.app.service.abstraction.ThemeService;
import spring.app.util.StringParser;

import java.util.ArrayList;
import java.util.List;

import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.DEF_BACK_KB;

/**
 * @author AkiraRokudo on 15.05.2020 in one of sun day
 */
@Component
public class AdminEditReviewGetThemeList extends Step {

    private StorageService storageService;
    private ThemeService themeService;

    @Autowired
    public AdminEditReviewGetThemeList(StorageService storageService, ThemeService themeService) {
        super("", DEF_BACK_KB);
        this.storageService = storageService;
        this.themeService = themeService;
    }

    @Override
    public void enter(BotContext context) {
        List<String> themesToReview = new ArrayList<>();
        themeService.getAllThemes().stream()
                .forEach(theme ->
                        // сохраняем позицию темы в лист
                        themesToReview.add(theme.getPosition().toString())
                );
        storageService.updateUserStorage(context.getVkId(), ADMIN_EDIT_REVIEW_GET_THEME_LIST, themesToReview);
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException, NoNumbersEnteredException, NoDataEnteredException {
        String currentInput = context.getInput();
        Integer vkId = context.getVkId();
        String wordInput = StringParser.toWordsArray(currentInput)[0];
        if (wordInput.equals("назад")) {
            storageService.removeUserStorage(vkId, ADMIN_EDIT_REVIEW_GET_THEME_LIST);
            sendUserToNextStep(context, ADMIN_EDIT_REVIEW_GET_USER_LIST);
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
            sendUserToNextStep(context, ADMIN_EDIT_REVIEW_GET_REVIEW_LIST);
        } else {
            throw new ProcessInputException("Введена неверная команда...");
        }
    }

    @Override
    public String getDynamicText(BotContext context) {
        StringBuilder stringBuilder = new StringBuilder("Выберите тему ревью\n");
        //обработаем вывод всех тем которые сохранили в энтере
        List<String> themesToReview = storageService.getUserStorage(context.getVkId(), ADMIN_EDIT_REVIEW_GET_THEME_LIST);
        if (themesToReview != null) {
            themesToReview.stream().forEach(themePosition -> {
                Theme theme = themeService.getByPosition(Integer.parseInt(themePosition));
                stringBuilder.append("[").append(themePosition).append("] ")
                        .append(theme.getTitle());
                stringBuilder.append("\n");
            });
        }
        return stringBuilder.toString();
    }

    @Override
    public String getDynamicKeyboard(BotContext context) {
        return "";
    }
}

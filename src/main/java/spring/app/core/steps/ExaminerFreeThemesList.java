package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.NoDataEnteredException;
import spring.app.exceptions.NoNumbersEnteredException;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.Theme;
import spring.app.service.abstraction.StorageService;
import spring.app.service.abstraction.ThemeService;
import spring.app.util.StringParser;

import java.util.Arrays;
import java.util.List;

import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.*;

@Component
public class ExaminerFreeThemesList extends Step {

    private ThemeService themeService;
    private StorageService storageService;

    public ExaminerFreeThemesList(StorageService storageService,
                                  ThemeService themeService) {
        super("", DEF_BACK_KB);
        this.storageService = storageService;
        this.themeService = themeService;
    }

    @Override
    public void enter(BotContext context) {
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException, NoNumbersEnteredException, NoDataEnteredException {
        String command = context.getInput();
        Integer vkId = context.getVkId();
        if (command.equalsIgnoreCase("назад")) {
            sendUserToNextStep(context, USER_MENU);
            storageService.removeUserStorage(vkId, EXAMINER_FREE_THEMES_LIST);
        } else if (StringParser.isNumeric(command)) {
            Integer selectedNumber = Integer.parseInt(command);
            List<Theme> themes = themeService.getFreeThemesByExaminerId(context.getUser().getId());
            if (selectedNumber <= 0 || selectedNumber > themes.size()) {
                throw new ProcessInputException("Введено неподходящее число");
            }
            Theme theme = themes.get(selectedNumber - 1);
            sendUserToNextStep(context, EXAMINER_CHOOSE_METHOD_TO_ADD_STUDENT);
            storageService.removeUserStorage(vkId, EXAMINER_FREE_THEMES_LIST);
            // В следующий шаг передается ID выбранной пользователем темы
            storageService.updateUserStorage(vkId, EXAMINER_CHOOSE_METHOD_TO_ADD_STUDENT, Arrays.asList(theme.getId().toString()));
        } else {
            throw new ProcessInputException("Введена неверная команда...");
        }
    }

    @Override
    public String getDynamicText(BotContext context) {
        List<Theme> freeThemesByUserId = themeService.getFreeThemesByExaminerId(context.getUser().getId());
        StringBuilder freeThemesString = new StringBuilder();
        freeThemesString.append("Выберите тему:\n");
        for (int i = 0; i < freeThemesByUserId.size(); i++) {
            freeThemesString.append(
                    String.format(
                            "[%d] %s\n",
                            i + 1,
                            freeThemesByUserId.get(i).getTitle()
                    )
            );
        }
        return freeThemesString.toString();
    }

    @Override
    public String getDynamicKeyboard(BotContext context) {
        return "";
    }
}

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

        // Обрабатываются команды пользователя
        if (StringParser.isNumeric(command)) {

            // Выбранная тема извлекается из БД
            Integer selectedNumber = Integer.parseInt(command);
            List<Theme> freeThemes = themeService.getFreeThemesByExaminerId(context.getUser().getId());
            if (selectedNumber <= 0 || selectedNumber > freeThemes.size()) {
                throw new ProcessInputException("Введено неподходящее число");
            }
            Theme freeTheme = freeThemes.get(selectedNumber - 1);

            // В следующий шаг передается ID выбранной темы
            sendUserToNextStep(context, EXAMINER_CHOOSE_METHOD_TO_ADD_STUDENT);
            storageService.updateUserStorage(vkId, EXAMINER_CHOOSE_METHOD_TO_ADD_STUDENT, Arrays.asList(freeTheme.getId().toString()));

        } else if (command.equalsIgnoreCase("назад")) {
            sendUserToNextStep(context, USER_MENU);
        } else {
            throw new ProcessInputException("Введена неверная команда...");
        }

        storageService.removeUserStorage(vkId, EXAMINER_FREE_THEMES_LIST);
    }

    @Override
    public String getDynamicText(BotContext context) {
        List<Theme> freeThemes = themeService.getFreeThemesByExaminerId(context.getUser().getId());
        StringBuilder infoMessage = new StringBuilder();
        infoMessage.append("Выберите тему:\n");
        for (int i = 0; i < freeThemes.size(); i++) {
            infoMessage.append(
                    String.format(
                            "[%d] %s\n",
                            i + 1,
                            freeThemes.get(i).getTitle()
                    )
            );
        }
        return infoMessage.toString();
    }

    @Override
    public String getDynamicKeyboard(BotContext context) {
        return "";
    }
}

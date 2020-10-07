package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.NoDataEnteredException;
import spring.app.exceptions.NoNumbersEnteredException;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.Theme;
import spring.app.model.User;
import spring.app.service.abstraction.StorageService;
import spring.app.service.abstraction.ThemeService;
import spring.app.util.StringParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static spring.app.core.StepSelector.EXAMINER_FREE_THEMES_LIST;
import static spring.app.core.StepSelector.USER_MENU;
import static spring.app.core.StepSelector.EXAMINER_CHOOSE_METHOD_TO_ADD_STUDENT;
import static spring.app.util.Keyboards.DEF_BACK_KB;

@Component
public class ExaminerFreeThemesList extends Step {

    private final ThemeService themeService;
    private final StorageService storageService;

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
        Integer examinerVkId = context.getVkId();

        // Обрабатываются команды пользователя
        if (StringParser.isNumeric(command)) {

            // Из хранилища текущего шага извлекается список ID тем, доступных экзаменатору
            List<String> freeThemesIds = storageService.getUserStorage(examinerVkId, EXAMINER_FREE_THEMES_LIST);

            // Проверяется введеный номер темы и из списка выбирается соответствующий ID
            Integer selectedNumber = Integer.parseInt(command);
            if (selectedNumber <= 0 || selectedNumber > freeThemesIds.size()) {
                throw new ProcessInputException("Введено неподходящее число");
            }
            String freeThemeId = freeThemesIds.get(selectedNumber - 1);

            // В хранилище текущего шага помещается ID выбранной темы
            sendUserToNextStep(context, EXAMINER_CHOOSE_METHOD_TO_ADD_STUDENT);
            storageService.updateUserStorage(examinerVkId, EXAMINER_FREE_THEMES_LIST, Arrays.asList(freeThemeId));

        } else if (command.equalsIgnoreCase("назад")) {
            sendUserToNextStep(context, USER_MENU);
            storageService.removeUserStorage(examinerVkId, EXAMINER_FREE_THEMES_LIST);

        } else {
            throw new ProcessInputException("Введена неверная команда...");
        }
    }

    @Override
    public String getDynamicText(BotContext context) {
        User examiner = context.getUser();

        /* Бот выводит сообщение со списком соответсвующих пользователю тем свободной защиты и формирует список c их ID,
        * чтобы отправить их в хранилище текущего шага */
        List<Theme> freeThemes = themeService.getFreeThemesByExaminerId(examiner.getId());
        List<String> freeThemesIds = new ArrayList<>();
        StringBuilder infoMessage = new StringBuilder();
        infoMessage.append("Выберите тему:\n");
        for (int i = 0; i < freeThemes.size(); i++) {
            Theme freeTheme = freeThemes.get(i);
            infoMessage.append(
                    String.format(
                            "[%d] %s\n",
                            i + 1,
                            freeTheme.getTitle()
                    )
            );
            freeThemesIds.add(freeTheme.getId().toString());
        }
        storageService.updateUserStorage(examiner.getVkId(), EXAMINER_FREE_THEMES_LIST, freeThemesIds);
        return infoMessage.toString();
    }

    @Override
    public String getDynamicKeyboard(BotContext context) {
        return "";
    }
}

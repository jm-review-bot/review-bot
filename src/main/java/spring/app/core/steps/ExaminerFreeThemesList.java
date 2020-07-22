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

            // В следующий шаг передается ID выбранной темы
            sendUserToNextStep(context, EXAMINER_CHOOSE_METHOD_TO_ADD_STUDENT);
            storageService.updateUserStorage(examinerVkId, EXAMINER_CHOOSE_METHOD_TO_ADD_STUDENT, Arrays.asList(freeThemeId));

        } else if (command.equalsIgnoreCase("назад")) {

            // Пользователь переходит в главное меню и очищаются хранилища всех шагов, связанных с экзаменатором
            sendUserToNextStep(context, USER_MENU);
            storageService.removeUserStorage(examinerVkId, EXAMINER_FREE_THEMES_LIST);
            storageService.removeUserStorage(examinerVkId, EXAMINER_CHOOSE_METHOD_TO_ADD_STUDENT);
            storageService.removeUserStorage(examinerVkId, EXAMINER_USERS_LIST_FROM_DB);

        } else {
            throw new ProcessInputException("Введена неверная команда...");
        }
    }

    @Override
    public String getDynamicText(BotContext context) {
        User examiner = context.getUser();

        // Из БД извлекается список всех тем, доступных экзаменатору, и сохраняются их ID в хранилище текущего шага
        List<Theme> freeThemes = themeService.getFreeThemesByExaminerId(examiner.getId());
        List<String> freeThemesIds = new ArrayList<>();
        for (Theme freeTheme : freeThemes) {
            freeThemesIds.add(freeTheme.getId().toString());
        }
        storageService.updateUserStorage(examiner.getVkId(), EXAMINER_FREE_THEMES_LIST, freeThemesIds);

        // Бот выводит сообщение со списком соответсвующих пользователю тем свободной защиты
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

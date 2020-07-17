package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.NoDataEnteredException;
import spring.app.exceptions.NoNumbersEnteredException;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.Theme;
import spring.app.service.abstraction.StorageService;
import spring.app.service.abstraction.ThemeService;

import java.util.Arrays;

import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.*;

@Component
public class ExaminerChooseMethodToAddStudent extends Step {

    private StorageService storageService;
    private ThemeService themeService;

    public ExaminerChooseMethodToAddStudent(StorageService storageService,
                                            ThemeService themeService) {
        super("", "");
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
        // Из предыдущего шага извлекается ID темы, выбранной пользователем
        Long themeId = Long.parseLong(storageService.getUserStorage(vkId, EXAMINER_CHOOSE_METHOD_TO_ADD_STUDENT).get(0));
        if (command.equalsIgnoreCase("назад")) {
            sendUserToNextStep(context, EXAMINER_FREE_THEMES_LIST);
            storageService.removeUserStorage(vkId, EXAMINER_CHOOSE_METHOD_TO_ADD_STUDENT);
        } else if (command.equalsIgnoreCase("выбрать из списка")) {
            sendUserToNextStep(context, EXAMINER_CHOOSE_USER_FROM_DB);
            // В следующий шаг передается ID темы, выбранной пользователем
            storageService.updateUserStorage(vkId, EXAMINER_CHOOSE_USER_FROM_DB, Arrays.asList(themeId.toString()));
            storageService.removeUserStorage(vkId, EXAMINER_CHOOSE_METHOD_TO_ADD_STUDENT);
        } else if (command.equalsIgnoreCase("ввести вручную")) {
            // В ТЗ есть такая кнопка, но нет для нее логики
        } else {
            throw  new ProcessInputException("Введена невенрная команда...");
        }
    }

    @Override
    public String getDynamicText(BotContext context) {
        Integer vkId = context.getVkId();
        // Из предыдущего шага извлекается ID темы, выбранной пользователем
        Long themeId = Long.parseLong(storageService.getUserStorage(vkId, EXAMINER_CHOOSE_METHOD_TO_ADD_STUDENT).get(0));
        Theme theme = themeService.getThemeById(themeId);
        return String.format(
                "Вы выбрали тему \"%s\".\n" +
                        "Вы можете выбрать студента из списка или ввести ссылку на профиль студента вконтакте вручную",
                theme.getTitle()
        );
    }

    @Override
    public String getDynamicKeyboard(BotContext context) {
        StringBuilder keys = new StringBuilder();
        keys
                .append(CHOOSE_FROM_LIST)
                .append(this.getColumnDelimiterString())
                .append(ENTER_MANUALLY)
                .append(this.getRowDelimiterString())
                .append(DEF_BACK_KB);
        return keys.toString();
    }
}

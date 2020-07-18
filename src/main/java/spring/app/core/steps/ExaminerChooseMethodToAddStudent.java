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

        // Из предыдущего шага извлекается ID темы
        Long freeThemeId = Long.parseLong(storageService.getUserStorage(vkId, EXAMINER_CHOOSE_METHOD_TO_ADD_STUDENT).get(0));

        // Обрабатываются команды пользователя
        if (command.equalsIgnoreCase("выбрать из списка")) {

            // В следующий шаг передается ID темы
            sendUserToNextStep(context, EXAMINER_USERS_LIST_FROM_DB);
            storageService.updateUserStorage(vkId, EXAMINER_USERS_LIST_FROM_DB, Arrays.asList(freeThemeId.toString()));

        } else if (command.equalsIgnoreCase("ввести вручную")) {
            // В ТЗ есть такая кнопка, но нет для нее логики
        } else if (command.equalsIgnoreCase("назад")) {
            sendUserToNextStep(context, EXAMINER_FREE_THEMES_LIST);
        } else {
            throw  new ProcessInputException("Введена невенрная команда...");
        }

        storageService.removeUserStorage(vkId, EXAMINER_CHOOSE_METHOD_TO_ADD_STUDENT);
    }

    @Override
    public String getDynamicText(BotContext context) {
        Integer vkId = context.getVkId();

        // Из предыдущего шага извлекается ID темы
        Long freeThemeId = Long.parseLong(storageService.getUserStorage(vkId, EXAMINER_CHOOSE_METHOD_TO_ADD_STUDENT).get(0));
        Theme freeTheme = themeService.getThemeById(freeThemeId);

        // Бот выводит сообщение с предложением способа для выбора студента
        return String.format(
                "Вы выбрали тему \"%s\".\n" +
                        "Вы можете выбрать студента из списка или ввести ссылку на профиль студента вконтакте вручную",
                freeTheme.getTitle()
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

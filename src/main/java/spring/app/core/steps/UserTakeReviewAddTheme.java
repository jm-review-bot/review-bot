package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.Theme;
import spring.app.service.abstraction.StorageService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.DEF_BACK_KB;

@Component
public class UserTakeReviewAddTheme extends Step {
    public UserTakeReviewAddTheme() {
        //у шага нет статического текста, но есть статические(видимые независимо от юзера) кнопки
        super("", DEF_BACK_KB);
    }

    @Override
    public void enter(BotContext context) {
        StringBuilder themeList = new StringBuilder("Выбери тему, которую хочешь принять, в качестве ответа пришли цифру (номер темы):\n" +
                "Ты можешь принимать ревью только по тем темам, которые успешно сдал.\n\n");
        List<String> listTheme = new ArrayList<>();
        List<Theme> themes = context.getThemeService().getAllThemes();
        for (Theme position : themes) {
            themeList.append(String.format("[%d] %s\n", position.getPosition(), position.getTitle()));
        }
        themeList.append("\nИли нажмите на кнопку \"Назад\" для возврата к предыдущему меню.");
        listTheme.add(themeList.toString());
        context.getStorageService().updateUserStorage(context.getVkId(), USER_TAKE_REVIEW_ADD_THEME, listTheme);
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException {
        StorageService storageService = context.getStorageService();
        String userInput = context.getInput();
        List<Theme> themes = context.getThemeService().getAllThemes();
        Integer vkId = context.getVkId();
        List<String> themePositionsList = themes.stream()
                .map(Theme::getPosition)
                .map(Object::toString)
                .collect(toList());
        if (themePositionsList.contains(userInput)) {
            // вытаскиваем тему по позиции, позиция соответствует пользовательскому вводу
            Theme selectedTheme = context.getThemeService().getByPosition(Integer.parseInt(userInput));
            // проверяем, что сдали ревью по теме, которую хотим принять
            List<Theme> passedThemesIds = context.getThemeService().getPassedThemesByUser(vkId);
            if (passedThemesIds.contains(selectedTheme)) {
                // складываем в хранилище для использования в следующих шагах
                storageService.updateUserStorage(vkId, USER_TAKE_REVIEW_ADD_THEME, Arrays.asList(selectedTheme.getId().toString()));
                sendUserToNextStep(context, USER_TAKE_REVIEW_ADD_DATE);
            } else {
                throw new ProcessInputException("Ты пока не можешь принять эту тему, потому что не сдал по ней ревью.\n\n" +
                        "Выбери другую тему ревью или нажми на кнопку \"Назад\" для возврата в главное меню.");
            }
        } else if (userInput.equalsIgnoreCase("назад")) {
            // очищаем данные с этого шага и со следующего, если они есть
            storageService.removeUserStorage(vkId, USER_TAKE_REVIEW_ADD_THEME);
            storageService.removeUserStorage(vkId, USER_TAKE_REVIEW_ADD_DATE);
            sendUserToNextStep(context, USER_MENU);
        } else if (userInput.equalsIgnoreCase("/start")) {
            // очищаем данные с этого шага и со следующего, если они есть
            storageService.removeUserStorage(vkId, USER_TAKE_REVIEW_ADD_THEME);
            storageService.removeUserStorage(vkId, USER_TAKE_REVIEW_ADD_DATE);
            sendUserToNextStep(context, START);
        } else {
            throw new ProcessInputException("Введена неверная команда...\n\n" +
                    "Введи цифру, соответствующую теме рьвью или нажми на кнопку \"Назад\" для возврата в главное меню");
        }
    }

    @Override
    public String getDynamicText(BotContext context) {
        List<String> themeList = context.getStorageService().getUserStorage(context.getVkId(), USER_TAKE_REVIEW_ADD_THEME);
        return themeList.get(0);
    }

    @Override
    public String getDynamicKeyboard(BotContext context) {
        return "";
    }
}
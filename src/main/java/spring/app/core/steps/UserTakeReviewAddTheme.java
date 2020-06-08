package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.Theme;
import spring.app.service.abstraction.StorageService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.DEF_BACK_KB;

@Component
public class UserTakeReviewAddTheme extends Step {
    public UserTakeReviewAddTheme() {
        //у шага нет статического текста, но есть статические(видимые независимо от юзера) кнопки
        super("", DEF_BACK_KB);
    }

    private Map<Integer, Theme> themes = new HashMap<>();

    @Override
    public void enter(BotContext context) {
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException {
        StorageService storageService = context.getStorageService();
        String userInput = context.getInput();
        Integer vkId = context.getVkId();
        List<String> themePositionsList = themes.keySet().stream()
                .map(Object::toString)
                .collect(toList());
        if (themePositionsList.contains(userInput)) {
            // вытаскиваем themeId по позиции, позиция соответствует пользовательскому вводу
            String themeId = themes.get(Integer.parseInt(userInput)).getId().toString();
            // проверяем, что сдали ревью по теме, которую хотим принять
            List<String> passedThemesIds = context.getThemeService().getPassedThemesByUser(vkId).stream()
                    .map(theme -> theme.getId().toString())
                    .collect(toList());
            if (passedThemesIds.contains(themeId)) {
                // складываем в хранилище для использования в следующих шагах
                storageService.updateUserStorage(vkId, USER_TAKE_REVIEW_ADD_THEME, Arrays.asList(themeId));
                sendUserToNextStep(context, USER_TAKE_REVIEW_ADD_DATE);
            } else {
                throw new ProcessInputException("Ты пока не можешь принять эту тему, потому что не сдал по ней ревью.\n\n" +
                        "Выбери другую тему ревью или нажми на кнопку \"Назад\" для возврата в главное меню.");
            }
        } else if (userInput.equalsIgnoreCase("назад")) {
            sendUserToNextStep(context, USER_MENU);
            // очищаем данные с этого шага и со следующего, если они есть
            storageService.removeUserStorage(vkId, USER_TAKE_REVIEW_ADD_THEME);
            if (storageService.getUserStorage(vkId, USER_TAKE_REVIEW_ADD_DATE) != null) {
                storageService.removeUserStorage(vkId, USER_TAKE_REVIEW_ADD_DATE);
            }
        } else if (userInput.equalsIgnoreCase("/start")) {
            sendUserToNextStep(context, START);
            // очищаем данные с этого шага и со следующего, если они есть
            storageService.removeUserStorage(vkId, USER_TAKE_REVIEW_ADD_THEME);
            if (storageService.getUserStorage(vkId, USER_TAKE_REVIEW_ADD_DATE) != null) {
                storageService.removeUserStorage(vkId, USER_TAKE_REVIEW_ADD_DATE);
            }
        } else {
            throw new ProcessInputException("Введена неверная команда...\n\n Введи цифру, соответствующую теме рьвью или нажми на кнопку \"Назад\" для возврата в главное меню");
        }
    }

    @Override
    public String getDynamicText(BotContext context) {
        context.getThemeService().getAllThemes().forEach(theme -> themes.putIfAbsent(theme.getPosition(), theme));
        StringBuilder themeList = new StringBuilder("Выбери тему, которую хочешь принять, в качестве ответа пришли цифру (номер темы):\n ");
        themeList.append("Ты можешь принимать ревью только по тем темам, которые успешно сдал.\n\n");
        for (Integer position : themes.keySet()) {
            themeList.append(String.format("[%d] %s\n", position, themes.get(position).getTitle()));
        }
        themeList.append("\nИли нажмите на кнопку \"Назад\" для возврата к предыдущему меню.");
        return themeList.toString();
    }

    @Override
    public String getDynamicKeyboard(BotContext context) {
        return "";
    }
}
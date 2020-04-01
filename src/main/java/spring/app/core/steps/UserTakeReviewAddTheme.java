package spring.app.core.steps;

import spring.app.core.BotContext;
import spring.app.core.StepSelector;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.Theme;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.BACK_KB;

public class UserTakeReviewAddTheme extends Step {
    private Map<Integer, Theme> themes = new HashMap<>();

    @Override
    public void enter(BotContext context) {
        context.getThemeService().getAllThemes().forEach(theme -> themes.put(theme.getPosition(), theme));
        StringBuilder themeList = new StringBuilder("Выберите тему, которую вы хотите принять, в качестве ответа пришлите цифру (номер темы):\n\n");
        for (Integer position : themes.keySet()) {
            themeList.append("[")
                    .append(position)
                    .append("] ")
                    .append(themes.get(position).getTitle())
                    .append("\n");
        }
        themeList.append("\nИли нажмите на кнопку \"Назад\" для возврата к предыдущему меню");
        text = themeList.toString();
        keyboard = BACK_KB;
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException {
        String userInput = context.getInput();
        Integer vkId = context.getVkId();
        List<String> themePositionsList = themes.keySet().stream().map(Object::toString).collect(toList());
        if (themePositionsList.contains(userInput)) {
            //todo проверка на то, что пользователь уже сдал ревью по теме, которую хочет принять

            // вытаскиваем theme_id по позиции, позиция соответствует пользовательскому вводу
            String themeId = themes.get(Integer.parseInt(userInput)).getId().toString();
            // складываем в хранилище
            Map<StepSelector, List<String>> userStorage = getStorage().get(vkId);
            List<String> themeIdStorage = new ArrayList<>();
            themeIdStorage.add(themeId);
            userStorage.put(USER_TAKE_REVIEW_ADD_THEME, themeIdStorage);
            getStorage().put(vkId, userStorage);

            nextStep = USER_TAKE_REVIEW_ADD_DATE;
            themes.clear();
        } else if (userInput.equalsIgnoreCase("назад")) {
            nextStep = USER_MENU;
            themes.clear();
        } else {
            nextStep = USER_TAKE_REVIEW_ADD_THEME;
            throw new ProcessInputException("Введена неверная команда...\n\n " +
                    "Введите цифру, соответствующую теме рьвью или нажмите на кнопку \"Назад\" для возврата в главное меню");
        }
    }
}


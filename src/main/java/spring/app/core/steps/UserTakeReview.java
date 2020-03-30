package spring.app.core.steps;

import org.springframework.stereotype.Component;
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
import static spring.app.util.Keyboards.USER_START_KB;

@Component
public class UserTakeReview extends Step {
    private final Map<Integer, Theme> themes = new HashMap<>();

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
        List<String> themePositionsList = themes.keySet().stream().map(Object::toString).collect(toList());
        if (themePositionsList.contains(userInput)) {

            //сохраняем номер темы ревью в глобальное хранилище по вкайди пользователя и наименованию шага
            Map<StepSelector, List<String>> stepStorage = new HashMap<>();
            List<String> inputStorage = new ArrayList<>();
            inputStorage.add(userInput);
            stepStorage.put(USER_TAKE_REVIEW, inputStorage);
            getStorage().put(context.getVkId(), stepStorage);

            nextStep = USER_TAKE_REVIEW_ADD_DATE;
            keyboard = BACK_KB;
            themes.clear();
        } else if (userInput.equalsIgnoreCase("назад")) {
            nextStep = USER_MENU;
            keyboard = USER_START_KB;
            themes.clear();

            // смысла очищать Storage нет, потому что оно используется только на следующем шаге,
            // на этом шаге мы данные из него не берем
            // на след шаг попадем только с этого шага, т.е. на этом шаге мы перезатрем данные, которые могут в хранилище быть
            // проблемы появления некорректных данных я не вижу

            // или все-таки сделать, чтобы было меньше мусора в программе

        } else {
            nextStep = USER_TAKE_REVIEW;
            keyboard = BACK_KB;
            throw new ProcessInputException("Введена неверная команда...\n\n " +
                    "Введите цифру, соответствующую теме рьвью или нажмите на кнопку \"Назад\" для возврата в главное меню");
        }
    }
}


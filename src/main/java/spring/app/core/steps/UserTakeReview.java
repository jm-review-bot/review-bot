package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.Theme;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static spring.app.core.StepSelector.USER_MENU;
import static spring.app.core.StepSelector.USER_TAKE_REVIEW;
import static spring.app.util.Keyboards.NO_KB;

@Component
public class UserTakeReview extends Step {
    List<Theme> themes = new ArrayList<>();

    @Override
    public void enter(BotContext context) {
        context.getThemeService().getAllThemes().stream()
                .sorted(Comparator.comparing(Theme::getPosition))
                .forEach(theme -> themes.add(theme));

        StringBuilder themeList = new StringBuilder("Выберите тему, которую вы хотите принять, в качестве ответа пришлите цифру (номер темы)\n");

        for (Theme theme : themes) {
            themeList.append("[")
                    .append(theme.getPosition())
                    .append("] ")
                    .append(theme.getTitle())
                    .append("\n");
        }
        text = themeList.toString();

        keyboard = NO_KB;
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException {
        List<String> commands = themes.stream().map(theme -> theme.getPosition().toString()).collect(Collectors.toList());
        if (commands.contains(context.getInput())) {
            //do logic
            // записать в базу ревью без даты
            nextStep = USER_MENU;
            keyboard = NO_KB;
        } else {
            keyboard = NO_KB;
            nextStep = USER_TAKE_REVIEW;
            themes.clear();
            throw new ProcessInputException("Введена неверная команда...\n");
            // как после ввода неправильной команды получить снова тоже сообщение из enter() ?
        }
    }
}


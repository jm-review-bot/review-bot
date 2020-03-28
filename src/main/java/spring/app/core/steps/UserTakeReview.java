package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.Theme;

import java.util.Comparator;

import static spring.app.util.Keyboards.NO_KB;

@Component
public class UserTakeReview extends Step {

    @Override
    public void enter(BotContext context) {
        StringBuilder themeList = new StringBuilder("Выберите тему, которую вы хотите принять, в качестве ответа пришлите цифру (номер темы)");
        context.getThemeService().getAllThemes().stream()
                .sorted(Comparator.comparing(Theme::getPosition))
                .forEach(theme -> themeList
                        .append("[")
                        .append(theme.getPosition())
                        .append("] ")
                        .append(theme.getTitle())
                        .append("\n")
                );
        text = themeList.toString();
        keyboard = NO_KB;
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException {

    }
}

package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.Review;
import spring.app.model.Theme;
import spring.app.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.*;

@Component
public class UserTakeReview extends Step {
    Map<Integer, Theme> themes = new HashMap<>();

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

            // здесь предположения такие, что у Юзера в 1 момент вреени может быть только 1 ревью, которое он принимает
            // поэтому получив номер темы, которую он хочет принять мы сохраняем в БД это ревью, но пока без даты,
            // дату мы введем на следующем этапе
            // и на следующем этапе апдейтим нашу БД

            // или, в еонтексте бота сделать какое-то временное хранилище и туда между шагами складывать инфу,
            //т.о. удет меньше запрсов к БД
            // и вообще это наверное правильное решение

            Theme theme = themes.get(Integer.parseInt(userInput));
            User user = context.getUserService().getByVkId(context.getVkId());
            context.getReviewService().addReview(new Review(user, theme, true));

            nextStep = USER_TAKE_REVIEW_ADD_DATE;
            keyboard = BACK_KB;
            themes.clear();
        } else if (userInput.equalsIgnoreCase("назад")) {
            nextStep = USER_MENU;
            keyboard = USER_START_KB;
            themes.clear();
        } else {
            nextStep = USER_TAKE_REVIEW;
            keyboard = BACK_KB;
            throw new ProcessInputException("Введена неверная команда...\n\n " +
                    "Введите цифру, соответствующую теме рьвью или нажмите на кнопку \"Назад\" для возврата к предыдущему меню");
        }
    }
}


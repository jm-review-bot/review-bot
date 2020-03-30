package spring.app.core.steps;

import spring.app.core.BotContext;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.Review;
import spring.app.model.Theme;
import spring.app.model.User;
import spring.app.util.StringParser;

import java.time.LocalDateTime;

import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.BACK_KB;
import static spring.app.util.Keyboards.GO_MAIN_MENU_KB;

public class UserAddReviewDate extends Step {

    @Override
    public void enter(BotContext context) {
        Integer vkId = context.getVkId();
        //достаем из глобального хранилища номер темы, введенной пользователем на предыдущем шаге
        Integer themePosition = (Integer.parseInt(getStorage().get(vkId).get(USER_TAKE_REVIEW).get(0)));
        Theme theme = context.getThemeService().getThemeByPosition(themePosition);
        text = "Вы выбрали тему: " + theme.getTitle() + ".\n\n" + "Укажите время и дату для принятия ревью в формате ДД.ММ.ГГГГ ЧЧ:ММ " +
                "по Московскому часовому поясу.\n Пример корректного ответа 02.06.2020 17:30\n\n" +
                "Или нажмите на кнопку \"Назад\" для возврата к предыдущему меню и выбора другой темы ревью";
        keyboard = BACK_KB;
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException {
        String userInput = context.getInput();
        LocalDateTime localDateTime = StringParser.stringToLocalDateTime(userInput);

        if (userInput.equalsIgnoreCase("назад")) {
            nextStep = USER_TAKE_REVIEW;
            keyboard = BACK_KB;
        } else if (localDateTime != null && localDateTime.isAfter(LocalDateTime.now())) {

            // здесь бы еще сделать логику, не давать записать 2 ревью одновременно на одну и ту же дату,
            // сделать проверку, не чаще 1 ревью в час
            // добавить ифэлсов, чтобы каждую такую ситуациб обрабатывать и отправлять конкретизированный ответ

            // перенести отсюда логику сохранения ревью в базу на следующий шаг
            // добавить на этом шаге сохранение даты в хранилище, чтобы на следующем шаге была возможность вернться назад и поменять

            Integer vkId = context.getVkId();
            User user = context.getUserService().getByVkId(vkId);
            Integer themePosition = (Integer.parseInt(getStorage().get(vkId).get(USER_TAKE_REVIEW).get(0)));
            Theme theme = context.getThemeService().getThemeByPosition(themePosition);
            context.getReviewService().addReview(new Review(user, theme, true, localDateTime));

            nextStep = USER_TAKE_REVIEW_SUCCESS;
            keyboard = GO_MAIN_MENU_KB;
        } else {
            nextStep = USER_TAKE_REVIEW_ADD_DATE;
            keyboard = BACK_KB;
            throw new ProcessInputException("Некорректный ввод данных...\n\n Пример корректного ответа 02.06.2020 17:30");
        }
    }
}

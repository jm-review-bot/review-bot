package spring.app.core.steps;

import spring.app.core.BotContext;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.Review;
import spring.app.model.Theme;
import spring.app.model.User;
import spring.app.util.StringParser;

import java.time.LocalDateTime;

import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.USER_TAKE_REVIEW_CONFIRMATION_KB;

public class UserTakeReviewConfirmation extends Step {

    @Override
    public void enter(BotContext context) {
        Integer vkId = context.getVkId();
        //достаем из глобального хранилища номер темы, введенной пользователем на предыдущем шаге
        Long theme_id = (Long.parseLong(getStorage().get(vkId).get(USER_TAKE_REVIEW_ADD_THEME).get(0)));
        Theme theme = context.getThemeService().getThemeById(theme_id);
        //достаем из глобального хранилища время ревью, выбранное пользователем на предыдущем шаге
        String reviewDate = getStorage().get(vkId).get(USER_TAKE_REVIEW_ADD_DATE).get(0);

        text = "Ты собираешься провести ревью по теме: " + theme.getTitle() +
                "\nДата начала ревью: " + reviewDate + "\n\n" +
                "В день и время когда оно наступит напиши в чат сообщение \"начать ревью\" или нажми на кнопку \"Начать ревью\"\n\n " +
                "Для добавления ревью в сетку расписания и выхода в главное меню нажми \"Добавить\"\n" +
                "Для выхода в главное меню без сохранения ревью нажми \"Отменить\"\n" +
                "Для возврата к предыдущему меню и выбора другого времени ревью нажми на кнопку \"Назад\"";
        keyboard = USER_TAKE_REVIEW_CONFIRMATION_KB;
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException {
        Integer vkId = context.getVkId();
        if (context.getInput().equalsIgnoreCase("добавить")) {
            User user = context.getUserService().getByVkId(vkId);
            Long theme_id = (Long.parseLong(getStorage().get(vkId).get(USER_TAKE_REVIEW_ADD_THEME).get(0)));
            Theme theme = context.getThemeService().getThemeById(theme_id);
            LocalDateTime plannedStartReviewTime = StringParser.stringToLocalDateTime(getStorage().get(vkId).get(USER_TAKE_REVIEW_ADD_DATE).get(0));
            context.getReviewService().addReview(new Review(user, theme, true, plannedStartReviewTime));
            nextStep = USER_MENU;
        } else if (context.getInput().equalsIgnoreCase("назад")) {
            nextStep = USER_TAKE_REVIEW_ADD_DATE;
        } else if (context.getInput().equalsIgnoreCase("отменить")) {
            nextStep = USER_MENU;
        } else {
            throw new ProcessInputException("Введена неверная команда...");
        }
    }
}

package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.NoDataEnteredException;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.Review;
import spring.app.model.Theme;
import spring.app.model.User;
import spring.app.util.StringParser;

import java.time.LocalDateTime;

import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.USER_TAKE_REVIEW_CONFIRMATION_KB;

@Component
public class UserTakeReviewConfirmation extends Step {

    @Override
    public void enter(BotContext context) {
//        Integer vkId = context.getVkId();
//        //достаем из глобального хранилища номер темы, введенной пользователем на предыдущем шаге
//        Long themeId = (Long.parseLong(getStorage().get(vkId).get(USER_TAKE_REVIEW_ADD_THEME).get(0)));
//        Theme theme = context.getThemeService().getThemeById(themeId);
//        //достаем из глобального хранилища время ревью, выбранное пользователем на предыдущем шаге
//        String reviewDate = getStorage().get(vkId).get(USER_TAKE_REVIEW_ADD_DATE).get(0);
//        StringBuilder textBiulder = new StringBuilder();
//        textBiulder.append("Ты собираешься провести ревью по теме: ").append(theme.getTitle())
//                .append("\nДата начала ревью: ").append(reviewDate)
//                .append("\n\nВ день и время когда оно наступит напиши в чат сообщение \"начать ревью\" или нажми на кнопку \"Начать ревью\"\n\n ")
//                .append("Для добавления ревью в сетку расписания и выхода в главное меню нажми \"Добавить\"\n\n")
//                .append("Для выхода в главное меню без сохранения ревью нажми \"Отменить\"\n\n")
//                .append("Для возврата к предыдущему меню и выбора другого времени ревью нажми на кнопку \"Назад\"");
//        text = textBiulder.toString();
//        keyboard = USER_TAKE_REVIEW_CONFIRMATION_KB;
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException, NoDataEnteredException {
//        Integer vkId = context.getVkId();
//        String userInput = context.getInput();
//        if (userInput.equalsIgnoreCase("добавить")) {
//            User user = context.getUserService().getByVkId(vkId);
//            Long themeId = (Long.parseLong(getStorage().get(vkId).get(USER_TAKE_REVIEW_ADD_THEME).get(0)));
//            Theme theme = context.getThemeService().getThemeById(themeId);
//            LocalDateTime plannedStartReviewTime = StringParser.stringToLocalDateTime(getStorage().get(vkId).get(USER_TAKE_REVIEW_ADD_DATE).get(0));
//            context.getReviewService().addReview(new Review(user, theme, true, plannedStartReviewTime));
//            nextStep = USER_MENU;
//        } else if (userInput.equalsIgnoreCase("назад")) {
//            nextStep = USER_TAKE_REVIEW_ADD_DATE;
//        } else if (userInput.equalsIgnoreCase("отменить")) {
//            nextStep = USER_MENU;
//        } else if (userInput.equalsIgnoreCase("/start")) {
//            nextStep = START;
//        } else {
//            throw new ProcessInputException("Введена неверная команда...");
//        }
    }
}

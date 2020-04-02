package spring.app.core.steps;

import spring.app.core.BotContext;
import spring.app.core.StepSelector;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.Review;
import spring.app.model.Theme;
import spring.app.util.StringParser;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.BACK_KB;

public class UserTakeReviewAddDate extends Step {

    @Override
    public void enter(BotContext context) {
        Integer vkId = context.getVkId();
        Long theme_id = (Long.parseLong(getStorage().get(vkId).get(USER_TAKE_REVIEW_ADD_THEME).get(0)));
        Theme theme = context.getThemeService().getThemeById(theme_id);
        text = "Ты выбрал тему: " + theme.getTitle() + ".\n\n" + "Укажи время и дату для принятия ревью в формате ДД.ММ.ГГГГ ЧЧ:ММ " +
                "по Московскому часовому поясу.\n Пример корректного ответа 02.06.2020 17:30\n\n" +
                "Ты можешь объявить о готовности принять ревью не ранее, чем за 1 час до его начала\n" +
                "Время принятия ревью 1 час, новое ревью не должно пересекаться с другими ревью, которые ты готов принять\n\n" +
                "Для возврата к предыдущему меню и выбора другой темы нажми на кнопку \"Назад\"";
        keyboard = BACK_KB;
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException {
        String userInput = context.getInput();
        LocalDateTime plannedStartReviewTime = StringParser.stringToLocalDateTime(userInput);
        if (userInput.equalsIgnoreCase("назад")) {
            nextStep = USER_TAKE_REVIEW_ADD_THEME;
        } else if (plannedStartReviewTime != null) {
            if (plannedStartReviewTime.isAfter(LocalDateTime.now().plusHours(1))) {
                Integer vkId = context.getVkId();
                List<Review> conflictReviews = context.getReviewService().
                        getReviewsByUserVkIdAndReviewPeriod(vkId, plannedStartReviewTime, plannedStartReviewTime.plusMinutes(59));
                if (conflictReviews.isEmpty()) {
                    Map<StepSelector, List<String>> userStorage = getStorage().get(vkId);
                    List<String> reviewDateStorage = new ArrayList<>();
                    reviewDateStorage.add(userInput);
                    userStorage.put(USER_TAKE_REVIEW_ADD_DATE, reviewDateStorage);
                    getStorage().put(context.getVkId(), userStorage);
                    nextStep = USER_TAKE_REVIEW_CONFIRMATION;
                } else {
                    Review conflictReview = conflictReviews.get(0);
                    throw new ProcessInputException("Новое ревью пересекается с другим ревью, которое ты проводишь." +
                            "\nОбрати внимание, что длительность ревью 1 час.\n\n"
                            + "Пересечение с ревью:\nТема: " + conflictReview.getTheme() +
                            "\nДата начала ревью: " + StringParser.LocalDateTimeToString(conflictReview.getDate()) +
                            "\nДата окончания ревью: " + StringParser.LocalDateTimeToString(conflictReview.getDate().plusHours(1)) + "\n\n" +
                            "Повтори ввод или вернись назад к выбору темы ревью");
                }
            } else if (plannedStartReviewTime.isBefore(LocalDateTime.now())) {
                throw new ProcessInputException("Время принятия нового ревью не может быть в прошлом :)\n " +
                        "Повтори ввод или вернись назад к выбору темы ревью");
            } else if (plannedStartReviewTime.isBefore(LocalDateTime.now().plusMinutes(59))) {
                throw new ProcessInputException("Ты можешь объявить о готовности принять, ревью не ранее, чем за 1 час до его начала\n " +
                        "Повтори ввод или вернись назад к выбору темы ревью");
            }
        } else {
            throw new ProcessInputException("Некорректный ввод данных...\n\n " +
                    "Пример корректного ответа 02.06.2020 17:30");
        }
    }
}

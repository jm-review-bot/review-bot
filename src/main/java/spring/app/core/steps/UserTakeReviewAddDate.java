package spring.app.core.steps;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.core.StepSelector;
import spring.app.exceptions.NoDataEnteredException;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.Review;
import spring.app.model.Theme;
import spring.app.util.StringParser;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.BACK_KB;

@Component
public class UserTakeReviewAddDate extends Step {

    @Value("${review.duration}")
    private int reviewDuration;

    @Value("${review.time_limit.before_starting_review}")
    private int timeLimitBeforeReview;

    @Override
    public void enter(BotContext context) {
        Integer vkId = context.getVkId();
        Long themeId = (Long.parseLong(getUserStorage(vkId, USER_TAKE_REVIEW_ADD_THEME).get(0)));
        Theme theme = context.getThemeService().getThemeById(themeId);
        StringBuilder textBuilder = new StringBuilder();
        textBuilder.append("Ты выбрал тему: ")
                .append(theme.getTitle())
                .append(".\n\n Укажи время и дату для принятия ревью в формате ДД.ММ.ГГГГ ЧЧ:ММ ")
                .append("по Московскому часовому поясу.\n Пример корректного ответа 02.06.2020 17:30\n\n")
                .append("Ты можешь объявить о готовности принять ревью не позднее, чем за ")
                .append(timeLimitBeforeReview + 1)
                .append(" минут до его начала\n")
                .append("Время принятия ревью ")
                .append(reviewDuration + 1)
                .append(" минут, новое ревью не должно пересекаться с другими ревью, которые ты готов принять\n\n")
                .append("Для возврата к предыдущему меню и выбора другой темы нажми на кнопку \"Назад\"");
        text = textBuilder.toString();
        keyboard = BACK_KB;
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException {
        String userInput = context.getInput();
        LocalDateTime plannedStartReviewTime;
        if (userInput.equalsIgnoreCase("назад")) {
            nextStep = USER_TAKE_REVIEW_ADD_THEME;
        } else if (userInput.equalsIgnoreCase("/start")) {
            nextStep = START;
        } else {
            try {
                plannedStartReviewTime = StringParser.stringToLocalDateTime(userInput);
            } catch (NoDataEnteredException e) {
                throw new ProcessInputException("Некорректный ввод данных...\n\n Пример корректного ответа 02.06.2020 17:30");
            }
            if (plannedStartReviewTime.isAfter(LocalDateTime.now().plusMinutes(timeLimitBeforeReview))) {
                Integer vkId = context.getVkId();
                List<Review> conflictReviews = context.getReviewService().getOpenReviewsByReviewerVkId(vkId, plannedStartReviewTime, reviewDuration);
                if (conflictReviews.isEmpty()) {
                    List<String> reviewDateStorage = new ArrayList<>();
                    reviewDateStorage.add(userInput);
                    updateUserStorage(vkId, USER_TAKE_REVIEW_ADD_DATE, reviewDateStorage);
                    nextStep = USER_TAKE_REVIEW_CONFIRMATION;
                } else {
                    Review conflictReview = conflictReviews.get(0);
                    StringBuilder conflictExceptionMessage = new StringBuilder();
                    conflictExceptionMessage.append("Новое ревью пересекается с другим ревью, которое ты планируешь провести.")
                            .append("\n\nОбрати внимание, что длительность ревью ")
                            .append(reviewDuration + 1).append(" минут.\n\n")
                            .append("Пересечение с ревью:\nТема: ")
                            .append(context.getThemeService().getThemeByReviewId(conflictReview.getId()).getTitle())
                            .append("\nДата начала ревью: ")
                            .append(StringParser.LocalDateTimeToString(conflictReview.getDate()))
                            .append("\nДата окончания ревью: ")
                            .append(StringParser.LocalDateTimeToString(conflictReview.getDate().plusMinutes(reviewDuration + 1)))
                            .append("\n\nПовтори ввод или вернись назад к выбору темы ревью");
                    throw new ProcessInputException(conflictExceptionMessage.toString());
                }
            } else if (plannedStartReviewTime.isBefore(LocalDateTime.now())) {
                throw new ProcessInputException("Время принятия нового ревью не может быть в прошлом :)\n Повтори ввод или вернись назад к выбору темы ревью");
            } else if (plannedStartReviewTime.isBefore(LocalDateTime.now().plusMinutes(timeLimitBeforeReview))) {
                StringBuilder timeLimitExceptionMessage = new StringBuilder();
                timeLimitExceptionMessage.append("Ты можешь объявить о готовности принять, ревью не ранее, чем за ")
                        .append(timeLimitBeforeReview + 1)
                        .append(" минут до его начала\n Повтори ввод или вернись назад к выбору темы ревью");
                throw new ProcessInputException(timeLimitExceptionMessage.toString());
            }
        }
    }
}

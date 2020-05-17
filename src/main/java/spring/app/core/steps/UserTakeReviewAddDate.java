package spring.app.core.steps;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.NoDataEnteredException;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.Review;
import spring.app.model.Theme;
import spring.app.service.abstraction.StorageService;
import spring.app.util.StringParser;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

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
        StorageService storageService = context.getStorageService();
        Integer vkId = context.getVkId();
        Long themeId = (Long.parseLong(storageService.getUserStorage(vkId, USER_TAKE_REVIEW_ADD_THEME).get(0)));
        Theme theme = context.getThemeService().getThemeById(themeId);
        List<String> errorMessages = storageService.getUserStorage(vkId, USER_TAKE_REVIEW_CONFIRMATION);
        StringBuilder textBuilder = new StringBuilder();
        if (errorMessages != null) {
            textBuilder.append(errorMessages.get(0));
            storageService.removeUserStorage(vkId, USER_TAKE_REVIEW_CONFIRMATION);
        }
        textBuilder.append(String.format("Ты выбрал тему: %s", theme.getTitle()))
                .append(".\n\n Укажи время и дату для принятия ревью в формате ДД.ММ.ГГГГ ЧЧ:ММ по Московскому часовому поясу.\n Пример корректного ответа 02.06.2020 17:30\n\n")
                .append(String.format("Ты можешь объявить о готовности принять ревью не позднее, чем за %d  минут до его начала\n", (timeLimitBeforeReview + 1)))
                .append(String.format("Время принятия ревью %d минут, новое ревью не должно пересекаться с другими ревью, которые ты готов принять\n\n", (reviewDuration + 1)))
                .append("Для возврата к предыдущему меню и выбора другой темы нажми на кнопку \"Назад\"");
        text = textBuilder.toString();
        keyboard = BACK_KB;
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException {
        StorageService storageService = context.getStorageService();
        String userInput = context.getInput();
        Integer vkId = context.getVkId();
        LocalDateTime plannedStartReviewTime;
        if (userInput.equalsIgnoreCase("назад")) {
            nextStep = USER_TAKE_REVIEW_ADD_THEME;
            // очищаем данные, введенные этом шаге
            storageService.removeUserStorage(vkId, USER_TAKE_REVIEW_ADD_DATE);
        } else if (userInput.equalsIgnoreCase("/start")) {
            nextStep = START;
            // очищаем данные, введенные этом шаге
            storageService.removeUserStorage(vkId, USER_TAKE_REVIEW_ADD_DATE);
        } else {
            try {
                plannedStartReviewTime = StringParser.stringToLocalDateTime(userInput);
            } catch (NoDataEnteredException e) {
                throw new ProcessInputException("Некорректный ввод данных...\n\n Пример корректного ответа 02.06.2020 17:30");
            }
//            if (plannedStartReviewTime.isAfter(LocalDateTime.now().plusMinutes(timeLimitBeforeReview))) {
            if (plannedStartReviewTime.isAfter(LocalDateTime.now())) {
                List<Review> conflictReviews = context.getReviewService().getOpenReviewsByReviewerVkId(vkId, plannedStartReviewTime, reviewDuration);
//                List<Review> conflictStudentReviews = context.getReviewService().getOpenReviewsByStudentVkId(vkId, plannedStartReviewTime, reviewDuration);
                if (!conflictReviews.isEmpty()) {
                    Review conflictReview = conflictReviews.get(0);
                    StringBuilder conflictExceptionMessage = new StringBuilder();
                    conflictExceptionMessage.append("Новое ревью пересекается с другим ревью, которое ты планируешь провести.")
                            .append(String.format("\n\nОбрати внимание, что длительность ревью %d минут", (reviewDuration + 1)))
                            .append(String.format("\n\nПересечение с ревью:\nТема: %s", context.getThemeService().getThemeByReviewId(conflictReview.getId()).getTitle()))
                            .append(String.format("\nДата начала ревью: %s", StringParser.localDateTimeToString(conflictReview.getDate())))
                            .append(String.format("\nДата окончания ревью: %s", StringParser.localDateTimeToString(conflictReview.getDate().plusMinutes(reviewDuration + 1))))
                            .append("\n\nПовтори ввод или вернись назад к выбору темы ревью");
                    throw new ProcessInputException(conflictExceptionMessage.toString());
//                } else if (!conflictStudentReviews.isEmpty()) {
//                    Review conflictReview = conflictStudentReviews.get(0);
//                    StringBuilder conflictExceptionMessage = new StringBuilder();
//                    conflictExceptionMessage.append("Новое ревью пересекается с другим ревью, в котором ты участвуешь.")
//                            .append(String.format("\n\nОбрати внимание, что длительность ревью %d минут", (reviewDuration + 1)))
//                            .append(String.format("\n\nПересечение с ревью:\nТема: %s", context.getThemeService().getThemeByReviewId(conflictReview.getId()).getTitle()))
//                            .append(String.format("\nДата начала ревью: %s", StringParser.localDateTimeToString(conflictReview.getDate())))
//                            .append(String.format("\nДата окончания ревью: %s", StringParser.localDateTimeToString(conflictReview.getDate().plusMinutes(reviewDuration + 1))))
//                            .append("\n\nПовтори ввод или вернись назад к выбору темы ревью");
//                    throw new ProcessInputException(conflictExceptionMessage.toString());
                } else {
                    storageService.updateUserStorage(vkId, USER_TAKE_REVIEW_ADD_DATE, Arrays.asList(userInput));
                    nextStep = USER_TAKE_REVIEW_CONFIRMATION;
                }
            } else if (plannedStartReviewTime.isBefore(LocalDateTime.now())) {
                throw new ProcessInputException("Время принятия нового ревью не может быть в прошлом :)\n Повтори ввод или вернись назад к выбору темы ревью");
//            } else if (plannedStartReviewTime.isBefore(LocalDateTime.now().plusMinutes(timeLimitBeforeReview))) {
//                throw new ProcessInputException(String.format("Ты можешь объявить о готовности принять, ревью не ранее, чем за %d минут до его начала\n" +
//                        "Повтори ввод или вернись назад к выбору темы ревью", (timeLimitBeforeReview + 1)));
            }
        }
    }
}

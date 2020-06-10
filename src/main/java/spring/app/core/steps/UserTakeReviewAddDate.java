package spring.app.core.steps;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.NoDataEnteredException;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.Review;
import spring.app.model.Theme;
import spring.app.model.User;
import spring.app.service.abstraction.StorageService;
import spring.app.util.StringParser;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.DEF_BACK_KB;

@Component
public class UserTakeReviewAddDate extends Step {
    public UserTakeReviewAddDate() {
        //у шага нет статического текста, но есть статические(видимые независимо от юзера) кнопки
        super("", DEF_BACK_KB);
    }
    @Override
    public void enter(BotContext context) {
    }
    @Value("${review.duration}")
    private int reviewDuration;

    @Value("${review.time_limit.before_starting_review}")
    private int timeLimitBeforeReview;

    @Override
    public void processInput(BotContext context) throws ProcessInputException {
        StorageService storageService = context.getStorageService();
        String userInput = context.getInput();
        Integer vkId = context.getVkId();
        LocalDateTime plannedStartReviewTime;
        if (userInput.equalsIgnoreCase("назад")) {
            sendUserToNextStep(context, USER_TAKE_REVIEW_ADD_THEME);
            //очищаем данные, введенные этом шаге
            storageService.removeUserStorage(vkId, USER_TAKE_REVIEW_ADD_DATE);
        } else if (userInput.equalsIgnoreCase("/start")) {
            sendUserToNextStep(context, START);
            //очищаем данные, введенные этом шаге
            storageService.removeUserStorage(vkId, USER_TAKE_REVIEW_ADD_DATE);
        } else {
            try {
                plannedStartReviewTime = StringParser.stringToLocalDateTime(userInput);
            } catch (NoDataEnteredException e) {
                throw new ProcessInputException("Некорректный ввод данных...\n\n Пример корректного ответа 02.06.2020 17:30");
            }
            if (plannedStartReviewTime.isAfter(LocalDateTime.now())) {
                List<Review> conflictReviews = context.getReviewService().getOpenReviewsByReviewerVkId(vkId, plannedStartReviewTime, reviewDuration);
                List<Review> conflictStudentReviews = context.getReviewService().getOpenReviewsByStudentVkId(vkId, plannedStartReviewTime, reviewDuration);
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
                } else if (!conflictStudentReviews.isEmpty()) {
                    Review conflictReview = conflictStudentReviews.get(0);
                    StringBuilder conflictExceptionMessage = new StringBuilder();
                    conflictExceptionMessage.append("Новое ревью пересекается с другим ревью, в котором ты участвуешь.")
                            .append(String.format("\n\nОбрати внимание, что длительность ревью %d минут", (reviewDuration + 1)))
                            .append(String.format("\n\nПересечение с ревью:\nТема: %s", context.getThemeService().getThemeByReviewId(conflictReview.getId()).getTitle()))
                            .append(String.format("\nДата начала ревью: %s", StringParser.localDateTimeToString(conflictReview.getDate())))
                            .append(String.format("\nДата окончания ревью: %s", StringParser.localDateTimeToString(conflictReview.getDate().plusMinutes(reviewDuration + 1))))
                            .append("\n\nПовтори ввод или вернись назад к выбору темы ревью");
                    throw new ProcessInputException(conflictExceptionMessage.toString());
                } else {
                    //все хорошо с валидацией, создаем ревью.
                    User user = context.getUserService().getByVkId(vkId);
                    Long themeId = (Long.parseLong(storageService.getUserStorage(vkId, USER_TAKE_REVIEW_ADD_THEME).get(0)));
                    Theme theme = context.getThemeService().getThemeById(themeId);
                    context.getReviewService().addReview(new Review(user, theme, true, plannedStartReviewTime));
                    storageService.removeUserStorage(vkId, USER_TAKE_REVIEW_ADD_THEME);
                    storageService.removeUserStorage(vkId, USER_TAKE_REVIEW_ADD_DATE);
                    sendUserToNextStep(context, USER_MENU);
                    String textForSend = String.format(String.format("Супер! Твоё ревью добавлено в сетку расписания, в день и время когда оно наступит нажми на кнопку \"Начать ревью\"\n\nВаше ревью '%%s' %%s было успешно добавлено в сетку расписания\n\n"), theme.getTitle(), userInput);
                    //и шлем сообщение.А вот и не шлем.Мы не знаем какие кнопки отобразить пользователю.Так что
                    //отправлять сообщения таким образом мы можем всем кроме себя.Для себя пользуемся хаком
                    //с хранилищем -
                    //        кладем сообщения на шаг, на который планируем придти
                    context.getVkService().sendMessage(textForSend, this.getComposeKeyboard(context), vkId);
                    context.getStorageService().updateUserStorage(vkId, USER_MENU, Arrays.asList(textForSend));
                }
            } else if (plannedStartReviewTime.isBefore(LocalDateTime.now())) {
                throw new ProcessInputException("Время принятия нового ревью не может быть в прошлом :)\n Повтори ввод или вернись назад к выбору темы ревью");
            } else if (plannedStartReviewTime.isBefore(LocalDateTime.now().plusMinutes(timeLimitBeforeReview))) {
                throw new ProcessInputException(String.format("Ты можешь объявить о готовности принять, ревью не ранее, чем за %d минут до его начала\n" +
                        "Повтори ввод или вернись назад к выбору темы ревью", (timeLimitBeforeReview + 1)));
            }
        }
    }

    @Override
    public String getDynamicText(BotContext context) {
        StorageService storageService = context.getStorageService();
        Integer vkId = context.getVkId();
        Long themeId = (Long.parseLong(storageService.getUserStorage(vkId, USER_TAKE_REVIEW_ADD_THEME).get(0)));
        Theme theme = context.getThemeService().getThemeById(themeId);
        StringBuilder textBuilder = new StringBuilder();

        textBuilder.append(String.format("Ты выбрал тему: %s", theme.getTitle()))
                .append(".\n\n Укажи время и дату для принятия ревью в формате ДД.ММ.ГГГГ ЧЧ:ММ по Московскому часовому поясу.\n Пример корректного ответа 02.06.2020 17:30\n\n")
                .append(String.format("Ты можешь объявить о готовности принять ревью не позднее, чем за %d  минут до его начала\n", (timeLimitBeforeReview + 1)))
                .append(String.format("Время принятия ревью %d минут, новое ревью не должно пересекаться с другими ревью, которые ты готов принять\n\n", (reviewDuration + 1)))
                .append("Для возврата к предыдущему меню и выбора другой темы нажми на кнопку \"Назад\"");
        return textBuilder.toString();
    }

    @Override
    public String getDynamicKeyboard(BotContext context) {
        return "";
    }
}
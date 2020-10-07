package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.Review;
import spring.app.service.abstraction.ReviewService;
import spring.app.service.abstraction.StorageService;
import spring.app.service.abstraction.ThemeService;
import spring.app.service.abstraction.VkService;
import spring.app.util.StringParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static spring.app.core.StepSelector.USER_MENU;
import static spring.app.core.StepSelector.SELECTING_REVIEW_TO_DELETE;
import static spring.app.core.StepSelector.REVIEWER_DELETE_REVIEW;
import static spring.app.util.Keyboards.DEF_BACK_KB;

@Component
public class SelectingReviewToDelete extends Step {

    private final VkService vkService;
    private final ReviewService reviewService;
    private final ThemeService themeService;
    private final StorageService storageService;

    public SelectingReviewToDelete(VkService vkService, ReviewService reviewService, ThemeService themeService, StorageService storageService) {
        super("",DEF_BACK_KB);
        this.reviewService = reviewService;
        this.vkService = vkService;
        this.themeService = themeService;
        this.storageService = storageService;
    }

    @Override
    public void enter(BotContext context) {
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException {
        String command = context.getInput();
        if ("Назад".equals(command)) {
            sendUserToNextStep(context, USER_MENU);
        } else if (StringParser.isNumeric(command)) {
            //это позиция (номер) ревью в списке, выведенном пользователю. это НЕ индекс этого ревью в БД,
            // это именно его номер (позиция) в списке, который выводиился при выполнении метода enter этого стэпа
            int numberReview = Integer.parseInt(command);
            if ((numberReview > 0) && (numberReview <= storageService.getUserStorage(context.getVkId(), SELECTING_REVIEW_TO_DELETE).size())) {
                //получаем индекс ревью по её позции в списке выбора......
                long reviewId = Long.parseLong(storageService.getUserStorage(context.getVkId(), SELECTING_REVIEW_TO_DELETE).get(numberReview - 1));//это индекс ревью в БД
                Review review = reviewService.getReviewById(reviewId);
                if ((review == null) || (review.getUser().getVkId().intValue() != context.getVkId().intValue())) {
                    String message = "Выбранное ревью отсутствует в базе данных либо вы перестали числиться его создателем. Возможно, оно было удалено во время выбора.\n";
                    storageService.removeUserStorage(context.getVkId(), SELECTING_REVIEW_TO_DELETE);
                    List<Review> reviews = reviewService.getOpenReviewsByReviewerVkId(context.getVkId());
                    if (reviews.isEmpty()) {
                        vkService.sendMessage(message, this.getComposeKeyboard(context), context.getUser().getVkId());
                        sendUserToNextStep(context, USER_MENU);
                    } else {
                        sendUserToNextStep(context, SELECTING_REVIEW_TO_DELETE);
                    }
                } else {
                    //...... и добавляем этот индекс в хранилище REVIEWER_DELETE_REVIEW для извлечения этого индекса на шаге REVIEWER_DELETE_REVIEW и последующего
                    //на вышеупомянутом шаге удаления ревью с этим индексом
                    storageService.updateUserStorage(context.getVkId(), REVIEWER_DELETE_REVIEW, Arrays.asList((Long.toString(reviewId))));
                    sendUserToNextStep(context, REVIEWER_DELETE_REVIEW);
                }
            } else {
                throw new ProcessInputException("Введённое число не является номером какого-либо ревью из списка. Введите корректное число.\n");
            }
        } else {
            throw new ProcessInputException("Введена неверная команда...\n");
        }
    }

    @Override
    public String getDynamicText(BotContext context) {
        //хранилище стэпа REVIEWER_DELETE_REVIEW для каждого vkId юзера хранит айдишник ревью, которое следует отменить
        List<Review> reviews = reviewService.getOpenReviewsByReviewerVkId(context.getVkId());
        StringBuilder selectReview = new StringBuilder("Выберете ревью, которое хотите отменить:\n");
        int i = 1;
        List<String> reviewIds = new ArrayList<>();
        for (Review review : reviews) {
            selectReview = selectReview.append("[").append(i).append("] ").append(themeService.getThemeById(review.getTheme().getId()).getTitle()).append(" - ").append(StringParser.localDateTimeToString(review.getDate())).append("\n");
            i++;
            reviewIds.add(Long.toString(review.getId()));
        }
        storageService.updateUserStorage(context.getVkId(), SELECTING_REVIEW_TO_DELETE, reviewIds);
        return selectReview.toString();
    }

    @Override
    public String getDynamicKeyboard(BotContext context) {
        return "";
    }
}
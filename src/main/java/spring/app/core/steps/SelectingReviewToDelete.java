package spring.app.core.steps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.Review;
import spring.app.service.abstraction.VkService;
import spring.app.service.impl.StorageServiceImpl;
import spring.app.util.StringParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static spring.app.core.StepSelector.*;
import static spring.app.core.StepSelector.REVIEWER_DELETE_REVIEW;
import static spring.app.util.Keyboards.*;

@Component
public class SelectingReviewToDelete extends Step {
    private StorageServiceImpl ssi;

    @Autowired
    public SelectingReviewToDelete(StorageServiceImpl ssi) {
        this.ssi = ssi;
    }

    @Override
    public void enter(BotContext context) {//здесь выводится текст в чате при переходе на этот step
        //хранилище стэпа REVIEWER_DELETE_REVIEW для каждого vkId юзера хранит айдишник ревью, которое следует отменить
        List<Review> reviews = context.getReviewService().getOpenReviewsByReviewerVkId(context.getVkId());
        StringBuilder selectReview = new StringBuilder("Выберете ревью, которое хотите отменить:\n");
        int i = 1;
        List<String> reviewIds = new ArrayList<>();
        for (Review review : reviews) {
            selectReview = selectReview.append("[").append(i).append("] {").append(context.getThemeService().getThemeById(review.getTheme().getId()).getTitle()).append("} - {").append(StringParser.localDateTimeToString(review.getDate())).append("}\n");
            i++;
            reviewIds.add(Long.toString(review.getId()));
        }
        ssi.getUserStorage(context.getVkId(), SELECTING_REVIEW_TO_DELETE).addAll(reviewIds);
        text = selectReview.toString();
        keyboard = BACK_KB;
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException {
        String command = context.getInput();
        if ("Назад".equals(command)) {
            nextStep = USER_MENU;
        } else if (StringParser.isNumeric(command)) {
            //это позиция (номер) ревью в списке, выведенном пользователю. это НЕ индекс этого ревью в БД,
            // это именно его номер (позиция) в списке, который выводиился при выполнении метода enter этого стэпа
            int numberReview = Integer.parseInt(command);
            if ((numberReview > 0) && (numberReview <= ssi.getUserStorage(context.getVkId(), SELECTING_REVIEW_TO_DELETE).size())) {
                //получаем индекс ревью по её позции в списке выбора......
                long reviewId = Long.parseLong(ssi.getUserStorage(context.getVkId(), SELECTING_REVIEW_TO_DELETE).get(numberReview - 1));//это индекс ревью в БД
                Review review = context.getReviewService().getReviewById(reviewId);
                if ((review == null) || (review.getUser().getVkId() != context.getVkId())) {
                    String message = "Выбранное ревью отсутствует в базе данных либо вы перестали числиться его создателем. Возможно оно было удалено во время выбора.\n";
                    ssi.removeUserStorage(context.getVkId(), SELECTING_REVIEW_TO_DELETE);
                    List<Review> reviews = context.getReviewService().getOpenReviewsByReviewerVkId(context.getVkId());
                    VkService vkService = context.getVkService();
                    if (reviews.isEmpty()) {
                        vkService.sendMessage(message, keyboard, context.getUser().getVkId());
                        nextStep = USER_MENU;
                    } else {
                        nextStep = SELECTING_REVIEW_TO_DELETE;
                    }
                } else {
                    //...... и добавляем этот индекс в хранилище REVIEWER_DELETE_REVIEW для извлечения этого индекса на шаге REVIEWER_DELETE_REVIEW и последующего
                    //на вышеупомянутом шаге удаления ревью с этим индексом
                    ssi.updateUserStorage(context.getVkId(), REVIEWER_DELETE_REVIEW, Arrays.asList((Long.toString(reviewId))));
                    nextStep = REVIEWER_DELETE_REVIEW;
                }
            } else {
                throw new ProcessInputException("Введённое число не является номером какого-либо ревью из списка. Введите корректное число.\n");
            }
        } else {
            throw new ProcessInputException("Введена неверная команда...\n");
        }
    }
}
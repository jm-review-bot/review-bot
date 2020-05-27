package spring.app.core.steps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.Review;
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
    @Autowired
    StorageServiceImpl ssi;
    @Override
    public void enter(BotContext context) {//здесь выводится текст в чате при переходе на этот step
        //хранилище стэпа REVIEWER_DELETE_REVIEW для каждого vkId юзера хранит айдишник ревью, которое следует отменить
        //если оно пустое, то создаём для данного пользователя пустой список строк
        if(ssi.getUserStorage(context.getVkId(),REVIEWER_DELETE_REVIEW)==null) {
            ssi.updateUserStorage(context.getVkId(),REVIEWER_DELETE_REVIEW,new ArrayList<String>());
        }
        //хранилище стэпа SELECTING_REVIEW_TO_DELETE для каждого vkId юзера хранит номера ревью (для выбора того, какое следует отменить), которые выведятся
        //списком в цикле for ниже
        ssi.updateUserStorage(context.getVkId(),SELECTING_REVIEW_TO_DELETE,new ArrayList<String>());
        List<Review> reviews = context.getReviewService().getOpenReviewsByReviewerVkId(context.getVkId());
        StringBuilder selectReview = new StringBuilder("Выберете ревью, которое хотите отменить:\n");
        int i = 1;
        for(Review review: reviews) {
            selectReview = selectReview.append("[").append(i).append("] {").append(context.getThemeService().getThemeById(review.getTheme().getId()).getTitle()).append("} - {").append(StringParser.localDateTimeToString(review.getDate())).append("}\n");
            i++;
            ssi.getUserStorage(context.getVkId(),SELECTING_REVIEW_TO_DELETE).add(Long.toString(review.getId()));
        }
        text = selectReview.toString();
        keyboard = BACK_KB;
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException {
        String command = context.getInput();
        if("Назад".equals(command)) {
            nextStep = USER_MENU;
        } else if(StringParser.isNumeric(command)) {
            //это позиция (номер) ревью в списке, выведенном пользователю. это НЕ индекс этого ревью в БД,
            // это именно его номер (позиция) в списке, который выводиился при выполнении метода enter этого стэпа
            int numberReview = Integer.parseInt(command);
            if((numberReview>0)&&(numberReview <= ssi.getUserStorage(context.getVkId(),SELECTING_REVIEW_TO_DELETE).size())) {
                //получаем индекс ревью по её позции в списке выбора......
                long indexReview = Long.parseLong(ssi.getUserStorage(context.getVkId(),SELECTING_REVIEW_TO_DELETE).get(numberReview-1));//это индекс ревью в БД
                if((((context.getReviewService().getReviewById(indexReview)) == null) || !(context.getReviewService().getReviewById(indexReview)).getOpen()) && (context.getReviewService().getReviewById(indexReview).getUser().getVkId()!=context.getVkId())) {
                    String message = "Введённое число является номером ревью из одного из ревью списка выше, однако к настоящему времени данное ревью было либо удалено, либо закрыто, либо вы не числитесь его создателем (а значит не можете отменять его).\n";
                    ssi.updateUserStorage(context.getVkId(),SELECTING_REVIEW_TO_DELETE,new ArrayList<String>());
                    List<Review> reviews = context.getReviewService().getOpenReviewsByReviewerVkId(context.getVkId());
                    if(reviews.isEmpty()) {
                        context.getVkService().sendMessage(message,keyboard,context.getUser().getVkId());
                        nextStep = USER_MENU;
                    } else {
                        nextStep = SELECTING_REVIEW_TO_DELETE;
                    }
                } else {
                    //...... и добавляем этот индекс в хранилище REVIEWER_DELETE_REVIEW для извлечения этого индекса на шаге REVIEWER_DELETE_REVIEW и последующего
                    //на вышеупомянутом шаге удаления ревью с этим индексом
                    ssi.updateUserStorage(context.getVkId(),REVIEWER_DELETE_REVIEW, Arrays.asList((Long.toString(indexReview))));
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
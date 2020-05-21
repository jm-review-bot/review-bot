package spring.app.core.steps;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.core.StepHolder;
import spring.app.core.StepSelector;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.Question;
import spring.app.model.Review;
import spring.app.model.User;
import spring.app.service.abstraction.*;
import spring.app.util.Keyboards;

import javax.persistence.NoResultException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static spring.app.core.StepSelector.*;
import static spring.app.core.StepSelector.REVIEWER_DELETE_REVIEW;
import static spring.app.util.Keyboards.*;

@Component
public class SelectingReviewToDelete extends Step {
    private Logger logger = LoggerFactory.getLogger(ReviewerDeleteReview.class);
    //public static HashMap<Integer, Integer> specMap = ReviewerDeleteReview.specMap;//мапа для хранения соответствующих каждому юзеру номеров (айдишников) ревью, которые надо отменить
    @Override
    public void enter(BotContext context) {//здесь выводится текст в чате при переходе на этот step
        System.out.println("BEGIN_STEP::"+"SelectingReviewToDelete");
        if(ReviewerDeleteReview.specMap.get(context.getUser().getVkId())==null) {
            ReviewerDeleteReview.specMap.put(context.getUser().getVkId(),new Integer(-1));
        }
        List<Review> reviews = context.getReviewService().getOpenReviewsByReviewerVkId(context.getUser().getVkId());
        //если нет ни одного ревью, то в чат даётся сообщение об ошибке
        if(reviews.isEmpty()) {
            System.out.println("reviews_isEmpty");
            text = "Произошла ошибка. Вы не запланировали ни одного ревью\n";
            nextStep = USER_MENU;
            try {
                processInput(context);
            } catch (Throwable throwable) {
                System.out.println("Throwable::"+throwable.toString());//
            }
            return;
        }
        String selectReview = "Выберете ревью, которое хотите отменить:\n";
        int i = 1;
        for(Review review: reviews) {
            selectReview = selectReview.concat("["+i+"] {"+context.getThemeService().getThemeById(review.getTheme().getId()).getTitle()+"} - {"+review.getDate()+"}\n");
            i++;
        }
        text = selectReview;
        keyboard = BACK_KB;
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException {
        String command = context.getInput();
        if("Назад".equals(command)) {
            nextStep = USER_MENU;//START;
        } else if(NumberUtils.isNumber(command)) {
            int numberReview = Integer.parseInt(command);
            List<Review> rs = context.getReviewService().getOpenReviewsByReviewerVkId(context.getUser().getVkId());
            if ((numberReview>0) && (numberReview<=rs.size())) {
                ReviewerDeleteReview.specMap.put(context.getVkId(),Integer.valueOf(numberReview));
                //context.getVkService().sendMessage(message,keyboard,context.getUser().getVkId());
                nextStep = REVIEWER_DELETE_REVIEW;
                //throw new ProcessInputException("...---...\n");
            } else {
                keyboard = BACK_KB;
                String message = "Введённое число не является номером какого-либо ревью из списка. Введите корректное число.\n";
                context.getVkService().sendMessage(message,keyboard,context.getUser().getVkId());
            }
        } else {
            keyboard = BACK_KB;
            throw new ProcessInputException("Введена неверная команда...\n");
        }
    }
}
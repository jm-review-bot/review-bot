package spring.app.core.steps;

import org.apache.commons.lang3.math.NumberUtils;//---***---
import org.slf4j.Logger;//---***---
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
import static spring.app.core.StepSelector.SELECTING_REVIEW_TO_DELETE;
import static spring.app.util.Keyboards.*;
import static spring.app.util.Keyboards.CANCEL_OR_DELETE;

@Component
public class ReviewerDeleteReview extends Step {
    private Logger logger = LoggerFactory.getLogger(ReviewerDeleteReview.class);
    public static HashMap<Integer, Integer> specMap = new HashMap<Integer, Integer>();//мапа для хранения соответствующих каждому юзеру номеров (айдишников) ревью, которые надо отменить
    @Override
    public void enter(BotContext context) {//здесь выводится текст в чате при переходе на этот step
        System.out.println("BEGIN_STEP::"+"ReviewerDeleteReview");
        int numberReview = specMap.get(context.getVkId());
        List<Review> rs = context.getReviewService().getOpenReviewsByReviewerVkId(context.getUser().getVkId());
        String name_review = context.getThemeService().getThemeById(rs.get(numberReview-1).getTheme().getId()).getTitle();
        String date_review = rs.get(numberReview-1).getDate().toString();
        String message = "Вы действительно хотите отменить ревью {"+name_review+"} - {"+date_review+"}.\n";
        System.out.println(message);
        text = message;
        keyboard = CANCEL_OR_DELETE;//две кнопки: “отмена” и “да, отменить ревью”
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException {
        int gn8gf43g34 = 0;
        String command = context.getInput();
        if("Да, отменить ревью".equals(command)) {
            int numberReview = specMap.get(context.getVkId()).intValue();
            int y = 0;
            List<Review> rs = context.getReviewService().getOpenReviewsByReviewerVkId(context.getUser().getVkId());
            Review specReview = deleteReview(
                    rs.get(numberReview-1),
                    context.getThemeService(),
                    context.getUserService(),
                    context.getStepHolder(),
                    context.getQuestionService(),
                    context.getReviewService(),
                    context.getVkService(),
                    context.getVkId()
            );
            specMap.put(context.getVkId(),new Integer(-1));//фэлс-ин-хэшмап
            keyboard = BACK_KB;
            nextStep = USER_MENU;
            int yy = 0;
            /////////////////////////////////////////////////////////nextStep = USER_MENU;
            logger.debug("\tlog-message об операции пользователя над экземпляром сущности:\n" +
                    "Администратор "+context.getUser().getFirstName()+" "+context.getUser().getLastName()+" [id - "+context.getUser().getVkId()+"] отменил ревью (которое должен был принимать). " +
                    "А именно - ревью {"+context.getThemeService().getThemeById(specReview.getTheme().getId()).getTitle()+"} - {"+specReview.getDate()+"}");
            String message = "Ревью {"+context.getThemeService().getThemeById(specReview.getTheme().getId()).getTitle()+"} - {"+specReview.getDate()+"} было успешно отменено.\n";
            System.out.println(message);
            context.getVkService().sendMessage(message,keyboard,context.getUser().getVkId());
            //throw new ProcessInputException("...---...\n");
            //
        } else if ("Отмена".equals(command))  {
            nextStep = SELECTING_REVIEW_TO_DELETE;
        } else {
            keyboard = CANCEL_OR_DELETE;
            throw new ProcessInputException("Введена неверная команда...\n");
        }
    }
    Review deleteReview(Review review, ThemeService themeService, UserService userService, StepHolder stepHolder, QuestionService questionService, ReviewService reviewService, VkService vkService, Integer vkId) {
        //всем участникам этого ревью, т.е. всем студентам, которые были на это ревью записаны - должно отправиться
        //сообщение с текстом: “Ревью {название темы} - {дата и время проведения} было отменено проверяющим”
        String message = "Ревью {"+themeService.getThemeById(review.getTheme().getId()).getTitle()+"} - {"+review.getDate()+"} было отменено проверяющим\n";
        List<User> studentsByReview = userService.getStudentsByReviewId(review.getId());
        for(User user : studentsByReview) {
            if(user.getChatStep() == USER_MENU) {
                //перед отправкой находящемуся на стэпе UserMenu студенту сообщения нужно определить, какие кнопки ему надо отображать
                //перед отправкой студенту сообщения нужно определить, какие кнопки ему надо отображать
                StringBuilder keys = new StringBuilder(HEADER_FR);
                Review studentReview = null;
                try {
                    studentReview = reviewService.getOpenReviewByStudentVkId(vkId);
                } catch (NoResultException ignore) {
                    // Если пользователь не записан ни на одно ревью, метод Dao выбросит исключение и нужно скрыть кнопку "Отменить ревью"
                }
                List<Review> userReviews = reviewService.getOpenReviewsByReviewerVkId(vkId).stream()
                        .filter(rev -> rev.getDate().plusMinutes(10).isAfter(LocalDateTime.now()))//сортировка ревью по дате, отобржается сначала ближайшее по времени
                        .collect(Collectors.toList());
                if (!userReviews.isEmpty()) {
                    keys
                            .append(REVIEW_START_FR)
                            .append(ROW_DELIMETER_FR);
                }
                if (studentReview != null) {
                    keys
                            .append(REVIEW_CANCEL_FR)
                            .append(ROW_DELIMETER_FR);
                }
                if(!reviewService.getOpenReviewsByReviewerVkId(user.getVkId()).isEmpty()) {
                    keys.append(Keyboards.USER_MENU_D_FR).
                            append(FOOTER_FR);
                } else {
                    keys.append(USER_MENU_FR)
                            .append(FOOTER_FR);
                }
                String new_keyboard = keys.toString();
                //отправка каждому студенту сообщения message
                vkService.sendMessage(message,new_keyboard,user.getVkId());
            } else {
                Step userStep = stepHolder.getSteps().get(user.getChatStep());
                //отправка каждому студенту сообщения message
                vkService.sendMessage(message,userStep.getKeyboard(),user.getVkId());
            }
            ////--Step userStep = stepHolder.getSteps().get(user.getChatStep());
            ////--//отправка каждому студенту сообщения message
            ////--vkService.sendMessage(message,userStep.getKeyboard(),user.getVkId());
        }
        //удалить из базы это ревью и все связанные с ним данные
        //удаление вопросов по этому ревью
        List<Question> questions = questionService.getQuestionsByReviewId(review.getId());
        for(Question question : questions) {
            questionService.deleteQuestionById(question.getId());
        }
        Review specReview = new Review(null,review.getTheme(),false,review.getDate());
        //удление самого ревью
        reviewService.deleteReviewById(review.getId());
        return specReview;
    }
}
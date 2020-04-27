package spring.app.core.steps;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.core.StepSelector;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.Question;
import spring.app.model.Review;
import spring.app.model.User;
import spring.app.service.abstraction.*;
import spring.app.util.StringParser;

import java.util.HashMap;
import java.util.List;

import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.*;

@Component
public class DeleteReview extends Step {
    private static final Logger logger = LoggerFactory.getLogger(
            DeleteReview.class);
    public static HashMap<Integer, Integer> specMap = new HashMap<Integer, Integer>();
    //public static boolean error;
    //public static int countReviews = -1;
    //public static Review specReview;
    @Override
    public void enter(BotContext context) {//здесь выводится текст в чате при переходе на этот step
        //======
        System.out.println("BEGIN_STEP::"+"DeleteReview");
        if(specMap.get(context.getUser().getVkId())==null) {
            System.out.println("--||---||--||--userVkId---"+context.getUser().getVkId()+"------");
            specMap.put(context.getUser().getVkId(),new Integer(-1));
               //DeleteReview.specMap.remove(userVkId);
               //DeleteReview.specMap.put(userVkId,new Integer(-1));
            System.out.println("--||---||--||--userVkId---"+context.getUser().getVkId()+"------");
        }
        //======
                    List<Review> reviews = context.getReviewService().getOpenReviewsByReviewerVkId(context.getUser().getVkId());
                    //==================================== если нет ни одного ревью, то в чат даётся сообщение об ошибке
                    if(reviews.isEmpty()) {
                        System.out.println("reviews_isEmpty");
                        text = "Произошла ошибка. Вы не запланировали ни одного ревью\n";
                        nextStep = USER_MENU;//START;//--//--// - не осуществляется нэкст-стэп
                        //error = true;
                        try {
                            processInput(context);
                        } catch (Throwable throwable) {
                            System.out.println("Throwable::"+throwable.toString());//
                        }
                        return;
                    }
                    //====================================
                    String selectReview = "Выберете ревью, которое хотите отменить:\n";
                    int i = 1;
                    for(Review review: reviews) {
                        selectReview = selectReview.concat("["+i+"] {"+context.getThemeService().getThemeById(review.getTheme().getId()).getTitle()+"} - {"+review.getDate()+"}\n");
                        i++;
                    }
                    //countReviews = reviews.size();
        text = selectReview;
        keyboard = BACK_KB;
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException {
        //ТЫ УЖЕ НА ЭТОМ STEP`Е
        //Тут обработчики твоих возможных команд, которые ты вводишь УЖЕ БУДУЧИ НА ЭТОМ STEP`е
        String command = context.getInput();
        if(("Отмена".equals(command) || "Да, отменить ревью".equals(command)) && (specMap.get(context.getVkId()).intValue() != -1)) {
            if("Да, отменить ревью".equals(command)) {
                int numberReview = specMap.get(context.getVkId()).intValue();
                List<Review> rs = context.getReviewService().getOpenReviewsByReviewerVkId(context.getUser().getVkId());
                Review specReview = deleteReview(
                        rs.get(numberReview-1),
                        context.getThemeService(),
                        context.getUserService(),
                        context.getQuestionService(),
                        context.getReviewService(),
                        context.getVkService(),
                        context.getVkId()
                );
                specMap.put(context.getVkId(),new Integer(-1));//фэлс-ин-хэшмап
                   //specMap.remove(context.getVkId());
                   //specMap.put(context.getVkId(), new Integer(-1));
                keyboard = BACK_KB;
                nextStep = USER_MENU;//keyboard = BACK_KB;
                //System.out.println("|-LOGGING-|-|-|-"+context.getUser().getLastName()+" "+context.getUser().getFirstName()+" отменил ревью, которое должен был принимать. " +
                  //      "А именно - ревью {"+context.getThemeService().getThemeById(specReview.getTheme().getId()).getTitle()+"} - {"+specReview.getDate()+"}");
                //logger.debug("Пользователь "+context.getUser().getLastName()+" "+context.getUser().getFirstName()+" отменил ревью, которое должен был принимать. " +
                  //      "А именно - ревью {"+context.getThemeService().getThemeById(specReview.getTheme().getId()).getTitle()+"} - {"+specReview.getDate()+"}");
                throw new ProcessInputException("Ревью {"+context.getThemeService().getThemeById(specReview.getTheme().getId()).getTitle()+"} - {"+specReview.getDate()+"} было успешно отменено.\n");
                //
            } else {
                List<Review> reviews = context.getReviewService().getOpenReviewsByReviewerVkId(context.getUser().getVkId());
                String selectReview = "Выберете ревью, которое хотите отменить:\n";
                int i = 1;
                for(Review review: reviews) {
                    selectReview = selectReview.concat("["+i+"] {"+context.getThemeService().getThemeById(review.getTheme().getId()).getTitle()+"} - {"+review.getDate()+"}\n");
                    i++;
                }
                specMap.put(context.getVkId(),new Integer(-1));//фэлс-ин-хэшмап
                   //specMap.remove(context.getVkId());
                   //specMap.put(context.getVkId(), new Integer(-1));
                keyboard = BACK_KB;//nextStep = StepSelector.DELETE_REVIEW;
                throw new ProcessInputException(selectReview);
            }
        }
        if("Назад".equals(command)) {
            nextStep = USER_MENU;//START;
        } else if(NumberUtils.isNumber(command)) {
            int numberReview = Integer.parseInt(command);
            List<Review> rs = context.getReviewService().getOpenReviewsByReviewerVkId(context.getUser().getVkId());
            if ((numberReview>0) && (numberReview<=rs.size())) {
                //“Вы действительно хотите отменить ревью {название темы} - {дата и время проведения}”
                //И две кнопки: “отмена” и “да, отменить ревью”
                specMap.put(context.getVkId(),Integer.valueOf(numberReview));
                   //specMap.remove(context.getVkId());
                   //specMap.put(context.getVkId(),Integer.valueOf(numberReview));
                keyboard = CANCEL_OR_DELETE;
                throw new ProcessInputException("Вы действительно хотите отменить ревью {"+context.getThemeService().getThemeById(rs.get(numberReview).getTheme().getId()).getTitle()+"} - {"+rs.get(numberReview).getDate()+"}.\n");
            } else {
                keyboard = BACK_KB;
                throw new ProcessInputException("Введённое число не является номером какого-либо ревью из списка. Введите корректное число.\n");
            }
        } else {
            keyboard = BACK_KB;
            throw new ProcessInputException("Введена неверная команда...\n");
        }
    }
    //============================================= МЕТОД УДАЛЕНИЯ РЕВЬЮ ====================================================================
    Review deleteReview(Review review, ThemeService themeService, UserService userService, QuestionService questionService, ReviewService reviewService, VkService vkService, Integer vkId) {
        //всем участникам этого ревью, т.е. всем студентам, которые были на это ревью записаны - должно отправиться
        //сообщение с текстом: “Ревью {название темы} - {дата и время проведения} было отменено проверяющим”
        String message = "Ревью {"+themeService.getThemeById(review.getTheme().getId()).getTitle()+"} - {"+review.getDate()+"} было отменено проверяющим\n";
        List<User> studentsByReview = userService.getStudentsByReviewId(review.getId());
        for(User user : studentsByReview) {
            //отправка каждому студенту сообщения message
            vkService.sendMessage(message,NO_KB,vkId);
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
    //=======================================================================================================================================
}
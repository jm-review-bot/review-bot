package spring.app.core.steps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.core.StepHolder;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.Review;
import spring.app.model.StudentReview;
import spring.app.model.User;
import spring.app.service.abstraction.*;
import spring.app.service.impl.StorageServiceImpl;
import spring.app.util.Keyboards;
import spring.app.util.StringParser;

import javax.persistence.NoResultException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static spring.app.core.StepSelector.*;
import static spring.app.core.StepSelector.SELECTING_REVIEW_TO_DELETE;
import static spring.app.util.Keyboards.*;
import static spring.app.util.Keyboards.CANCEL_OR_DELETE;

@Component
public class ReviewerDeleteReview extends Step {
    @Autowired
    StorageServiceImpl ssi;//хранилище стэпа REVIEWER_DELETE_REVIEW для каждого vkId юзера хранит айдишник ревью, которое следует отменить
    @Override
    public void enter(BotContext context) {//здесь выводится текст в чате при переходе на этот step
        long numberReview = Long.parseLong(ssi.getUserStorage(context.getVkId(),REVIEWER_DELETE_REVIEW).get(0));
        String nameReview = context.getThemeService().getThemeById(context.getReviewService().getReviewById(numberReview).getTheme().getId()).getTitle();
        String dateReview = StringParser.localDateTimeToString(context.getReviewService().getReviewById(numberReview).getDate());
        StringBuilder message = new StringBuilder("Вы действительно хотите отменить ревью {").append(nameReview).append("} - {").append(dateReview).append("}.\n");
        text = message.toString();
        keyboard = CANCEL_OR_DELETE;//две кнопки: “отмена” и “да, отменить ревью”
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException {
        String command = context.getInput();
        if("Да, отменить ревью".equals(command)) {
            long numberReview = Long.parseLong(ssi.getUserStorage(context.getVkId(),REVIEWER_DELETE_REVIEW).get(0));
            Review specReview = context.getReviewService().getReviewById(numberReview);
            deleteReview(
                    specReview,
                    context.getStudentReviewService(),
                    context.getThemeService(),
                    context.getUserService(),
                    context.getStepHolder(),
                    context.getReviewService(),
                    context.getVkService(),
                    context.getVkId()
            );
            ssi.updateUserStorage(context.getVkId(),REVIEWER_DELETE_REVIEW,new ArrayList<String>());
            keyboard = BACK_KB;
            nextStep = USER_MENU;
            StringBuilder message = new StringBuilder("Ревью {").append(context.getThemeService().getThemeById(specReview.getTheme().getId()).getTitle()).append("} - {").append(StringParser.localDateTimeToString(specReview.getDate())).append("} было успешно отменено.\n");
            ssi.updateUserStorage(context.getVkId(),USER_MENU,Arrays.asList((message.toString())));
        } else if ("Отмена".equals(command))  {
            nextStep = SELECTING_REVIEW_TO_DELETE;
        } else {
            throw new ProcessInputException("Введена неверная команда...\n");
        }
    }
    private void deleteReview(Review review, StudentReviewService studentReviewService, ThemeService themeService, UserService userService, StepHolder stepHolder, ReviewService reviewService, VkService vkService, Integer vkId) {
        //всем участникам этого ревью, т.е. всем студентам, которые были на это ревью записаны - должно отправиться
        //сообщение с текстом: “Ревью {название темы} - {дата и время проведения} было отменено проверяющим”
        StringBuilder message = new StringBuilder("Ревью {").append(themeService.getThemeById(review.getTheme().getId()).getTitle()).append("} - {").append(StringParser.localDateTimeToString(review.getDate())).append("} было отменено проверяющим\n");
        List<User> studentsByReview = userService.getStudentsByReviewId(review.getId());
        for(User user : studentsByReview) {
            if(user.getChatStep() == USER_MENU) {

                //перед отправкой находящемуся на стэпе UserMenu студенту сообщения нужно определить, какие кнопки ему надо отображать
                //перед отправкой студенту сообщения нужно определить, какие кнопки ему надо отображать
                StringBuilder keys = new StringBuilder(HEADER_FR);
                List<StudentReview> studentReviews = null;
                try {
                    studentReviews = studentReviewService.getOpenReviewByStudentVkId(vkId);//reviewService.getOpenReviewByStudentVkId(vkId);
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
                if (studentReviews != null) {
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
                String newKeyboard = keys.toString();
                //отправка каждому студенту сообщения message
                vkService.sendMessage(message.toString(),newKeyboard,user.getVkId());
            } else {
                Step userStep = stepHolder.getSteps().get(user.getChatStep());
                //отправка каждому студенту сообщения message
                vkService.sendMessage(message.toString(),userStep.getKeyboard(),user.getVkId());
            }
        }
        //собственно удаление самого ревью
        reviewService.deleteReviewById(review.getId());
    }
}
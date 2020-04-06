package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.core.StepSelector;
import spring.app.exceptions.NoNumbersEnteredException;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.Review;
import spring.app.model.StudentReview;
import spring.app.model.Theme;
import spring.app.util.StringParser;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.BACK_KB;


@Component
public class UserPassReviewGetListReview extends Step {

    @Override
    public void enter(BotContext context) {
        //с прошлошо шага получаем ID темы и по нему из запроса получаем тему
        Theme theme = context.getThemeService().getThemeById(Long.parseLong(getUserStorage(context.getVkId(),
                StepSelector.USER_PASS_REVIEW_GET_LIST_REVIEW).get(0)));
        List<Review> reviews = context.getReviewService().getAllReviewsByTheme(theme);
        //получаем список возможных ревью по выбранной теме
        StringBuilder reviewList = new StringBuilder("Выберите ревью, на которое вы хотите записаться, в качестве ответа пришлите цифру (номер ревью) \n\n");
        reviews.stream()
                .sorted(Comparator.comparing(Review::getDate))
                .forEach(review -> reviewList
                        .append("[")
                        .append(review.getId())
                        .append("]")
                        .append(" дата: ")
                        .append(review.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                        .append(", принимающий: ")
                        .append(review.getUser().getFirstName())
                        .append(" ")
                        .append(review.getUser().getLastName())
                        .append("\n")
                );
        text = reviewList.toString();
        keyboard = BACK_KB;
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException, NoNumbersEnteredException {
        //TODO ВАЖНО возможно могут выбранное выше ревью уже занять
        Theme theme = context.getThemeService().getThemeById(Long.parseLong(getUserStorage(context.getVkId(),
                StepSelector.USER_PASS_REVIEW_GET_LIST_REVIEW).get(0)));
        List<Review> reviews = context.getReviewService().getAllReviewsByTheme(theme);
        if (StringParser.isNumeric(context.getInput())) {
            Long command = StringParser.toNumbersSet(context.getInput()).iterator().next().longValue();
            //перебираем все ревью и сравниваем ID с введенным числом и так по кругу, пока не получим нужный результат
            for (Review review : reviews) {
                if (review.getId() == command) {
                    StudentReview studentReview = new StudentReview();
                    studentReview.setReview(review);
                    studentReview.setUser(context.getUser());
                    context.getStudentReviewService().addStudentReview(studentReview);
                    //сохраняю дату ревью для следующего шага и очищаю данные в Storage для этого шага
                    List<String> list = new ArrayList<>();
                    list.add(review.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                    updateUserStorage(context.getVkId(), USER_PASS_REVIEW_ADD_STUDENT_REVIEW, list);
                    removeUserStorage(context.getVkId(), USER_PASS_REVIEW_GET_LIST_REVIEW);
                    nextStep = USER_PASS_REVIEW_ADD_STUDENT_REVIEW;
                    break;
                }
            }
            if (nextStep == null){
                throw new ProcessInputException("Введен неверный номер ревью...");
            }
        } else {
            //определяем нажатую кнопку или сообщаем о неверной команде
            String command = StringParser.toWordsArray(context.getInput())[0];
            if ("/start".equals(command)) {
                nextStep = START;
                removeUserStorage(context.getVkId(), USER_PASS_REVIEW_GET_LIST_REVIEW);
            } else if ("назад".equals(command)) {
                nextStep = USER_PASS_REVIEW_ADD_THEME;
                removeUserStorage(context.getVkId(), USER_PASS_REVIEW_GET_LIST_REVIEW);
            } else {
                throw new ProcessInputException("Введена неверная команда...");
            }
        }
    }
}
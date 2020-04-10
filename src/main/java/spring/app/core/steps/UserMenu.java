package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.Review;
import spring.app.model.User;
import spring.app.service.abstraction.ReviewService;
import spring.app.util.StringParser;

import javax.persistence.NoResultException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.*;

@Component
public class UserMenu extends Step {

    @Override
    public void enter(BotContext context) {
        Integer vkId = context.getVkId();
        User user = context.getUser();
        ReviewService reviewService = context.getReviewService();
        // проверка, есть ли у юзера открытые ревью где он ревьюер - кнопка начать ревью
        List<Review> userReviews = reviewService.getOpenReviewsByReviewerVkId(vkId);
        // проверка, записан ли он на другие ревью.
        Review studentReview = null;
        try {
            studentReview = reviewService.getOpenReviewByStudentVkId(vkId);
        } catch (NoResultException ignore) {
            // Если пользователь не записан ни на одно ревью, метод Dao выбросит исключение и нужно скрыть кнопку "Отменить ревью"
        }
        // формируем блок кнопок
        StringBuilder keys = new StringBuilder(HEADER_FR);
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
        keys.append(USER_MENU_FR)
                .append(FOOTER_FR);
        keyboard = keys.toString();
        text = String.format("Привет, %s!\nВы можете сдавать и принимать p2p ревью по разным темам, для удобного использования бота воспользуйтесь кнопками + скрин. \nНа данный момент у вас %d очков RP (Review Points) для сдачи ревью.\nRP используются для записи на ревью, когда вы хотите записаться на ревью вам надо потратить RP, первое ревью бесплатное, после его сдачи вы сможете зарабатывать RP принимая ревью у других. Если вы приняли 1 ревью то получаете 2 RP, если вы дали возможность вам сдать, но никто не записался на сдачу (те вы пытались провести ревью, но не было желающих) то вы получаете 1 RP.", user.getFirstName(), user.getReviewPoint());
    }

    // TODO вынести правила ревью в проперти (начало за +- 10 минут, кол-во очков за ревью в котором не было участников)
    @Override
    public void processInput(BotContext context) throws ProcessInputException {
        Integer vkId = context.getVkId();
        String command = StringParser.toWordsArray(context.getInput())[0];
        if (command.equals("начать")) { // (Начать прием ревью)
            // получаем список всех ревью, которые проводит пользователь
            List<Review> userReviews = context.getReviewService().getOpenReviewsByReviewerVkId(vkId);
            if (!userReviews.isEmpty()) {
                // берем из списка то ревью, которое находится в диапазоне +- 10 минут от текущего времени
                Review nearestReview = userReviews.stream()
                        .filter(review -> review.getDate().plusMinutes(10).isAfter(LocalDateTime.now()) && review.getDate().minusMinutes(10).isBefore(LocalDateTime.now()))
                        .findFirst().orElse(null);
                // если такого ревью нет, сообщаем, что начать ты можешь либо за 10 минут до его начала, либо в течение 10 минут после начала
                if (nearestReview == null) {
                    throw new ProcessInputException("Ты можешь начать проведение ревью не ранее 10 минут до его начала, либо в течение 10 минут после начала ревью");
                    // если ревью есть в этом временном диапазоне
                } else {
                    // то смотрим записан ли кто-то на него
                    Long reviewId = nearestReview.getId();
                    List<User> students = context.getUserService().getStudentsByReviewId(reviewId);
                    if (!students.isEmpty()) {
                        // если кто-то записан на ревью, то сохраняем reviewId в STORAGE
                        List<String> reviewIds =  new ArrayList<>();
                        reviewIds.add(reviewId.toString());
                        updateUserStorage(vkId, USER_MENU, reviewIds);
                        nextStep = USER_START_REVIEW_HANGOUTS_LINK;
                    } else {
                        // если никто не записался на ревью, то добавялем 1 очко пользователю и закрываем ревью
                        nearestReview.setOpen(false);
                        context.getReviewService().updateReview(nearestReview);
                        User user = context.getUserService().getByVkId(vkId);
                        user.setReviewPoint(user.getReviewPoint() + 1);
                        throw new ProcessInputException(String.format("На твое ревью никто не записался, ты получаешь 1 RP.\nТвой баланс: %d RP", user.getReviewPoint()));
                    }
                }
            } else {
                // если пользователь не проводит ревью, то показываем сообщение
                throw new ProcessInputException("Ты еще не объявлял о принятии ревью!\n Сначала ты должен его объвить, для этого нажми на кнопку \"Принять ревью\" и следуй дальнейшим указаниям.");
            }
        } else if (command.equals("отменить")) { // (Отменить ревью)
            nextStep = USER_CANCEL_REVIEW;
        } else if (command.equals("сдать")) { // (Сдать ревью)
            nextStep = USER_PASS_REVIEW_ADD_THEME;
        } else if (command.equals("принять")) { // (Принять ревью)
            nextStep = USER_TAKE_REVIEW_ADD_THEME;
        } else if (command.equals("/admin")
                && context.getRole().isAdmin()) { // валидация что юзер имеет роль админ
            nextStep = ADMIN_MENU;
        } else { // любой другой ввод
            throw new ProcessInputException("Введена неверная команда...");
        }
    }
}

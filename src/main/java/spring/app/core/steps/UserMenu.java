package spring.app.core.steps;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.core.StepSelector;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.Review;
import spring.app.model.User;
import spring.app.service.abstraction.ReviewService;
import spring.app.service.abstraction.StorageService;
import spring.app.util.Keyboards;
import spring.app.util.StringParser;

import javax.persistence.NoResultException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static spring.app.core.StepSelector.*;
import static spring.app.core.StepSelector.DELETE_REVIEW;
import static spring.app.util.Keyboards.*;

@Component
public class UserMenu extends Step {

    @Value("${review.point_for_empty_review}")
    int pointForEmptyReview;

    @Value("${review.delay}")
    int reviewDelay;

    @Value("${review.early_start}")
    int reviewEarlyStart;

    @Override
    public void enter(BotContext context) {
        //======
        System.out.println("BEGIN_STEP::"+"UserMenu");
        //======
        Integer vkId = context.getVkId();
        User user = context.getUser();
        ReviewService reviewService = context.getReviewService();
        // проверка, есть ли у юзера открытые ревью где он ревьюер - кнопка начать ревью
        // если ревью открыто, но прошло более 10 минут с момента его начала, то кнопка отображаться не будет
        List<Review> userReviews = reviewService.getOpenReviewsByReviewerVkId(vkId).stream()
                .filter(review -> review.getDate().plusMinutes(reviewDelay).isAfter(LocalDateTime.now()))//сортировка ревью по дате, отобржается сначала ближайшее по времени
                .collect(Collectors.toList());
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
        //---если у пользователя есть ХОТЬ ОДНО созданное им ревью, то отобразить кнопку "Отменить приём ревью" [DELETE_REVIEW]
        //getOpenReviewsByReviewerVkId(new Integer(vkId))
        if(!context.getReviewService().getOpenReviewsByReviewerVkId(user.getVkId()).isEmpty()) {
            System.out.println("|STEP::UserMenu::пользователь_с_vkId_"+vkId.toString()+"_записан_принимающим_(преподавателем)_как_минимум_на_одно_ревью|");
            keys.append(Keyboards.USER_MENU_D_FR).
                    append(FOOTER_FR);
            System.out.println("--======--");
        } else {
            keys.append(USER_MENU_FR)
                    .append(FOOTER_FR);
        }
        //---
        keyboard = keys.toString();
        text = String.format("Привет, %s!\nВы можете сдавать и принимать p2p ревью по разным темам, для удобного использования бота воспользуйтесь кнопками + скрин. \nНа данный момент у вас %d RP (Review Points) для сдачи ревью.\nRP используются для записи на ревью, когда вы хотите записаться на ревью вам надо потратить RP, первое ревью бесплатное, после его сдачи вы сможете зарабатывать RP принимая ревью у других. Если вы приняли 1 ревью то получаете 2 RP, если вы дали возможность вам сдать, но никто не записался на сдачу (те вы пытались провести ревью, но не было желающих) то вы получаете 1 RP.", user.getFirstName(), user.getReviewPoint());
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException {
        Integer vkId = context.getVkId();
        StorageService storageService = context.getStorageService();
        String command = context.getInput();//StringParser.toWordsArray(context.getInput())[0];
        //тут каждая команда со своим обратчиком:
        if (command.indexOf("Начать")!=-1) { // (Начать прием ревью)
            // получаем список всех ревью, которые проводит пользователь
            List<Review> userReviews = context.getReviewService().getOpenReviewsByReviewerVkId(vkId);
            if (!userReviews.isEmpty()) {
                // берем из списка то ревью, которое находится в диапазоне 5 минут до начала ревью и 10 минут после начала ревью
                Review nearestReview = userReviews.stream()
                        .filter(review -> review.getDate().plusMinutes(reviewDelay).isAfter(LocalDateTime.now()) && review.getDate().minusMinutes(reviewEarlyStart).isBefore(LocalDateTime.now()))
                        .findFirst().orElse(null);
                // если такого ревью нет, сообщаем, что начать ты можешь либо за 5 минут до его начала, либо в течение 10 минут после начала
                if (nearestReview == null) {
                    String notification = String.format("Ты можешь начать проведение ревью не ранее %d минут до его начала, либо в течение %d минут после начала ревью", reviewEarlyStart, reviewDelay);
                    throw new ProcessInputException(notification);
                    // если ревью есть в этом временном диапазоне
                } else {
                    // то смотрим записан ли кто-то на него
                    Long reviewId = nearestReview.getId();
                    List<User> students = context.getUserService().getStudentsByReviewId(reviewId);
                    if (!students.isEmpty()) {
                        // если кто-то записан на ревью, то сохраняем reviewId в STORAGE
                        storageService.updateUserStorage(vkId, USER_MENU, Arrays.asList(reviewId.toString()));
                        nextStep = USER_START_REVIEW_HANGOUTS_LINK;
                    } else {
                        // если никто не записался на ревью, то добавялем очки пользователю и закрываем ревью
                        nearestReview.setOpen(false);
                        context.getReviewService().updateReview(nearestReview);
                        User user = context.getUserService().getByVkId(vkId);
                        user.setReviewPoint(user.getReviewPoint() + pointForEmptyReview);
                        context.getUserService().updateUser(user);
                        throw new ProcessInputException(String.format("На твое ревью никто не записался, ты получаешь 1 RP.\nТвой баланс: %d RP", user.getReviewPoint()));
                    }
                }
            } else {
                // если пользователь не проводит ревью, то показываем сообщение
                throw new ProcessInputException("Ты еще не объявлял о принятии ревью!\n Сначала ты должен его объвить, для этого нажми на кнопку \"Принять ревью\" и следуй дальнейшим указаниям.");
            }
        } else if (command.indexOf("приём")!=-1) { // (Отменить ревью (!) у принимающего лица [препода])
            nextStep = DELETE_REVIEW;
            storageService.removeUserStorage(vkId, DELETE_REVIEW);
        } else if (command.indexOf("сдачу")!=-1) { // (Отменить запись на ревью (!) у сдающего лица [студента])
            nextStep = USER_CANCEL_REVIEW;
            storageService.removeUserStorage(vkId, USER_MENU);
        } else if (command.indexOf("Сдать")!=-1) { // (Сдать ревью)
            nextStep = USER_PASS_REVIEW_ADD_THEME;
            storageService.removeUserStorage(vkId, USER_MENU);
        } else if (command.indexOf("Принять")!=-1) { // (Принять ревью)
            nextStep = USER_TAKE_REVIEW_ADD_THEME;
            storageService.removeUserStorage(vkId, USER_MENU);
        } else if (command.equals("/admin")
                && context.getRole().isAdmin()) { // валидация что юзер имеет роль админ
            nextStep = ADMIN_MENU;
            storageService.removeUserStorage(vkId, USER_MENU);
        } else { // любой другой ввод
            throw new ProcessInputException("Введена неверная команда...");
        }
    }
}

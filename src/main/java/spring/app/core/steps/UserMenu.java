package spring.app.core.steps;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.Review;
import spring.app.model.StudentReview;
import spring.app.model.User;
import spring.app.service.abstraction.ReviewService;
import spring.app.service.abstraction.StorageService;
import spring.app.service.abstraction.StudentReviewService;
import spring.app.service.abstraction.UserService;
import spring.app.util.StringParser;

import javax.persistence.NoResultException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.*;

@Component
public class UserMenu extends Step {

    private final StorageService storageService;
    private final ReviewService reviewService;
    private final StudentReviewService studentReviewService;
    private final UserService userService;

    @Value("${review.point_for_empty_review}")
    int pointForEmptyReview;

    public UserMenu(StorageService storageService, ReviewService reviewService,
                    StudentReviewService studentReviewService, UserService userService) {
        this.storageService = storageService;
        this.reviewService = reviewService;
        this.studentReviewService = studentReviewService;
        this.userService = userService;
    }

    @Override
    public void enter(BotContext context) {
        Integer vkId = context.getVkId();
        User user = context.getUser();
        // проверка, есть ли у юзера открытые ревью где он ревьюер - кнопка начать ревью
        List<Review> userReviews = reviewService.getOpenReviewsByReviewerVkId(vkId);
        // проверка, записан ли он на другие ревью.
        Review studentReview = null;
        List<StudentReview> openStudentReview = studentReviewService.getOpenReviewByStudentVkId(vkId);
        if(!openStudentReview.isEmpty()) {
            if (openStudentReview.size()>1) {
                //TODO:впилить запись в логи - если у нас у студента 2 открытых ревью которые он сдает - это не нормально
            }
            studentReview = openStudentReview.get(0).getReview();
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
        text = String.format("Привет, %s!\nВы можете сдавать и принимать p2p ревью по разным темам, для удобного использования бота воспользуйтесь кнопками + скрин. \nНа данный момент у вас %d RP (Review Points) для сдачи ревью.\nRP используются для записи на ревью, когда вы хотите записаться на ревью вам надо потратить RP, первое ревью бесплатное, после его сдачи вы сможете зарабатывать RP принимая ревью у других. Если вы приняли 1 ревью то получаете 2 RP, если вы дали возможность вам сдать, но никто не записался на сдачу (те вы пытались провести ревью, но не было желающих) то вы получаете 1 RP.", user.getFirstName(), user.getReviewPoint());
        List<String> currentStorage = storageService.getUserStorage(vkId, USER_MENU);
        if (currentStorage != null) {
            //если кому потребуется выводить кучу текста - пусть стримами бегаем по элементам. А пока тут нужен только первый
            text = currentStorage.get(0) + text;
        }
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException {
        Integer vkId = context.getVkId();
        String command = StringParser.toWordsArray(context.getInput())[0];
        if (command.equals("начать")) { // (Начать прием ревью)
            // получаем список всех ревью, которые проводит пользователь
            List<Review> userReviews = reviewService.getOpenReviewsByReviewerVkId(vkId);
            if (!userReviews.isEmpty()) {
                // берем из списка то ревью, которое начинается позже текущего времени с самой поздней датой
                Review nearestReview = userReviews.stream()
                        .filter(review -> review.getDate().isBefore(LocalDateTime.now()))
                        .max(Comparator.comparing(Review::getDate))
                        .orElse(null);
                // если такого ревью нет, сообщаем, что начать ревью можно не раньше установленного в самом ревью времени
                if (nearestReview == null) {
                    String notification = "Начать ревью можно не раньше установленного в самом ревью времени";
                    throw new ProcessInputException(notification);
                    // если ревью есть в этом временном диапазоне
                } else {
                    // то смотрим записан ли кто-то на него
                    Long reviewId = nearestReview.getId();
                    List<User> students = userService.getStudentsByReviewId(reviewId);
                    if (!students.isEmpty()) {
                        // если кто-то записан на ревью, то сохраняем reviewId в STORAGE
                        storageService.updateUserStorage(vkId, USER_MENU, Arrays.asList(reviewId.toString()));
                        nextStep = USER_START_REVIEW_HANGOUTS_LINK;
                    } else {
                        // если никто не записался на ревью, то добавялем очки пользователю и закрываем ревью
                        nearestReview.setOpen(false);
                        reviewService.updateReview(nearestReview);
                        User user = userService.getByVkId(vkId);
                        user.setReviewPoint(user.getReviewPoint() + pointForEmptyReview);
                        userService.updateUser(user);
                        throw new ProcessInputException(String.format("На твое ревью никто не записался, ты получаешь 1 RP.\nТвой баланс: %d RP", user.getReviewPoint()));
                    }
                }
            } else {
                // если пользователь не проводит ревью, то показываем сообщение
                throw new ProcessInputException("Ты еще не объявлял о принятии ревью!\n Сначала ты должен его объвить, для этого нажми на кнопку \"Принять ревью\" и следуй дальнейшим указаниям.");
            }
        } else if (command.equals("отменить")) { // (Отменить ревью)
            nextStep = USER_CANCEL_REVIEW;
            storageService.removeUserStorage(vkId, USER_MENU);
        } else if (command.equals("сдать")) { // (Сдать ревью)
            nextStep = USER_PASS_REVIEW_ADD_THEME;
            storageService.removeUserStorage(vkId, USER_MENU);
        } else if (command.equals("принять")) { // (Принять ревью)
            nextStep = USER_TAKE_REVIEW_ADD_THEME;
            storageService.removeUserStorage(vkId, USER_MENU);
        } else if (command.equals("/admin")) {

            if (context.getRole().isAdmin()) { // валидация что юзер имеет роль админ
                nextStep = ADMIN_MENU;
                storageService.removeUserStorage(vkId, USER_MENU);
            } else {
                throw new ProcessInputException("Недостаточно прав для выполнения команды!");
            }

        } else {
            // любой другой ввод
            throw new ProcessInputException("Введена неверная команда...");
        }
    }
}

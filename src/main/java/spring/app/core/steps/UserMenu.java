package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.Review;
import spring.app.model.User;
import spring.app.service.abstraction.ReviewService;
import spring.app.util.StringParser;

import javax.persistence.NoResultException;
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
        StringBuilder keys = HEADER_FR;
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

    @Override
    public void processInput(BotContext context) throws ProcessInputException {
        String command = StringParser.toWordsArray(context.getInput())[0];
        if (command.equals("начать")) { // (Начать прием ревью)
//            nextStep = ; TODO
        } else if (command.equals("отменить")) { // (Отменить ревью)
//            nextStep = ; TODO
        } else if (command.equals("сдать")) { // (Сдать ревью)
//            nextStep = ; TODO
        } else if (command.equals("принять")) { // (Принять ревью)
//            nextStep = ; TODO
        } else if (command.equals("/admin")
                && context.getRole().isAdmin()) { // валидация что юзер имеет роль админ
            nextStep = ADMIN_MENU;
        } else { // любой другой ввод
            throw new ProcessInputException("Введена неверная команда...");
        }
    }
}

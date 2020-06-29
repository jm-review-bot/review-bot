package spring.app.core.steps;

import org.springframework.beans.factory.annotation.Autowired;
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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.*;

@Component
public class UserMenu extends Step {
    @Value("${review.point_for_empty_review}")
    private int pointForEmptyReview;

    @Autowired
    private StorageService storageService;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private UserService userService;
    @Autowired
    private StudentReviewService studentReviewService;

    public UserMenu(String text, String keyboard) {
        super(text, keyboard);
    }

    public UserMenu() {
        //у шага нет статического текста, но есть статические(видимые независимо от юзера) кнопки
        super("", DEF_USER_MENU_KB);
    }

    @Override
    public void enter(BotContext context) {
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
                List<Review> overdueReviews = userReviews.stream()
                        .filter(review -> review.getDate().isBefore(LocalDateTime.now()))
                        .sorted(Comparator.comparing(Review::getDate))
                        .collect(Collectors.toList());

                int overdueReviewsCount = overdueReviews.size();

                if (overdueReviewsCount == 0) {
                    // если такого ревью нет, сообщаем, что начать ревью можно не раньше установленного в самом ревью времени
                    String notification = "Начать ревью можно не раньше установленного в самом ревью времени";
                    throw new ProcessInputException(notification);
                } else if (overdueReviewsCount == 1) {
                    // если ревью есть в этом временном диапазоне
                    Review nearestReview = overdueReviews.get(0);
                    // то смотрим записан ли кто-то на него
                    Long reviewId = nearestReview.getId();
                    List<User> students = userService.getStudentsByReviewId(reviewId);
                    if (!students.isEmpty()) {
                        // если кто-то записан на ревью, то сохраняем reviewId в STORAGE
                        storageService.updateUserStorage(vkId, USER_MENU, Arrays.asList(reviewId.toString()));
                        //теперь мы просто сохраняем юзеру его значения
                        sendUserToNextStep(context, USER_START_REVIEW_HANGOUTS_LINK);
                    } else {
                        // если никто не записался на ревью, то добавялем очки пользователю и закрываем ревью
                        nearestReview.setOpen(false);
                        reviewService.updateReview(nearestReview);
                        User user = userService.getByVkId(vkId);
                        user.setReviewPoint(user.getReviewPoint() + pointForEmptyReview);
                        userService.updateUser(user);
                        throw new ProcessInputException(String.format("На твое ревью никто не записался, ты получаешь 1 RP.\nТвой баланс: %d RP", user.getReviewPoint()));
                    }
                } else {
                    storageService.updateUserStorage(
                            vkId, USER_START_CHOOSE_REVIEW,
                            overdueReviews.stream().map(review -> review.getId().toString()).collect(Collectors.toList())
                    );
                    sendUserToNextStep(context, USER_START_CHOOSE_REVIEW);
                }
            } else {
                // если пользователь не проводит ревью, то показываем сообщение
                throw new ProcessInputException("Ты еще не объявлял о принятии ревью!\n Сначала ты должен его объвить, для этого нажми на кнопку \"Принять ревью\" и следуй дальнейшим указаниям.");
            }
        } else if (command.equals("отмена")) { // (Отменить ревью)
            sendUserToNextStep(context, USER_CANCEL_REVIEW);
            storageService.removeUserStorage(vkId, USER_MENU);
        } else if (command.equals("отменить")) { // (Отменить ревью)
            // сюда метод отмены ревью создателем
            storageService.removeUserStorage(vkId, USER_MENU);
        } else if (command.equals("сдать")) { // (Сдать ревью)
            sendUserToNextStep(context, USER_PASS_REVIEW_ADD_THEME);
            storageService.removeUserStorage(vkId, USER_MENU);
        } else if (command.equals("принять")) { // (Принять ревью)
            sendUserToNextStep(context, USER_TAKE_REVIEW_ADD_THEME);
            storageService.removeUserStorage(vkId, USER_MENU);
        } else if (command.equals("/admin")) {

            if (context.getRole().isAdmin()) { // валидация что юзер имеет роль админ
                sendUserToNextStep(context, ADMIN_MENU);
                storageService.removeUserStorage(vkId, USER_MENU);
            } else {
                throw new ProcessInputException("Недостаточно прав для выполнения команды!");
            }
        } else { // любой другой ввод
            throw new ProcessInputException("Введена неверная команда...");
        }
    }

    @Override
    public String getDynamicText(BotContext context) {
        Integer vkId = context.getVkId();
        User user = context.getUser();
        List<String> currentStorage = storageService.getUserStorage(vkId, USER_MENU);
        String textUserCancelMenuStep = String.valueOf(storageService.getUserStorage(vkId, USER_CANCEL_REVIEW));
        String text = "";
        if (!(textUserCancelMenuStep.equals("null"))) {
            text += textUserCancelMenuStep + "\n\n";
        }
        text += String.format(
                "Привет, %s!\nВы можете сдавать и принимать p2p ревью по разным темам, " +
                "для удобного использования бота воспользуйтесь кнопками + скрин.\n" +
                "На данный момент у вас %d RP (Review Points) для сдачи ревью.\n" +
                "RP используются для записи на ревью, когда вы хотите записаться на ревью " +
                "вам надо потратить RP, первое ревью бесплатное, после его сдачи вы сможете зарабатывать RP " +
                "принимая ревью у других. Если вы приняли 1 ревью то получаете 2 RP, " +
                "если вы дали возможность вам сдать, но никто не записался на сдачу " +
                "(те вы пытались провести ревью, но не было желающих) то вы получаете 1 RP."
                , user.getFirstName(), user.getReviewPoint());
        if (currentStorage != null) {
            //если кому потребуется выводить кучу текста - пусть стримами бегает по элементам. А пока тут нужен только первый
            text = currentStorage.get(0) + text;
            storageService.removeUserStorage(vkId, USER_MENU);
            storageService.removeUserStorage(vkId, USER_CANCEL_REVIEW);
        }
        return text;
    }

    @Override
    public String getDynamicKeyboard(BotContext context) {
        Integer vkId = context.getVkId();
        // проверка, есть ли у юзера открытые ревью где он ревьюер - кнопка начать ревью
        List<Review> userReviews = reviewService.getOpenReviewsByReviewerVkId(vkId);
        // проверка, записан ли он на другие ревью.
        Review studentReview = null;
        List<StudentReview> openStudentReview = studentReviewService.getOpenReviewByStudentVkId(vkId);
        if (!openStudentReview.isEmpty()) {
            if (openStudentReview.size() > 1) {
                //TODO:впилить запись в логи - если у нас у студента 2 открытых ревью которые он сдает - это не нормально
            }
            studentReview = openStudentReview.get(0).getReview();
        }
        // формируем блок кнопок
        StringBuilder keys = new StringBuilder();
        boolean isEmpty = true;//если не пустое - добавляем разделитель
        //кнопка начать ревью для ревьюера
        if (!userReviews.isEmpty()) {
            keys
                    .append(this.getRowDelimiterString())
                    .append(REVIEW_START_FR)
                    .append(this.getRowDelimiterString())
                    .append(REVIEW_CANCEL_FR);
            isEmpty = false;
        }
        //кнопка отмены ревью для студента
        if (studentReview != null) {
            if (!isEmpty) {
                keys
                        .append(this.getRowDelimiterString())
                        .append(DELETE_STUDENT_REVIEW);
                isEmpty = false;
            }
        }
        if (!isEmpty) {
            return keys.toString();
        } else {
            //если динамические кнопки не нужны - ничего не возвращаем
            return "";
        }
    }
}

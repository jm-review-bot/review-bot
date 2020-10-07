package spring.app.core.steps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.NoDataEnteredException;
import spring.app.exceptions.NoNumbersEnteredException;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.StudentReview;
import spring.app.service.abstraction.StorageService;
import spring.app.service.abstraction.StudentReviewService;

import java.util.ArrayList;

import static spring.app.core.StepSelector.ADMIN_EDIT_REVIEW_GET_REVIEW_INFO;
import static spring.app.core.StepSelector.ADMIN_EDIT_REVIEW_GET_REVIEW_LIST;
import static spring.app.core.StepSelector.ADMIN_EDIT_REVIEW_CHANGE_REVIEW;
import static spring.app.util.Keyboards.PASS_OR_NOT_PASS_OR_BACK;

/**
 * @author AkiraRokudo on 15.05.2020 in one of sun day
 */
@Component
public class AdminEditReviewChangeReview extends Step {

    private StorageService storageService;
    private StudentReviewService studentReviewService;

    @Autowired
    public AdminEditReviewChangeReview(StorageService storageService, StudentReviewService studentReviewService) {
        super("Выберите статус\n", PASS_OR_NOT_PASS_OR_BACK);
        this.storageService = storageService;
        this.studentReviewService = studentReviewService;
    }

    @Override
    public void enter(BotContext context) {
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException, NoNumbersEnteredException, NoDataEnteredException {
        //если выбор нормальный - возвращаем на шаг reviewList. в Котором выводим инфу об успешной смене инфы
        //если не нормальный - оставляем на этом
        //если мы выбрали назад - возвращаемся на инфу по ревью
        String currentInput = context.getInput();
        Integer vkId = context.getVkId();
        String wordInput = currentInput.toLowerCase();
        if (wordInput.equals("назад")) {
            sendUserToNextStep(context, ADMIN_EDIT_REVIEW_GET_REVIEW_INFO);
        } else if (wordInput.equals("пройдено") || wordInput.equals("не пройдено")) {
            boolean isEqualsPass = wordInput.equals("пройдено");
            String selectedStudentReview = storageService.getUserStorage(vkId, ADMIN_EDIT_REVIEW_GET_REVIEW_LIST).get(0);
            StudentReview studentReview = studentReviewService.getStudentReviewById(Long.parseLong(selectedStudentReview));
            if (studentReview.getIsPassed() ^ isEqualsPass) { //см. else станет понятно.
                studentReview.setIsPassed(isEqualsPass);
                studentReviewService.updateStudentReview(studentReview);
                //хранить нам ничего не надо, а вот уведомить лист - да
                storageService.updateUserStorage(vkId, ADMIN_EDIT_REVIEW_CHANGE_REVIEW, new ArrayList<>());
                storageService.removeUserStorage(vkId, ADMIN_EDIT_REVIEW_GET_REVIEW_INFO);
                sendUserToNextStep(context, ADMIN_EDIT_REVIEW_GET_REVIEW_LIST);
            } else {
                //ругаемся
                throw new ProcessInputException("Ревью уже находится в этом статусе");
            }
        } else {
            throw new ProcessInputException("Введена неверная команда...");
        }
    }

    @Override
    public String getDynamicText(BotContext context) {
        return "";
    }

    @Override
    public String getDynamicKeyboard(BotContext context) {
        return "";
    }
}

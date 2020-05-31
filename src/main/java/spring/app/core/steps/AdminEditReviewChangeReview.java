package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.NoDataEnteredException;
import spring.app.exceptions.NoNumbersEnteredException;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.StudentReview;
import spring.app.service.abstraction.StorageService;
import spring.app.util.StringParser;

import java.util.ArrayList;
import java.util.List;

import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.BACK_KB;

/**
 * @author AkiraRokudo on 15.05.2020 in one of sun day
 */
@Component
public class AdminEditReviewChangeReview extends Step {

    @Override
    public void enter(BotContext context) {
        StringBuilder stringBuilder = new StringBuilder("Выберите статус\n")
                .append("[1] Пройдено\n")
                .append("[2] Не пройдено\n");
        text = stringBuilder.toString();
        keyboard = BACK_KB;
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException, NoNumbersEnteredException, NoDataEnteredException {
        //если выбор нормальный - возвращаем на шаг reviewList. в Котором выводим инфу об успешной смене инфы
        //если не нормальный - оставляем на этом
        //если мы выбрали назад - возвращаемся на инфу по ревью
        String currentInput = context.getInput();
        StorageService storageService = context.getStorageService();
        Integer vkId = context.getVkId();
        String wordInput = StringParser.toWordsArray(currentInput)[0];
        if (wordInput.equals("назад")) {
            nextStep = ADMIN_EDIT_REVIEW_GET_REVIEW_INFO;
        } else if (StringParser.isNumeric(wordInput)) {
            Integer selectedNumber = Integer.parseInt(wordInput);
            if (selectedNumber == 1 || selectedNumber == 2) {
                String selectedStudentReview = storageService.getUserStorage(vkId, ADMIN_EDIT_REVIEW_GET_REVIEW_INFO).get(0);
                StudentReview studentReview = context.getStudentReviewService().getStudentReviewById(Long.parseLong(selectedStudentReview));
                if (studentReview.getPassed() && selectedNumber == 1 ||
                        !studentReview.getPassed() && selectedNumber == 2) {
                    //раз мы ругаемся, следующий шаг будет этим же, для корректного ввода
                    throw new ProcessInputException("Ревью уже находится в этом статусе");
                } else {
                    studentReview.setPassed(selectedNumber == 1);
                    List<String> newStatus = new ArrayList<>();
                    newStatus.add(Integer.toString(selectedNumber));
                    storageService.updateUserStorage(vkId, ADMIN_EDIT_REVIEW_CHANGE_REVIEW, newStatus);
                    storageService.removeUserStorage(vkId, ADMIN_EDIT_REVIEW_GET_REVIEW_INFO);
                    context.getStudentReviewService().updateStudentReview(studentReview);
                    nextStep = ADMIN_EDIT_REVIEW_GET_REVIEW_LIST;
                }
            } else {
                throw new ProcessInputException("Введите 1 или 2 для смены статуса");
            }
        } else {
            throw new ProcessInputException("Введена неверная команда...");
        }
    }
}

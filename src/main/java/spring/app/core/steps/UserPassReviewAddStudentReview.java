package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.ProcessInputException;
import spring.app.util.StringParser;


import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.USER_GO_MENU;

@Component
public class UserPassReviewAddStudentReview extends Step {

    @Override
    public void enter(BotContext context) {
        //получаю дату ревью с прошлого шага
        String date = getUserStorage(context.getVkId(), USER_PASS_REVIEW_ADD_STUDENT_REVIEW).get(0);
        text = String.format("Ты записан на ревью в %s, для отмены записи - в меню нажми кнопку \"Отменить ревью\". " +
                "В момент, когда ревью начнётся - тебе придёт сюда ссылка для подключения к разговору.", date);

        keyboard = USER_GO_MENU;
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException {
            String command = StringParser.toWordsArray(context.getInput())[0];
            if ("вернуться".equals(command)) {
                nextStep = USER_MENU;
            } else if ("/start".equals(command)) {
                nextStep = START;
            } else {
                throw new ProcessInputException("Введена неверная команда...");
            }
            removeUserStorage(context.getVkId(), USER_PASS_REVIEW_ADD_STUDENT_REVIEW);
    }
}
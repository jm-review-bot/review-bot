package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.ProcessInputException;
import spring.app.util.StringParser;


import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.USER_MENU_KB;

@Component
public class UserPassReviewAddStudentReview extends Step {

    @Override
    public void enter(BotContext context) {
        //получаю дату ревью с прошлого шага
        String date = getUserStorage(context.getVkId(), USER_PASS_REVIEW_GET_LIST_REVIEW).get(0);
        text = String.format("Ты записан на ревью в %s, для отмены записи - в меню нажми кнопку \"Отменить ревью\". " +
                "В момент, когда ревью начнётся - тебе придёт сюда ссылка для подключения к разговору.", date);

        keyboard = USER_MENU_KB;
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException {
        String command = StringParser.toWordsArray(context.getInput())[0];
        if ("главное".equals(command)) {
            nextStep = USER_MENU;
            removeUserStorage(context.getVkId(), USER_PASS_REVIEW_GET_LIST_REVIEW);
        } else if ("/start".equals(command)) {
            nextStep = START;
            removeUserStorage(context.getVkId(), USER_PASS_REVIEW_GET_LIST_REVIEW);
        } else {
            throw new ProcessInputException("Введена неверная команда...");
        }
    }
}
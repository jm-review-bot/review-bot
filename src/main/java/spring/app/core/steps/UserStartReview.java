package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.NoDataEnteredException;
import spring.app.exceptions.NoNumbersEnteredException;
import spring.app.exceptions.ProcessInputException;
import spring.app.util.StringParser;

import static spring.app.core.StepSelector.START;
import static spring.app.core.StepSelector.USER_MENU;
import static spring.app.util.Keyboards.BACK_KB;

@Component
public class UserStartReview extends Step {

    @Override
    public void enter(BotContext context) {
        text = "Чтобы начать ревью, необходимо создать разговор в hangouts, для этого перейди по ссылке https://hangouts.google.com/hangouts/_/ ," +
                " подключись к диалогу нажми \"пригласить участников\" и скопируй ссылку." +
                " Важно! Не копируй ссылку из браузерной строки, копировать надо именно ссылку из модального окна приглашения участников." +
                " Эту ссылку отправь ответным сообщением. ";
        keyboard = BACK_KB;
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException, NoNumbersEnteredException, NoDataEnteredException {
        Integer vkId = context.getVkId();
        String userInput = context.getInput();
        if (userInput.equalsIgnoreCase("назад") || userInput.equalsIgnoreCase("/start")) {
            nextStep = USER_MENU;
        } else if (StringParser.isHangoutsLink(userInput)) {
            nextStep = START;

            // берем ближайшее ревью
            // отправляем ссылку на ревью его участникам
            // переходим на следуюший шаг
        } else {
            nextStep = START;

            // если ссылка невалидна
            // сообщить об ошибке, повторить ввод на этом шаге
        }
    }
}

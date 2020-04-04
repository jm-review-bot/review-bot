package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.NoDataEnteredException;
import spring.app.exceptions.NoNumbersEnteredException;
import spring.app.exceptions.ProcessInputException;

import static spring.app.core.StepSelector.USER_TAKE_REVIEW_ADD_THEME;
import static spring.app.util.Keyboards.START_KB;

@Component
public class UserStartReviewStepTwo extends Step {

    @Override
    public void enter(BotContext context) {
        text = "На твоём ревью сегодня присутствуют \n" +
                "[1] Андрей Иванов (ссылка на профиль в вк)\n" +
                "[2] Сергей Петров (ссылка на профиль в вк)\n" +
                "[3] Иван Иванович (ссылка на профиль в вк)\n" +
                "\n" +
                "Система будет выдавать вопрос из спика, который тебе необходимо задать, ты сам выбираешь кому задать данный вопрос, если человек не отвечает на вопрос - отправь его номер в чат и задай вопрос другому человеку, если он тоже не ответит - так же напиши его номер, если кто-то ответил правильно на вопрос напиши его номер и добавь + после номера. \n" +
                "\n" +
                "Правильный ответ на данный вопрос \"здесь текст ответа на вопрос\"\n" +
                "\n" +
                "Если Александр и Сергей не ответили на вопрос, а Иван ответил, то надо написать в чат 1 2 3+\n" +
                "Если Андрей ответил на вопрос то остальным мы его не задаём и пишем в чат 1+\n" +
                "Если Андрей не ответил на вопрос, а Сергей ответил то Ивану вопрос не задаём и пишем в чат 1 2+\n" +
                "Если никто не ответил правильно на вопрос - написать в чат 1 2 3 \n" +
                "\n" +
                "Вопрос задаётся до ПЕРВОГО человека ответившего правильно";
        keyboard = START_KB;
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException, NoNumbersEnteredException, NoDataEnteredException {
        String userInput = context.getInput();
        if (userInput.equalsIgnoreCase("назад")) { // TODO заглушка
            nextStep = USER_TAKE_REVIEW_ADD_THEME;
        }
    }
}

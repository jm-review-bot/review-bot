package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.ProcessInputException;

import static spring.app.core.StepSelector.USER_PASS_REVIEW;
import static spring.app.core.StepSelector.USER_TAKE_REVIEW_ADD_THEME;
import static spring.app.util.Keyboards.USER_START_KB;

@Component
public class UserMenu extends Step {

    @Override
    public void enter(BotContext context) {

        /*// TODO проверку из ТЗ
        text = "Пользователь с таким vk id не найден в базе. Обратитесь к Герману Севостьянову или Станиславу Сорокину";
        */

        text = "Этот бот создан для p2p ревью по разным темам, для удобного использования бота воспользуйтесь кнопками + скрин. " +
                "На данный момент у вас 0 очков для сдачи ревью (review points/ RP). " +
                "RP используются для того, чтобы записаться на ревью, когда вы хотите записаться на ревью вам надо потратить RP, " +
                "первое ревью бесплатное, после его сдачи вы сможете зарабатывать RP принимая ревью у других. " +
                "Если вы приняли 1 ревью то получаете 2 RP, если вы дали возможность вам сдать, " +
                "но никто не записался на сдачу (те вы пытались провести ревью, но не было желающих) то вы получаете 1 RP.  ";
        keyboard = USER_START_KB;
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException {
        String[] commands = {"Сдать ревью", "Принять ревью"};
        String userInput = context.getInput();
        if (userInput.equals(commands[0])) {
            nextStep = USER_PASS_REVIEW;
        } else if (userInput.equals(commands[1])) {
            nextStep = USER_TAKE_REVIEW_ADD_THEME;
        } else {
            keyboard = USER_START_KB;
            throw new ProcessInputException("Введена неверная команда...");
        }
    }
}

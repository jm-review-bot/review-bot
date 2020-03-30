package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.User;

import static spring.app.util.Keyboards.*;

@Component
public class UserMenu extends Step {

    @Override
    public void enter(BotContext context) {
        Integer vkId = context.getVkId();
        User user = context.getUser();

        String output = String.format("Привет, %s!\nВы можете сдавать и принимать p2p ревью по разным темам, для удобного использования бота воспользуйтесь кнопками + скрин. \nНа данный момент у вас %s очков RP (Review Points) для сдачи ревью.\nRP используются для записи на ревью, когда вы хотите записаться на ревью вам надо потратить RP, первое ревью бесплатное, после его сдачи вы сможете зарабатывать RP принимая ревью у других. Если вы приняли 1 ревью то получаете 2 RP, если вы дали возможность вам сдать, но никто не записался на сдачу (те вы пытались провести ревью, но не было желающих) то вы получаете 1 RP.", user.getReviewPoint().toString());



        keyboard = HEADER_FR
                .append(REVIEW_START_FR)
                .append(ROW_DELIMETER_FR)
                .append(REVIEW_CANCEL_FR)
                .append(ROW_DELIMETER_FR)
                .append(USER_MENU_FR)
                .append(FOOTER_FR)
                .toString();
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException {

    }
}

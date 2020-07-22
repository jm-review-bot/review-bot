package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.NoDataEnteredException;
import spring.app.exceptions.NoNumbersEnteredException;
import spring.app.exceptions.ProcessInputException;

import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.*;

@Component
public class ExaminerGetInfoLastReview extends Step{

    public ExaminerGetInfoLastReview() {
        super("", EDIT_OLD_OR_ADD_NEW + ROW_DELIMETER_FR + DEF_BACK_KB);
    }

    @Override
    public void enter(BotContext context) {

    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException, NoNumbersEnteredException, NoDataEnteredException {

    }

    @Override
    public String getDynamicText(BotContext context) {
        return "done";
    }

    @Override
    public String getDynamicKeyboard(BotContext context) {
        return "";
    }
}

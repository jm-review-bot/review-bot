package spring.app.core.steps;

import spring.app.core.BotContext;
import spring.app.core.StepSelector;
import spring.app.exceptions.NoDataEnteredException;
import spring.app.exceptions.NoNumbersEnteredException;
import spring.app.exceptions.ProcessInputException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public abstract class Step implements Storage {
    protected String text;
    protected String keyboard;
    protected StepSelector nextStep;

    // абстрактные методы которые должны быть переопределены в каждом Step
    public abstract void enter(BotContext context);

    public abstract void processInput(BotContext context) throws ProcessInputException, NoNumbersEnteredException, NoDataEnteredException;

    public StepSelector nextStep() {
        return nextStep;
    }

    public String getText() {
        return text;
    }

    public String getKeyboard() {
        return keyboard;
    }
}

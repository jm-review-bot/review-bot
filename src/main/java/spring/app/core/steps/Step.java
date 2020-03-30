package spring.app.core.steps;

import spring.app.core.BotContext;
import spring.app.core.StepSelector;
import spring.app.exceptions.ProcessInputException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Step {

    protected String text;
    protected String keyboard;
    protected StepSelector nextStep;
    protected static final Map<Integer, Map<StepSelector, List<String>>> storage = new HashMap<>();

    // абстрактные методы которые должны быть переопределены в каждом Step
    public abstract void enter(BotContext context);

    public abstract void processInput(BotContext context) throws ProcessInputException;

    public StepSelector nextStep() {
        return nextStep;
    }

    public String getText() {
        return text;
    }

    public String getKeyboard() {
        return keyboard;
    }

    public static Map<Integer, Map<StepSelector, List<String>>> getStorage() {
        return storage;
    }
}

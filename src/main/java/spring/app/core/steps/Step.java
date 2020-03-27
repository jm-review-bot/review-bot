package spring.app.core.steps;

import spring.app.core.BotContext;
import spring.app.core.StepSelector;
import spring.app.exceptions.ProcessInputException;

public abstract class Step {
    protected String text;
    protected String keyboard;
    protected StepSelector nextStep;

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
}

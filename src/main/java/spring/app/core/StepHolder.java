package spring.app.core;

import org.springframework.stereotype.Component;
import spring.app.core.steps.Step;

import java.util.HashMap;
import java.util.Map;

@Component
public class StepHolder {
    private final Map<StepSelector, Step> steps = new HashMap<>();

    public StepHolder() {
    }

    public Map<StepSelector, Step> getSteps() {
        return steps;
    }
}
package spring.app.core;

import spring.app.core.steps.Step;

import java.util.HashMap;
import java.util.Map;

public class StepHolder {
    public static final Map<StepSelector, Step> steps = new HashMap<>();
}

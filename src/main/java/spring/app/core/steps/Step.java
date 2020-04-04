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

public abstract class Step {
    protected String text;
    protected String keyboard;
    protected StepSelector nextStep;
    /** Мапа для хранения данных, структурированных по vkId юзера и указанному шагу */
    private static final ConcurrentMap<Integer, Map<StepSelector, List<String>>> STORAGE = new ConcurrentHashMap<>();

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

    /**
     * Возвращает List<String> данного юзера соответствующий указанному шагу.
     * Если Искомых данных нет, возвращает null.
     */
    public List<String> getUserStorage(Integer vkId, StepSelector step) {
        try {
            return STORAGE.get(vkId).get(step);
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * Сохраняет указанный List<String> в ячейку данному юзеру, для указанного шага.
     * Создает пустой Map (ячейку) для данного юзера, если раньше для него не было создана ячейка.
     */
    public void updateUserStorage(Integer vkId, StepSelector step, List<String> listToSave) {
        STORAGE.putIfAbsent(vkId, new HashMap<>());
        Map<StepSelector, List<String>> userStorage = STORAGE.get(vkId);
        userStorage.put(step, listToSave);
        STORAGE.put(vkId, userStorage);
    }

    /**
     * Очищает сохраненные данные указанного шага для указанного юзера
     */
    public void removeUserStorage(Integer vkId, StepSelector step) {
        if (getUserStorage(vkId, step) != null) {
            STORAGE.get(vkId).remove(step);
        }
    }
}

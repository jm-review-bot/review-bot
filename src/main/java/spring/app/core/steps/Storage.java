package spring.app.core.steps;

import spring.app.core.StepSelector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public interface Storage {

    /** Мапа для хранения данных, структурированных по vkId юзера и указанному шагу */
    ConcurrentMap<Integer, Map<StepSelector, List<String>>> STORAGE = new ConcurrentHashMap<>();

    /**
     * Возвращает List<String> данного юзера соответствующий указанному шагу.
     * Если Искомых данных нет, возвращает null.
     */
    default List<String> getUserStorage(Integer vkId, StepSelector step) {
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
    default void updateUserStorage(Integer vkId, StepSelector step, List<String> listToSave) {
        STORAGE.putIfAbsent(vkId, new HashMap<>());
        Map<StepSelector, List<String>> userStorage = STORAGE.get(vkId);
        userStorage.put(step, listToSave);
        STORAGE.put(vkId, userStorage);
    }

    /**
     * Очищает сохраненные данные указанного шага для указанного юзера
     */
    default void removeUserStorage(Integer vkId, StepSelector step) {
        if (getUserStorage(vkId, step) != null) {
            STORAGE.get(vkId).remove(step);
        }
    }

    // метод который полностью очищает кэш
    default void clearStorage(){
        STORAGE.clear();
    }

    // метод для удаления из кэша пользователей, после того как они будут удалены админом
    default void clearUsersOfStorage(List<String> usersToDelete){
        usersToDelete.forEach(userIdString ->
                STORAGE.keySet().remove(Long.parseLong(userIdString))
        );
    }
}

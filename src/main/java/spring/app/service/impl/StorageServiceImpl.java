package spring.app.service.impl;

import org.springframework.stereotype.Service;
import spring.app.core.StepSelector;
import spring.app.service.abstraction.StorageService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class StorageServiceImpl implements StorageService {

    /**
     * Мапа для хранения данных, структурированных по vkId юзера и указанному шагу
     */

    private static final ConcurrentMap<Integer, Map<StepSelector, List<String>>> STORAGE = new ConcurrentHashMap<>();


    /**
     * Возвращает List<String> данного юзера соответствующий указанному шагу.
     * Если Искомых данных нет, возвращает null.
     */

    @Override
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

    @Override
    public void updateUserStorage(Integer vkId, StepSelector step, List<String> listToSave) {
        STORAGE.putIfAbsent(vkId, new HashMap<>());
        Map<StepSelector, List<String>> userStorage = STORAGE.get(vkId);
        userStorage.put(step, listToSave);
        STORAGE.put(vkId, userStorage);
    }

    /**
     * Очищает сохраненные данные указанного шага для указанного юзера
     */

    @Override
    public void removeUserStorage(Integer vkId, StepSelector step) {
        if (getUserStorage(vkId, step) != null) {
            STORAGE.get(vkId).remove(step);
        }
    }

    /**
     * метод который полностью очищает кэш
     */

    @Override
    public void clearStorage() {
        STORAGE.clear();
    }

    /**
     * метод для удаления из кэша пользователей, после того как они будут удалены админом
     */

    @Override
    public void clearUsersOfStorage(Integer userToDelete) {
        STORAGE.keySet().remove(userToDelete);
    }
}

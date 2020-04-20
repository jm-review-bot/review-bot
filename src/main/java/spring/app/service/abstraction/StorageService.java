package spring.app.service.abstraction;

import spring.app.core.StepSelector;

import java.util.List;

public interface StorageService {

    List<String> getUserStorage(Integer vkId, StepSelector step);

    void updateUserStorage(Integer vkId, StepSelector step, List<String> listToSave);

    void removeUserStorage(Integer vkId, StepSelector step);

}

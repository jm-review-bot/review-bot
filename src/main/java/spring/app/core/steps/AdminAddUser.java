package spring.app.core.steps;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.IncorrectVkIdsException;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.Role;
import spring.app.model.User;
import spring.app.service.abstraction.UserService;
import spring.app.service.abstraction.VkService;
import spring.app.util.StringParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.*;

@Component
public class AdminAddUser extends Step {
    private final Map<Integer, String> savedInputs = new HashMap<>();

    @Override
    public void enter(BotContext context) {
        Integer vkId = context.getVkId();
        String savedInput = savedInputs.get(vkId);

        if (savedInput == null || savedInput.isEmpty()) {
            text = "Введите ссылку на профиль пользователя.\nМожно добавить несколько пользователей, введя ссылки через пробел или запятую.";
            keyboard = BACK_KB;
        } else {
            text = savedInput;
            keyboard = BACK_KB;
            savedInputs.remove(vkId);
        }
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException {
        VkService vkService = context.getVkService();
        UserService userService = context.getUserService();
        Role userRole = context.getRoleService().getRoleByName("USER");
        Integer vkId = context.getVkId();
        String currentInput = context.getInput();

        List<String> parsedInput = StringParser.toVkIdsList(currentInput);
        // также он  может прислать команду отмены
        String wordInput = StringParser.toWordsArray(currentInput)[0];
        if (wordInput.equals("назад")
                || wordInput.equals("нет")
                || wordInput.equals("отмена")
                || wordInput.equals("/admin")) {
            savedInputs.remove(vkId);
            nextStep = ADMIN_MENU;
        } else if (wordInput.equals("/start")) {
            savedInputs.remove(vkId);
            nextStep = START;
        } else {
            // мы ожидаем от него ссылки на профили добавляемых  юзеров
            try {
                // получем список юзеров на основе запроса в VK
                List<User> addedUsersList = vkService.newUsersFromVk(parsedInput);
                // убираем из списка тех, кто уже есть в базе
                addedUsersList.removeIf(user -> userService.isExistByVkId(user.getVkId()));
                // проверяем что получился непустой список
                if (!addedUsersList.isEmpty()) {
                    // теперь можно начать формировать ответ
                    StringBuilder addedUserText = new StringBuilder("Были успешно добавлены пользователи:\n\n");

                    addedUsersList.forEach(user -> {
                        // сэтим юзерам роль ЮЗЕР, добавляем в базу и формируем строку с оветом
                        user.setRole(userRole);
                        userService.addUser(user);
                        addedUserText
                                .append("- ")
                                .append(user.getLastName())
                                .append(" ")
                                .append(user.getFirstName())
                                .append(", https://vk.com/id")
                                .append(user.getVkId())
                                .append("\n");

                    });
                    addedUserText.append("\nВы можете прислать еще ссылки на профили или вернуться в Меню, введя \"назад\".");
                    savedInputs.put(vkId, addedUserText.toString());
                    nextStep = ADMIN_ADD_USER;
                } else {
                    savedInputs.put(vkId, "Пользователь(и) уже в базе.");
                    nextStep = ADMIN_ADD_USER;
                }
            } catch (ClientException | ApiException | IncorrectVkIdsException e) {
                keyboard = BACK_KB;
                throw new ProcessInputException("Введены неверные данные. Таких пользователей не найдено...");
            }
        }

    }
}

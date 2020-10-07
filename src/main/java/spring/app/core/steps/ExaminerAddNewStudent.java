package spring.app.core.steps;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.IncorrectVkIdsException;
import spring.app.exceptions.NoDataEnteredException;
import spring.app.exceptions.NoNumbersEnteredException;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.User;
import spring.app.service.abstraction.StorageService;
import spring.app.service.abstraction.UserService;
import spring.app.service.abstraction.VkService;
import spring.app.util.StringParser;

import java.util.Arrays;

import static spring.app.core.StepSelector.EXAMINER_CHOOSE_METHOD_TO_ADD_STUDENT;
import static spring.app.core.StepSelector.EXAMINER_USERS_LIST_FROM_DB;
import static spring.app.core.StepSelector.EXAMINER_GET_INFO_LAST_REVIEW;
import static spring.app.util.Keyboards.DEF_BACK_KB;

@Component
public class ExaminerAddNewStudent extends Step {

    private final StorageService storageService;
    private final UserService userService;
    private final VkService vkService;

    public ExaminerAddNewStudent(StorageService storageService, VkService vkService, UserService userService) {
        super("Введите ссылку на профиль студента вконтакте.\n", DEF_BACK_KB);
        this.storageService = storageService;
        this.vkService = vkService;
        this.userService = userService;
    }

    @Override
    public void enter(BotContext context) {
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException, NoNumbersEnteredException, NoDataEnteredException {
        String currentInput = context.getInput();
        String parsedInput = StringParser.toVkId(currentInput);
        if (currentInput.equalsIgnoreCase("назад")) {
            sendUserToNextStep(context, EXAMINER_CHOOSE_METHOD_TO_ADD_STUDENT);
            storageService.removeUserStorage(context.getVkId(), EXAMINER_USERS_LIST_FROM_DB);
        } else if (parsedInput != null) {
            try {
                // получем юзера на основе запроса в VK
                User user = vkService.newUserFromVk(parsedInput);
                Integer studentVkId = user.getVkId();
                //проверим, что у нас нет такого юзера
                if (!userService.isExistByVkId(studentVkId)) {
                    throw new ProcessInputException("Студента с таким ID нет в базе.\n");
                } else {
                    // В хранилище шага EXAMINER_USERS_LIST_FROM_DB сохраняется ID студента - хранилище именно этого шага предназначено для хранения студенческого ID
                    sendUserToNextStep(context, EXAMINER_GET_INFO_LAST_REVIEW);
                    Long studentId = userService.getByVkId(studentVkId).get().getId();
                    storageService.updateUserStorage(context.getVkId(), EXAMINER_USERS_LIST_FROM_DB, Arrays.asList(studentId.toString()));
                }
            } catch (ClientException | ApiException | IncorrectVkIdsException e) {
                throw new ProcessInputException("Введены неверные данные. Такой пользователь не найден...");
            }
        } else {
            throw new ProcessInputException("Введена некрректная ссылка.");
        }
    }

    @Override
    public String getDynamicText(BotContext context) {
        return "";
    }

    @Override
    public String getDynamicKeyboard(BotContext context) {
        return "";
    }
}
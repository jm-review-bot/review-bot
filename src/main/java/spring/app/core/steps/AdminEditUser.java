package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.NoDataEnteredException;
import spring.app.exceptions.NoNumbersEnteredException;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.ReviewStatistic;
import spring.app.model.User;
import spring.app.service.abstraction.ReviewStatisticService;
import spring.app.service.abstraction.StorageService;
import spring.app.service.abstraction.UserService;

import java.util.List;

import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.*;

/**
 * @author AkiraRokudo on 27.05.2020 in one of sun day
 */
@Component
public class AdminEditUser extends Step {

    private final StorageService storageService;
    private final UserService userService;
    private final ReviewStatisticService reviewStatisticService;


    public AdminEditUser(StorageService storageService,
                         UserService userService,
                         ReviewStatisticService reviewStatisticService) {
        super("", "");
        this.storageService = storageService;
        this.userService = userService;
        this.reviewStatisticService = reviewStatisticService;
    }

    @Override
    public void enter(BotContext context) {

    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException, NoNumbersEnteredException, NoDataEnteredException {
        String inputText = context.getInput();
        Integer vkId = context.getVkId();
        if ("изменить имя".equals(inputText)) {
            sendUserToNextStep(context, ADMIN_INPUT_NEW_FULLNAME_EDITED_USER);
        } else if ("изменить вкИд".equals(inputText)) {
            sendUserToNextStep(context, ADMIN_INPUT_NEW_VKID_EDITED_USER);
        } else if ("Снять блок с ревью".equals(inputText)) {
            ReviewStatistic reviewStatistic = reviewStatisticService.getReviewStatisticByUserId(Long.parseLong(storageService.getUserStorage(vkId, ADMIN_USERS_LIST).get(0)));
            if (reviewStatistic != null && reviewStatistic.isReviewBlocked()) {
                sendUserToNextStep(context, ADMIN_UNBLOCK_USER_TAKE_REVIEW);
            } else {
                throw new ProcessInputException("Пользователь имеет возможность создавать ревью");
            }
            sendUserToNextStep(context, ADMIN_UNBLOCK_USER_TAKE_REVIEW);
        } else if ("Изменить роль".equals(inputText)) {
            sendUserToNextStep(context, ADMIN_EDIT_USER_GET_ROLES_LIST);
        } else if ("Назад".equals(inputText)) {
            storageService.removeUserStorage(vkId, ADMIN_USERS_LIST);
            sendUserToNextStep(context, ADMIN_USERS_LIST);
        } else {
            throw new ProcessInputException("Введена неверная команда...");
        }
    }

    @Override
    public String getDynamicText(BotContext context) {
        Integer vkId = context.getVkId();
        List<String> savedInput = storageService.getUserStorage(vkId, ADMIN_USERS_LIST);
        String text;

        if (savedInput != null) {
            Long userId = Long.parseLong(savedInput.get(0));
            User selectedUser = userService.getUserById(userId);
            ReviewStatistic reviewStatistic = reviewStatisticService.getReviewStatisticByUserId(userId);
            text = String.format(
                    "Вы выбрали %s %s (%s).\nСтатус ревью: %s.\nРоль пользователя: %s.\nВыберите действие",
                    selectedUser.getFirstName(),
                    selectedUser.getLastName(),
                    selectedUser.getVkId(),
                    reviewStatistic != null && reviewStatistic.isReviewBlocked() ? "заблокировано" : "доступно",
                    selectedUser.getRole().getName()
            );
        } else {
            text = "Изменение параметров выбранного пользователя невозможно. Нажмите 'Назад' чтобы вернуться к списку пользователей";
        }
        return text;
    }

    @Override
    public String getDynamicKeyboard(BotContext context) {
        String keyboard = "";
        Integer vkId = context.getVkId();
        List<String> savedInput = storageService.getUserStorage(vkId, ADMIN_USERS_LIST);

        // Кнопка для разблокировки пользователю возможности создания ревью
        ReviewStatistic reviewStatistic = reviewStatisticService.getReviewStatisticByUserId(Long.parseLong(savedInput.get(0)));
        if (reviewStatistic != null && reviewStatistic.isReviewBlocked()) {
            keyboard += CANCEL_BLOCK_FOR_TAKE_REVIEW + getRowDelimiterString();
        }

        // Набор кнопок для редактирования пользователя
        keyboard += EDITING_USER_OR_BACK;

        return keyboard;
    }
}

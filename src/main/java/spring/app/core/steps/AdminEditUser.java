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


    public AdminEditUser(StorageService storageService, UserService userService, ReviewStatisticService reviewStatisticService) {
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
            sendUserToNextStep(context, ADMIN_UNBLOCK_USER_TAKE_REVIEW);
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
            text = String.format("Вы выбрали %s %s (%s). Выберите действие", selectedUser.getFirstName(), selectedUser.getLastName(), selectedUser.getVkId());
        } else {
            text = "Изменение параметров выбранного пользователя невозможно. Нажмите 'Назад' чтобы вернуться к списку пользователей";
        }
        return text;
    }

    @Override
    public String getDynamicKeyboard(BotContext context) {
        Integer vkId = context.getVkId();
        List<String> savedInput = storageService.getUserStorage(vkId, ADMIN_USERS_LIST);
        String keyboard = (savedInput != null ? CHANGE_FULLNAME_VKID_EDITING_USER_OR_BACK : DEF_BACK_KB);
        ReviewStatistic reviewStatistic = reviewStatisticService.getReviewStatisticByUserVkId(vkId);
        if (reviewStatistic !=null && reviewStatistic.isReviewBlocked()) {
            keyboard += getRowDelimiterString() + CANCEL_BLOCK_FOR_TAKE_REVIEW;
        }
        return keyboard;
    }
}

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

import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.*;

@Component
public class AdminUnblockUserTakeReview extends Step {

    private final StorageService storageService;
    private final UserService userService;
    private final ReviewStatisticService reviewStatisticService;

    public AdminUnblockUserTakeReview(StorageService storageService,
                                      ReviewStatisticService reviewStatisticService,
                                      UserService userService) {
        super("", YES_NO_KB);
        this.storageService = storageService;
        this.reviewStatisticService = reviewStatisticService;
        this.userService = userService;
    }

    @Override
    public void enter(BotContext context) {
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException, NoNumbersEnteredException, NoDataEnteredException {
        String currentInput = context.getInput();
        Integer vkId = context.getVkId();
        if (currentInput.equalsIgnoreCase("да")) {
            reviewStatisticService.unblockTakingReviewForUser(Long.parseLong(storageService.getUserStorage(vkId, ADMIN_USERS_LIST).get(0)));
            storageService.removeUserStorage(vkId, ADMIN_USERS_LIST);
            sendUserToNextStep(context, ADMIN_MENU);
        } else if (currentInput.equalsIgnoreCase("нет")) {
            storageService.removeUserStorage(vkId, ADMIN_USERS_LIST);
            sendUserToNextStep(context, ADMIN_MENU);
        } else {
            throw new ProcessInputException("Введена неверная команда...");
        }
    }

    @Override
    public String getDynamicText(BotContext context) {
        Integer vkId = context.getVkId();
        Long studentId = Long.parseLong(storageService.getUserStorage(vkId, ADMIN_USERS_LIST).get(0));
        User student = userService.getUserById(studentId);
        ReviewStatistic reviewStatistic = reviewStatisticService.getReviewStatisticByUserId(studentId);
        return String.format("Вы уверены, что хотите снять блок с возможности создания ревью для пользователя %s %s?\nКоличество блокировок пользователя: %d.\nПоследняя причина блокировки:\n\n%s",
                student.getFirstName(),
                student.getLastName(),
                reviewStatistic.getCountBlocks(),
                reviewStatistic.getLastBlockReason());
    }

    @Override
    public String getDynamicKeyboard(BotContext context) {
        return "";
    }
}

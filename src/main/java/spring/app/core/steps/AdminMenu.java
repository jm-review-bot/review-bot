package spring.app.core.steps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.ProcessInputException;
import spring.app.service.abstraction.StorageService;

import java.util.List;

import static spring.app.core.StepSelector.START;
import static spring.app.core.StepSelector.ADMIN_CHOOSE_ACTION_FOR_REVIEW;
import static spring.app.core.StepSelector.ADMIN_CHOOSE_ACTION_FOR_USER;
import static spring.app.core.StepSelector.USER_MENU;
import static spring.app.core.StepSelector.ADMIN_MENU;
import static spring.app.util.Keyboards.DEF_ADMIN_MENU_KB;

@Component
public class AdminMenu extends Step {

    private final StorageService storageService;

    @Autowired
    public AdminMenu(StorageService storageService) {
        super("", DEF_ADMIN_MENU_KB);
        this.storageService = storageService;
    }

    @Override
    public void enter(BotContext context) {
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException {
        String command = context.getInput();
        switch (command) {
            case "Пользователи":
                if ("ADMIN".equals(context.getRole().getName())) {
                    sendUserToNextStep(context, ADMIN_CHOOSE_ACTION_FOR_USER);
                } else {
                    sendUserToNextStep(context, START);
                }
                break;
            case "/start":
                sendUserToNextStep(context, START);
                break;
            case "Ревью":
                if ("ADMIN".equals(context.getRole().getName())) {
                    sendUserToNextStep(context, ADMIN_CHOOSE_ACTION_FOR_REVIEW);
                } else {
                    sendUserToNextStep(context, START);
                }
                break;
            case "Главное меню":
                sendUserToNextStep(context, USER_MENU);
                break;
            default:
                throw new ProcessInputException("Введена неверная команда...");
        }
    }

    @Override
    public String getDynamicText(BotContext context) {
        Integer vkId = context.getVkId();
        List<String> stepContent = storageService.getUserStorage(vkId, ADMIN_MENU);
        if (stepContent != null) { // Если есть какая-либо информация для отображения пользователю
            storageService.removeUserStorage(vkId, ADMIN_MENU);
            return stepContent.get(0);
        }
        return String.format("Привет %s! Ты в админке", context.getUser().getFirstName());
    }

    @Override
    public String getDynamicKeyboard(BotContext context) {
        return "";
    }
}

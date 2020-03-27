package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.User;

import java.util.Comparator;

import static spring.app.util.Keyboards.*;

@Component
public class AdminRemoveUser extends Step {
    private String text;
    private String keyboard;

    @Override
    public void enter(BotContext context) {
        StringBuilder userList = new StringBuilder("Вот список всех пользователей. Для удаления, напиши vkId одного пользователя или нескольких пользователей через пробел.\n\n");
        context.getUserService().getAllUsers().stream()
                .filter(user -> !user.getRole().isAdmin())
                .sorted(Comparator.comparing(User::getLastName))
                .forEach(user -> userList
                        .append(user.getLastName())
                        .append(" ")
                        .append(user.getFirstName())
                        .append(". vkId ")
                        .append(user.getVkId())
                        .append("\n")
                );
        text = userList.toString();
        keyboard = NO_KB;
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException {

    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public String getKeyboard() {
        return keyboard;
    }
}

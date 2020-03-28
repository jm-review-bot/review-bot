package spring.app.core.steps;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.User;
import spring.app.util.StringParser;

import javax.persistence.NoResultException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.*;

@Component
public class AdminRemoveUser extends Step {
    private final Map<Integer, String> savedInputs = new HashMap<>();

    @Override
    public void enter(BotContext context) {
        String savedInput = savedInputs.get(context.getVkId());
        StringBuilder userList;
        if (savedInput == null || savedInput.isEmpty()) {
            // если в памяти пусто, показываем первичный вопрос
            userList = new StringBuilder("Вот список всех пользователей. Для удаления, напиши vkId одного или нескольких пользователей через пробел или запятую.\n\n");
            context.getUserService().getAllUsers().stream()
                    .filter(user -> !user.getRole().isAdmin())
                    .sorted(Comparator.comparing(User::getLastName))
                    .forEach(user -> userList
                            .append(user.getLastName())
                            .append(" ")
                            .append(user.getFirstName())
                            .append(", vkId: ")
                            .append(user.getVkId())
                            .append(", https://vk.com/id")
                            .append(user.getVkId())
                            .append("\n")
                    );
            text = userList.toString();
            keyboard = NO_KB;
        } else {
            // если в памяти уже есть данные, значит показываем предупреждение об удалении юзеров
            // оно было подготовлено в processInput и сохранено в памяти,
            // т.к. там могло выпасть исключение, если юзер вводит заведомо неверные данные
            text = savedInput;
            keyboard = YES_NO_KB;
        }
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException {
        String currentInput = context.getInput();
        Integer vkId = context.getVkId();
        String savedInput = savedInputs.get(vkId);
        if (savedInput == null || savedInput.isEmpty()) {
            // если юзер на данном шаге ничего еще не вводил, значит мы ожидаем от него
            // vkId для удаления input. Сохраняем в память введенный текст
            StringBuilder userList = new StringBuilder("Вы собираетесь удалить следующих пользователей:\n\n");
            try {
                StringParser.toNumbersSet(currentInput)
                        .forEach(inputNumber -> {
                            User user = context.getUserService().getByVkId(inputNumber);
                            if (user != null) {
                                userList
                                        .append(user.getLastName())
                                        .append(" ")
                                        .append(user.getFirstName())
                                        .append(". vkId ")
                                        .append(user.getVkId())
                                        .append(" https://vk.com/id")
                                        .append(user.getVkId())
                                        .append("\n");
                            }
                        });
                userList.append("Согласны? (Да/Нет)");
                savedInputs.put(vkId, userList.toString());
                nextStep = ADMIN_REMOVE_USER;
            } catch (NumberFormatException | NoResultException | EmptyResultDataAccessException e) {
                keyboard = NO_KB;
                throw new ProcessInputException("Введены неверные данные. Таких пользователей не найдено...");
            }
        } else {
            // если он раньше что-то вводил на этом шаге, то мы ожидаем подтверждения действий
            String yesOrNo = StringParser.toWordsArray(currentInput)[0];
            if (yesOrNo.equalsIgnoreCase("да")) {
                // удаляем юзеров
                StringParser.toNumbersSet(savedInput)
                        .forEach(savedVkId -> context.getUserService().deleteUserByVkId(savedVkId));
                // обязательно очищаем память
                savedInputs.remove(vkId);
                nextStep = ADMIN_REMOVE_USER;
            } else if (yesOrNo.equalsIgnoreCase("нет")) {
                savedInputs.remove(vkId);
                nextStep = ADMIN_MENU;
            } else if (yesOrNo.equalsIgnoreCase("/start")) {
                savedInputs.remove(vkId);
                nextStep = START;
            } else {
                keyboard = START_KB;
                throw new ProcessInputException("Введена неверная команда...");
            }
        }
    }
}

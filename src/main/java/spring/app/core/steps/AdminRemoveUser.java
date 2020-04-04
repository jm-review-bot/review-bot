package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.NoNumbersEnteredException;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.User;
import spring.app.util.StringParser;

import javax.persistence.NoResultException;
import java.util.*;

import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.*;

@Component
public class AdminRemoveUser extends Step {

    @Override
    public void enter(BotContext context) {
        Integer vkId = context.getVkId();
        List<String> savedInput = getUserStorage(vkId, ADMIN_REMOVE_USER);
        StringBuilder userList;
        if (savedInput == null || savedInput.isEmpty()) {
            // если в памяти пусто, показываем первичный вопрос
            userList = new StringBuilder("Вот список всех пользователей. Для удаления, напиши vkId одного или нескольких пользователей через пробел или запятую.\nДля возврата в меню, введи \"назад\".\n\n");
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
            keyboard = BACK_KB;
        } else {
            // если в памяти уже есть данные, значит показываем предупреждение об удалении юзеров
            // оно было подготовлено в processInput и сохранено в памяти,
            // т.к. там могло выпасть исключение, если юзер вводит заведомо неверные данные
            text = savedInput.get(0);
            keyboard = YES_NO_KB;
        }
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException, NoNumbersEnteredException {
        String currentInput = context.getInput();
        Integer vkId = context.getVkId();
        List<String> savedInput = getUserStorage(vkId, ADMIN_REMOVE_USER);

        // также он  может прислать команду отмены
        String wordInput = StringParser.toWordsArray(currentInput)[0];

        if (wordInput.equals("назад")
                || wordInput.equals("нет")
                || wordInput.equals("отмена")) {
            removeUserStorage(vkId, ADMIN_REMOVE_USER);
            nextStep = ADMIN_MENU;
        } else if (wordInput.equals("/start")) {
            removeUserStorage(vkId, ADMIN_REMOVE_USER);
            nextStep = START;
        } else if (savedInput == null || savedInput.isEmpty()) {
            // если юзер на данном шаге ничего еще не вводил, значит мы ожидаем от него
            // vkId для удаления input. Сохраняем в память введенный текст
            StringBuilder userList = new StringBuilder("Вы собираетесь удалить следующих пользователей:\n\n");
            try {
                StringParser.toNumbersSet(currentInput)
                        .forEach(inputNumber -> {
                            User user = context.getUserService().getByVkId(inputNumber);
                                userList
                                        .append(user.getLastName())
                                        .append(" ")
                                        .append(user.getFirstName())
                                        .append(". vkId ")
                                        .append(user.getVkId())
                                        .append(" https://vk.com/id")
                                        .append(user.getVkId())
                                        .append("\n");
                        });
                userList.append("Согласны? (Да/Нет)");
                updateUserStorage(vkId, ADMIN_REMOVE_USER, Arrays.asList(userList.toString()));
                nextStep = ADMIN_REMOVE_USER;
            } catch (NumberFormatException | NoResultException | NoNumbersEnteredException e) {
                keyboard = BACK_KB;
                throw new ProcessInputException("Введены неверные данные. Таких пользователей не найдено...");
            }
        } else if (wordInput.equals("да")) {
            // если он раньше что-то вводил на этом шаге, то мы ожидаем подтверждения действий.
            // удаляем юзеров
            StringParser.toNumbersSet(savedInput.get(0))
                    .forEach(savedVkId -> context.getUserService().deleteUserByVkId(savedVkId));
            // обязательно очищаем память
            removeUserStorage(vkId, ADMIN_REMOVE_USER);
            nextStep = ADMIN_REMOVE_USER;
        } else {
            keyboard = START_KB;
            throw new ProcessInputException("Введена неверная команда...");
        }
    }
}

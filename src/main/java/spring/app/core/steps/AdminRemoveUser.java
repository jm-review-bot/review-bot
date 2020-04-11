package spring.app.core.steps;

import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.NoNumbersEnteredException;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.User;
import spring.app.util.StringParser;

import javax.persistence.NoResultException;
import java.util.*;
import java.util.stream.Collectors;

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
            userList = new StringBuilder("Вот список всех пользователей. Для удаления, напиши указанные номера одного или нескольких пользователей через пробел или запятую.\nДля возврата в меню, введи \"назад\".\n\n");
            // создаем лист строк, куда будем складывать Id Юзеров, которых мы показываем админу в боте
            List<String> usersToDelete = new ArrayList<>();
            final int[] i = {1};
            context.getUserService().getAllUsers().stream()
                    .filter(user -> !user.getRole().isAdmin())
                    .sorted(Comparator.comparing(User::getLastName))
                    .forEach(user -> {
                        userList.append("[").append(i[0]++).append("] ")
                                .append(user.getLastName())
                                .append(" ")
                                .append(user.getFirstName())
                                .append(", https://vk.com/id")
                                .append(user.getVkId())
                                .append("\n");
                        // сохраняем ID юзера в лист
                        usersToDelete.add(user.getId().toString());
                    });
            text = userList.toString();
            keyboard = BACK_KB;
            updateUserStorage(vkId, ADMIN_USERS_LIST, usersToDelete);
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
        List<String> usesToDelete = getUserStorage(vkId, ADMIN_USERS_LIST);

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
            StringBuilder confirmMessage = new StringBuilder("Вы собираетесь удалить пользователей:\n\n");
            try {
                List<String> selectedUsers = new ArrayList<>();

                StringParser.toNumbersSet(currentInput)
                        .forEach(inputNumber -> {
                            Long userId = Long.parseLong(usesToDelete.get(inputNumber - 1));
                            User user = context.getUserService().getUserById(userId);
                            confirmMessage
                                    .append(user.getLastName())
                                    .append(" ")
                                    .append(user.getFirstName())
                                    .append(", https://vk.com/id")
                                    .append(user.getVkId())
                                    .append("\n");
                            selectedUsers.add(user.getId().toString());
                        });
                confirmMessage.append("\nСогласны? (Да/Нет)");
                updateUserStorage(vkId, ADMIN_REMOVE_USER, Arrays.asList(confirmMessage.toString()));
                updateUserStorage(vkId, ADMIN_USERS_LIST, selectedUsers);
                nextStep = ADMIN_REMOVE_USER;
            } catch (NumberFormatException | NoResultException | NoNumbersEnteredException e) {
                keyboard = BACK_KB;
                throw new ProcessInputException("Введены неверные данные. Таких пользователей не найдено...");
            }
        } else if (wordInput.equals("да")) {
            // если он раньше что-то вводил на этом шаге, то мы ожидаем подтверждения действий.
            // удаляем юзеров
            usesToDelete.forEach(userIdString ->
                    context.getUserService()
                            .deleteUserById(Long.parseLong(userIdString))
            );
            // обязательно очищаем память
            removeUserStorage(vkId, ADMIN_REMOVE_USER);
            removeUserStorage(vkId, ADMIN_USERS_LIST);
            clearUsersOfStorage(usesToDelete);
            nextStep = ADMIN_REMOVE_USER;
        } else {
            keyboard = START_KB;
            throw new ProcessInputException("Введена неверная команда...");
        }
    }
}

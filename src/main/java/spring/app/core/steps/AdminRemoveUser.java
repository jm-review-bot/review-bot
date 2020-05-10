package spring.app.core.steps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.NoNumbersEnteredException;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.User;
import spring.app.service.abstraction.StorageService;
import spring.app.util.StringParser;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.*;

@Component
public class AdminRemoveUser extends Step {

    private Logger logger = LoggerFactory.getLogger(
            AdminRemoveUser.class);

    @Override
    public void enter(BotContext context) {
        //======
        System.out.println("BEGIN_STEP::"+"AdminRemoveUser");
        //======
        Integer vkId = context.getVkId();
        StorageService storageService = context.getStorageService();
        List<String> savedInput = storageService.getUserStorage(vkId, ADMIN_REMOVE_USER);
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
            storageService.updateUserStorage(vkId, ADMIN_USERS_LIST, usersToDelete);
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
        StorageService storageService = context.getStorageService();
        Integer vkId = context.getVkId();
        List<String> savedInput = storageService.getUserStorage(vkId, ADMIN_REMOVE_USER);
        List<String> usersToDelete = storageService.getUserStorage(vkId, ADMIN_USERS_LIST);

        // также он  может прислать команду отмены
        String wordInput = StringParser.toWordsArray(currentInput)[0];

        if (wordInput.equals("назад")
                || wordInput.equals("нет")
                || wordInput.equals("отмена")) {
            storageService.removeUserStorage(vkId, ADMIN_REMOVE_USER);
            nextStep = ADMIN_MENU;
        } else if (wordInput.equals("/start")) {
            storageService.removeUserStorage(vkId, ADMIN_REMOVE_USER);
            nextStep = START;
        } else if (savedInput == null || savedInput.isEmpty()) {
            // если юзер на данном шаге ничего еще не вводил, значит мы ожидаем от него
            // vkId для удаления input. Сохраняем в память введенный текст
            StringBuilder confirmMessage = new StringBuilder("Вы собираетесь удалить пользователей:\n\n");
            try {
                List<String> selectedUsers = new ArrayList<>();

                StringParser.toNumbersSet(currentInput)
                        .forEach(inputNumber -> {
                            Long userId = Long.parseLong(usersToDelete.get(inputNumber - 1));
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
                storageService.updateUserStorage(vkId, ADMIN_REMOVE_USER, Arrays.asList(confirmMessage.toString()));
                storageService.updateUserStorage(vkId, ADMIN_USERS_LIST, selectedUsers);
                nextStep = ADMIN_REMOVE_USER;
            } catch (NumberFormatException | NoResultException | NoNumbersEnteredException | IndexOutOfBoundsException e) {
                throw new ProcessInputException("Введены неверные данные. Таких пользователей не найдено...");
            }
        } else if (wordInput.equals("да")) {
            // если он раньше что-то вводил на этом шаге, то мы ожидаем подтверждения действий.
            // удаляем юзеров
            String str = "";
            for(String s : usersToDelete) {
                str = str + context.getUserService().getUserById(Long.parseLong(s)).getFirstName() + " " +
                        context.getUserService().getUserById(Long.parseLong(s)).getLastName() + "[vkId - "+context.getUserService().getUserById(Long.parseLong(s)).getVkId()+"]\n";
            }
            usersToDelete.forEach(userIdString -> {
                        context.getUserService()
                                .deleteUserById(Long.parseLong(userIdString));
                    }
            );
            logger.debug("\tlog-message об операции пользователя над экземпляром(ами) сущности:\n" +
                    "Администратор "+context.getUser().getFirstName()+" "+context.getUser().getLastName()+" [vkId - "+context.getUser().getId()+"] удалил пользователя(ей) из базы.\n" +
                    "А именно, он удалил следующего(их) пользователя(ей):\n" +
                    str);
            // обязательно очищаем память
            storageService.removeUserStorage(vkId, ADMIN_REMOVE_USER);
            storageService.removeUserStorage(vkId, ADMIN_USERS_LIST);
            nextStep = ADMIN_REMOVE_USER;
        } else {
            throw new ProcessInputException("Введена неверная команда...");
        }
    }
}
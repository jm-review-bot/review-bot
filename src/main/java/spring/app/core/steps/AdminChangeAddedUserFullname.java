package spring.app.core.steps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import spring.app.core.BotContext;
import spring.app.exceptions.NoDataEnteredException;
import spring.app.exceptions.NoNumbersEnteredException;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.User;
import spring.app.service.abstraction.UserService;
import spring.app.util.StringParser;


import static spring.app.core.StepSelector.ADMIN_ADD_USER;
import static spring.app.util.Keyboards.NO_KB;

/**
 * @author AkiraRokudo on 23.05.2020 in one of sun day
 */
@Component
public class AdminChangeAddedUserFullname extends Step{
    private final static Logger log = LoggerFactory.getLogger(StringParser.class);

    @Override
    public void enter(BotContext context) {
        text = "Введите новое имя и фамилию. Например: Иван Иванов";
        keyboard = NO_KB;
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException, NoNumbersEnteredException, NoDataEnteredException {
        String newFullName = context.getInput();
        Integer vkId = context.getVkId();
        String [] firstAndLastName = newFullName.split(" ");
        if (firstAndLastName.length == 2) {
            //Проверим, что есть только символы алфавитов
            boolean allSymbolAlphabet = newFullName.replaceAll(" ", "").chars().allMatch(Character::isLetter);
            if(allSymbolAlphabet) {
                UserService userService = context.getUserService();
                Long addedUserId = Long.parseLong(context.getStorageService().getUserStorage(vkId, ADMIN_ADD_USER).get(0));
                User addedUser = userService.getUserById(addedUserId);
                String oldFirstName = addedUser.getFirstName();
                String oldLastName = addedUser.getLastName();
                addedUser.setFirstName(firstAndLastName[0]);
                addedUser.setLastName(firstAndLastName[1]);
                userService.updateUser(addedUser);
                log.debug("\tlog-message об операции пользователя над экземпляром(ами) сущности:\n" +
                        "Администратор "+context.getUser().getFirstName()+" "+context.getUser().getLastName()+" [vkId - "+context.getUser().getId()+"] изменил имя добавляемого пользователя с vkId "+addedUser.getVkId()+".\n" +
                        "Предыдущее имя:\n" + oldFirstName+" "+oldLastName+"\nНовое имя:\n"+firstAndLastName[0]+" "+firstAndLastName[1]);
                nextStep = ADMIN_ADD_USER;
            } else {
                throw new ProcessInputException("В новом имени фамилии присутствуют не алфавитные символы");
            }
        } else {
            throw new ProcessInputException("Новое имя и фамилия должны состоять из 2 слов");
        }
    }
}

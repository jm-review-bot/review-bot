package spring.app.core.steps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import spring.app.controller.AdminThemeRestController;
import spring.app.core.BotContext;
import spring.app.exceptions.NoDataEnteredException;
import spring.app.exceptions.NoNumbersEnteredException;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.Role;
import spring.app.model.User;
import spring.app.service.abstraction.RoleService;
import spring.app.service.abstraction.StorageService;
import spring.app.service.abstraction.UserService;
import spring.app.util.StringParser;

import java.util.ArrayList;
import java.util.List;

import static spring.app.core.StepSelector.*;
import static spring.app.util.Keyboards.*;

@Component
public class AdminEditUserGetRolesList extends Step {

    private final static Logger logger = LoggerFactory.getLogger(AdminThemeRestController.class);

    private final StorageService storageService;
    private final RoleService roleService;
    private final UserService userService;

    public AdminEditUserGetRolesList(StorageService storageService,
                                     RoleService roleService,
                                     UserService userService) {
        super("", DEF_BACK_KB);
        this.storageService = storageService;
        this.roleService = roleService;
        this.userService = userService;
    }

    @Override
    public void enter(BotContext context) {
    }

    @Override
    public void processInput(BotContext context) throws ProcessInputException, NoNumbersEnteredException, NoDataEnteredException {
        String currentInput = context.getInput();
        Integer vkId = context.getVkId();
        if (StringParser.isNumeric(currentInput)) {
            Integer rolePosition = Integer.parseInt(currentInput);
            List<String> roleIds = storageService.getUserStorage(vkId, ADMIN_EDIT_USER_GET_ROLES_LIST);
            if (rolePosition > roleIds.size() || rolePosition < 1) {
                throw new ProcessInputException("Введен неверный номер роли. Повторите ввод.");
            }
            Long selectedRoleId = Long.parseLong(roleIds.get(rolePosition - 1));
            Long studentId = Long.parseLong(storageService.getUserStorage(vkId, ADMIN_USERS_LIST).get(0));
            User student = userService.getUserById(studentId);
            if (student.getRole().getId() == selectedRoleId) {
                throw new ProcessInputException("Пользователь уже имеет выбранную роль");
            } else {
                student.setRole(roleService.getRoleById(selectedRoleId));
                userService.updateUser(student);
                logger.info("Админ (vkId={}) изменил роль пользователю (vkId={})",
                        vkId, student.getVkId());
                storageService.removeUserStorage(vkId, ADMIN_EDIT_USER_GET_ROLES_LIST);
                sendUserToNextStep(context, ADMIN_EDIT_USER);
            }
        } else if (currentInput.equalsIgnoreCase("назад")) {
            storageService.removeUserStorage(vkId, ADMIN_EDIT_USER_GET_ROLES_LIST);
            storageService.removeUserStorage(vkId, ADMIN_USERS_LIST);
            sendUserToNextStep(context, ADMIN_EDIT_USER);
        } else {
            throw new ProcessInputException("Введена неверная команда...");
        }
    }

    @Override
    public String getDynamicText(BotContext context) {
        Integer vkId = context.getVkId();
        Long studentId = Long.parseLong(storageService.getUserStorage(vkId, ADMIN_USERS_LIST).get(0));
        User student = userService.getUserById(studentId);
        List<Role> allRoles = roleService.getAllRoles();
        List<String> rolesIds = new ArrayList<>();
        StringBuilder infoMessage = new StringBuilder(String.format(
                "Выбран пользователь %s %s.\nТекущая роль пользователя: %s.\n\nВыберите новую роль для пользователя из списка:\n",
                student.getFirstName(),
                student.getLastName(),
                student.getRole().getName()
        ));
        for (int i = 0; i < allRoles.size(); i++) {
            Role role = allRoles.get(i);
            infoMessage.append("\n[")
                    .append(i + 1)
                    .append("] ")
                    .append(role.getName());
            rolesIds.add(role.getId().toString());
        }
        storageService.updateUserStorage(vkId, ADMIN_EDIT_USER_GET_ROLES_LIST, rolesIds);
        return infoMessage.toString();
    }

    @Override
    public String getDynamicKeyboard(BotContext context) {
        return "";
    }
}

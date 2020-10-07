package spring.app.controller;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.app.dto.UserDto;
import spring.app.exceptions.IncorrectVkIdsException;
import spring.app.groups.CreateGroup;
import spring.app.groups.UpdateGroup;
import spring.app.model.User;
import spring.app.service.abstraction.VkService;
import spring.app.service.abstraction.RoleService;
import spring.app.service.abstraction.StudentReviewService;
import spring.app.service.abstraction.ThemeService;
import spring.app.service.abstraction.UserService;
import spring.app.util.StringParser;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Validated
@RestController
@RequestMapping("/api/admin/user")
@Api(value = "Users controller")
public class AdminUserRestController {

    private final static Logger logger = LoggerFactory.getLogger(AdminThemeRestController.class);

    private final UserService userService;
    private final StudentReviewService studentReviewService;
    private final ThemeService themeService;
    private final RoleService roleService;
    private final VkService vkService;

    @Autowired
    public AdminUserRestController(UserService userService,
                                   StudentReviewService studentReviewService,
                                   ThemeService themeService,
                                   RoleService roleService,
                                   VkService vkService) {
        this.userService = userService;
        this.studentReviewService = studentReviewService;
        this.themeService = themeService;
        this.roleService = roleService;
        this.vkService = vkService;
    }

    @ApiOperation(value = "Get all users list")
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsersDto());
    }

    @ApiOperation(value = "Get user by ID")
    @GetMapping("/{userId}")
    public ResponseEntity<Optional<UserDto>> getUser(@ApiParam(value = "User ID", required = true) @PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserDtoById(userId));
    }

    @ApiOperation(value = "Add new user")
    @Validated(CreateGroup.class)
    @PutMapping
    public ResponseEntity<?> addNewUser(@ApiParam(value = "User DTO", required = true) @RequestBody @Valid UserDto newUserDto) {
        String vkId = newUserDto.getStringVkId();
        try {
            User user = userService.addUserByVkId(vkId);
            if (user == null) { // Пользователь уже есть в базе и еще раз добавлен не был
                return ResponseEntity.badRequest().build();
            }
            // Новому пользователю устанавливается тема, с которой он может начинать сдавать ревью
            Integer startThemePosition = themeService.getThemeById(newUserDto.getStartThemeId()).getPosition();
            if (startThemePosition > 1) {
                studentReviewService.setPassedThisAndPreviousThemesForStudent(user.getId(), themeService.getByPosition(startThemePosition - 1).get().getId());
            }

            // Логирование
            User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            logger.info("Админ (vkId={}) добавил пользователя (ID={})" ,
                    loggedInUser.getVkId(), user.getVkId());

            return ResponseEntity.noContent().build();
        } catch (ClientException | ApiException | IncorrectVkIdsException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @ApiOperation(value = "Delete user by ID")
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@ApiParam(value = "User ID", required = true) @PathVariable Long userId) {
        User user = userService.getUserById(userId);
        userService.deleteUserById(userId);

        // Логирование
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        logger.info("Админ (vkId={}) удалил пользователя (ID={})" ,
                loggedInUser.getVkId(), user.getVkId());

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Edit user")
    @Validated(UpdateGroup.class)
    @PostMapping("/{userId}")
    public ResponseEntity<?> editUser(@ApiParam(value = "User DTO", required = true) @RequestBody @Valid UserDto userDto,
                                         @ApiParam(value = "User ID", required = true) @PathVariable Long userId) {

        String stringVkId = userDto.getStringVkId();
        if (!StringParser.isNumeric(stringVkId)) { // Если VK ID строковый, его необходимо преобразовать в числовой
            try {
                userDto.setVkId(vkService.newUserFromVk(stringVkId).getVkId());
            } catch (ClientException | ApiException | IncorrectVkIdsException e) {
                return ResponseEntity.notFound().build();
            }
        } else {
            userDto.setVkId(Integer.parseInt(stringVkId));
        }

        User user = userService.getUserById(userId);
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setVkId(userDto.getVkId());
        user.setRole(roleService.getRoleByName(userDto.getRole()).get());
        userService.updateUser(user);

        // Логирование
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        logger.info("Админ (vkId={}) изменил пользователя (ID={})" ,
                loggedInUser.getVkId(), user.getVkId());

        return ResponseEntity.ok().build();
    }
}

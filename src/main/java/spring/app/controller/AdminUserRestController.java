package spring.app.controller;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import spring.app.dto.UserDto;
import spring.app.exceptions.IncorrectVkIdsException;
import spring.app.groups.CreateGroup;
import spring.app.groups.UpdateGroup;
import spring.app.model.User;
import spring.app.service.abstraction.*;
import spring.app.util.StringParser;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/admin/user")
@Api(value = "Users controller")
public class AdminUserRestController {

    private final UserService userService;
    private final StudentReviewService studentReviewService;
    private final ThemeService themeService;
    private final RoleService roleService;
    private final VkService vkService;

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
    public ResponseEntity<UserDto> getUser(@ApiParam(value = "User ID", required = true) @PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserDtoById(userId));
    }

    @ApiOperation(value = "Add new user")
    @Validated(CreateGroup.class)
    @PutMapping
    public ResponseEntity<?> addNewUser(@ApiParam(value = "User DTO", required = true) @RequestBody @Valid UserDto newUserDto) {
        String vkId = newUserDto.getStringVkId();
        try {
            User user = userService.addUserByVkId(vkId);
            // Новому пользователю устанавливается тема, с которой он может начинать сдавать ревью
            Integer startThemePosition = newUserDto.getStartThemePosition();
            if (startThemePosition > 1) {
                studentReviewService.setPassedThisAndPreviousThemesForStudent(user.getId(), themeService.getByPosition(startThemePosition - 1).getId());
            }
            return ResponseEntity.noContent().build();
        } catch (ClientException | ApiException | IncorrectVkIdsException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @ApiOperation(value = "Delete user by ID")
    @DeleteMapping("/{userId}")
    public ResponseEntity<UserDto> deleteUser(@ApiParam(value = "User ID", required = true) @PathVariable Long userId) {
        UserDto userDto = userService.getUserDtoById(userId);
        userService.deleteUserById(userId);
        return ResponseEntity.ok(userDto);
    }

    @ApiOperation(value = "Edit user")
    @Validated(UpdateGroup.class)
    @PostMapping("/{userId}")
    public ResponseEntity<UserDto> editUser(@ApiParam(value = "User DTO", required = true) @RequestBody @Valid UserDto userDto,
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

        String[] name = StringParser.toWordsArray(userDto.getName());
        if (name.length != 2) { // Имя и фамилия должны быть разделены пробелом
            return ResponseEntity.badRequest().build();
        }

        User user = userService.getUserById(userId);
        user.setFirstName(name[0]);
        user.setLastName(name[1]);
        user.setVkId(userDto.getVkId());
        user.setRole(roleService.getRoleByName(userDto.getRole()));
        userService.updateUser(user);

        return ResponseEntity.ok(userDto);
    }
}

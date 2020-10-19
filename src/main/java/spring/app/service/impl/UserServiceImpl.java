package spring.app.service.impl;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.app.dao.abstraction.RoleDao;
import spring.app.dao.abstraction.UserDao;
import spring.app.dto.ReviewerDto;
import spring.app.dto.UserDto;
import spring.app.exceptions.IncorrectVkIdsException;
import spring.app.model.FreeTheme;
import spring.app.model.Role;
import spring.app.model.User;
import spring.app.service.abstraction.ThemeService;
import spring.app.service.abstraction.UserService;
import spring.app.service.abstraction.VkService;
import spring.app.util.StringParser;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final RoleDao roleDao;
    private final ThemeService themeService;
    private final VkService vkService;

    @Autowired
    public UserServiceImpl(UserDao userDao,
                           RoleDao roleDao,
                           ThemeService themeService,
                           VkService vkService) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.themeService = themeService;
        this.vkService = vkService;
    }

    @Override
    public void restoreUserById(Long id) { userDao.restoreUserById(id);}

    @Transactional
    @Override
    public void addUser(User user) {
        userDao.save(user);
    }

    @Override
    public User getUserById(Long id) {
        return userDao.getById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.getAll();
    }

    @Override
    public List<UserDto> getAllUsersDto() {
        return userDao.getAllUsersDto();
    }

    @Override
    public Optional<UserDto> getUserDtoById(Long userId) {
        return userDao.getUserDtoById(userId);
    }

    @Transactional
    @Override
    public void updateUser(User user) {
        userDao.update(user);
    }

    @Transactional
    @Override
    public void deleteUserById(Long id) {
        userDao.deleteById(id);
    }

    @Override
    public Optional<User> getByVkId(Integer vkId) {
        return userDao.getByVkId(vkId);
    }

    @Override
    public boolean isExistByVkId(Integer vkId) {
        return userDao.isExistByVkId(vkId);
    }

    @Override
    @Transactional
    public void deleteUserByVkId(Integer vkId) {
        userDao.deleteUserByVkId(vkId);
    }

    @Override
    public List<User> getUsersByReviewPeriod(LocalDateTime periodStart, LocalDateTime periodEnd) {
        return userDao.getUsersByReviewPeriod(periodStart, periodEnd);
    }

    @Override
    public List<User> getStudentsByReviewId(Long reviewId) {
        return userDao.getStudentsByReviewId(reviewId);
    }

    @Override
    public List<User> getStudentsByReviewPeriod(LocalDateTime periodStart, LocalDateTime periodEnd) {
        return userDao.getStudentsByReviewPeriod(periodStart, periodEnd);
    }

    @Override
    public boolean isUserExaminer(Long userId) {
        return userDao.isUserExaminer(userId);
    }

    @Override
    public List<ReviewerDto> getExaminersInThisTheme(long themeId) {
        return userDao.getExaminersInThisTheme(themeId);
    }

    @Override
    public List<ReviewerDto> getExaminersInNotThisTheme(long themeId) {
        return userDao.getExaminersInNotThisTheme(themeId);
    }

    @Override
    @Transactional
    public User addNewReviewer(long themeId , long userId) {
        List<User> users = userDao.getExaminersByFreeThemeId(themeId);
        users.add(userDao.getById(userId));
        FreeTheme freeTheme = themeService.getFreeThemeById(themeId).get();
        freeTheme.setExaminers(users);
        themeService.updateTheme(freeTheme);
        return userDao.getById(userId);
    }

    @Override
    @Transactional
    public void deleteReviewerFromTheme(long themeId, long reviewerId) {
        userDao.deleteReviewerFromTheme(themeId , reviewerId);
    }



    // Метод нужен для реализации UserDetailService.В рамках проекта username - это VkId пользователя
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (StringParser.isNumeric(username)) {
            Optional<User> optionalUser = userDao.getByVkId(Integer.parseInt(username));
            if (optionalUser.isPresent()) {
                return optionalUser.get();
            } else {
                throw new UsernameNotFoundException("Пользователя нет в БД");
            }
        } else {
            throw new UsernameNotFoundException("Логин не похож на VkId");
        }
    }

    @Transactional
    @Override
    public User addUserByVkId(String stringVkId) throws ClientException, ApiException, IncorrectVkIdsException {
        User newUser = vkService.newUserFromVk(stringVkId);
        if (!userDao.isExistByVkId(newUser.getVkId())) { // Проверка на тот факт, что пользователя еще нет в БД
            newUser.setRole(roleDao.getRoleByName("USER").get());
            userDao.save(newUser);
            return newUser;
        } else {
            return null;
        }
    }

    @Override
    public List<UserDto> getUsersDtoByIds(List<Long> userIds) {
        return userDao.getUsersDtoByIds(userIds);
    }
}

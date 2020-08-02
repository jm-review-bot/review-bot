package spring.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.app.dao.abstraction.ReviewDao;
import spring.app.dao.abstraction.StudentReviewAnswerDao;
import spring.app.dao.abstraction.StudentReviewDao;
import spring.app.dao.abstraction.UserDao;
import spring.app.dto.ReviewerDto;
import spring.app.model.FreeTheme;
import spring.app.model.User;
import spring.app.service.abstraction.ThemeService;
import spring.app.service.abstraction.UserService;
import spring.app.util.StringParser;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final ThemeService themeService;

    @Autowired
    public UserServiceImpl(UserDao userDao, ThemeService themeService) {
        this.userDao = userDao;
        this.themeService = themeService;
    }

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
    public User getByVkId(Integer vkId) {
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
        FreeTheme freeTheme = themeService.getFreeThemeById(themeId);
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
            User user = userDao.getByVkId(Integer.parseInt(username));
            if (user != null) {
                return user;
            } else {
                throw new UsernameNotFoundException("Пользователя нет в БД");
            }
        } else {
            throw new UsernameNotFoundException("Логин не похож на VkId");
        }
    }
}

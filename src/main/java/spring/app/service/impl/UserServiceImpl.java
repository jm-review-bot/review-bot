package spring.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.app.dao.abstraction.ReviewDao;
import spring.app.dao.abstraction.StudentReviewAnswerDao;
import spring.app.dao.abstraction.StudentReviewDao;
import spring.app.dao.abstraction.UserDao;
import spring.app.dto.ReviewerDto;
import spring.app.model.User;
import spring.app.service.abstraction.ThemeService;
import spring.app.service.abstraction.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final StudentReviewAnswerDao studentReviewAnswerDao;
    private final StudentReviewDao studentReviewDao;
    private final ReviewDao reviewDao;
    private final ThemeService themeService;


    @Autowired
    public UserServiceImpl(UserDao userDao, StudentReviewAnswerDao studentReviewAnswerDao, StudentReviewDao studentReviewDao, ReviewDao reviewDao , ThemeService themeService) {
        this.userDao = userDao;
        this.studentReviewAnswerDao = studentReviewAnswerDao;
        this.studentReviewDao = studentReviewDao;
        this.reviewDao = reviewDao;
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
        // удаляем StudentReviewAnswer
        studentReviewAnswerDao.bulkDeleteByUserId(id);
        // удаляем StudentReview
        studentReviewDao.bulkDeleteByUserId(id);
        // удаляем Review
        reviewDao.bulkDeleteByUserId(id);
        // удаляем юзера
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
    public List<ReviewerDto> getExaminersInThisTheme(long themeId) {
        return userDao.getExaminersInThisTheme(themeId);
    }

    @Override
    public List<ReviewerDto> getExaminersInNotThisTheme(long themeId) {
        return userDao.getExaminersInNotThisTheme(themeId);
    }

    @Override
    public void deleteReviewerByThemeId (long themeId , long examinerId) {
        userDao.deleteReviewerByThemeId(themeId , examinerId);
    }

    @Override
    public ReviewerDto addNewReviewer (long themeId , ReviewerDto reviewerDto) {
        themeService.addThemeIdToFreeTheme(themeId);
        return userDao.addNewReviewer(themeId,reviewerDto);
    }

}

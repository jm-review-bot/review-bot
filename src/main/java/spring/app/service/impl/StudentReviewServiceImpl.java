package spring.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.app.dao.abstraction.ReviewDao;
import spring.app.dao.abstraction.StudentReviewDao;
import spring.app.dao.abstraction.ThemeDao;
import spring.app.dao.abstraction.UserDao;
import spring.app.model.Review;
import spring.app.model.StudentReview;
import spring.app.model.Theme;
import spring.app.model.User;
import spring.app.service.abstraction.StudentReviewService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class StudentReviewServiceImpl implements StudentReviewService {

    private StudentReviewDao studentReviewDao;
    private ReviewDao reviewDao;
    private UserDao userDao;
    private ThemeDao themeDao;

    @Autowired
    public StudentReviewServiceImpl(StudentReviewDao studentReviewDao,
                                    ReviewDao reviewDao,
                                    UserDao userDao,
                                    ThemeDao themeDao) {
        this.studentReviewDao = studentReviewDao;
        this.reviewDao = reviewDao;
        this.userDao = userDao;
        this.themeDao = themeDao;
    }

    @Transactional
    @Override
    public void addStudentReview(StudentReview studentReview) {
        studentReviewDao.save(studentReview);
    }

    @Override
    public StudentReview getStudentReviewById(Long id) {
        return studentReviewDao.getById(id);
    }

    @Override
    public List<StudentReview> getAllStudentReview() {
        return studentReviewDao.getAll();
    }

    /**
     * Возвращает все ревью студента по определенной теме
     *
     * @param theme тема
     * @param studentId  студента
     * @return
     */
    @Override
    public List<StudentReview> getAllStudentReviewsByStudentIdAndTheme(Long studentId, Theme theme) {
        return studentReviewDao.getAllStudentReviewsByStudentIdAndTheme(studentId, theme);
    }

    @Transactional
    @Override
    public void updateStudentReview(StudentReview studentReview) {
        studentReviewDao.update(studentReview);
    }

    @Transactional
    @Override
    public void deleteStudentReviewById(Long id) {
        studentReviewDao.deleteById(id);
    }

    @Override
    public Optional<StudentReview> getStudentReviewIfAvailableAndOpen(Long idUser) {
        return studentReviewDao.getStudentReviewIfAvailableAndOpen(idUser);
    }

    @Override
    public Long getNumberStudentReviewByIdReview(Long idReview) {
        return studentReviewDao.getNumberStudentReviewByIdReview(idReview);
    }

    @Transactional
    @Override
    public void deleteStudentReviewByVkId(Integer vkId) {
        studentReviewDao.deleteStudentReviewByVkId(vkId);
    }

    @Override
    public Optional<StudentReview> getStudentReviewByReviewIdAndStudentId(Long reviewId, Long studentId) {
        return studentReviewDao.getStudentReviewByReviewIdAndStudentId(reviewId, studentId);
    }

    @Override
    public List<StudentReview> getOpenReviewByStudentVkId(Integer vkId) {
        return studentReviewDao.getOpenReviewByStudentVkId(vkId);
    }

    @Override
    public Optional<StudentReview> getStudentReviewsByIdWithFetchReviewUserThemeAndReviewer(Long id) {
        return studentReviewDao.getStudentReviewsByIdWithFetchReviewUserThemeAndReviewer(id);
    }

    @Override
    public List<StudentReview> getAllStudentReviewsByReviewId(Long reviewId) {
        return studentReviewDao.getAllStudentReviewsByReviewId(reviewId);
    }

    @Transactional
    @Override
    public void removeAll(List<StudentReview> studentReviews) {
        studentReviewDao.removeAll(studentReviews);
    }

    @Override
    public List<StudentReview> getStudentReviewsByStudentId(Long studentId) {
        return studentReviewDao.getStudentReviewsByStudentId(studentId);
    }

    @Override
    public Optional<StudentReview> getLastStudentReviewByStudentIdAndThemeId(Long studentId, Long themeId) {
        return studentReviewDao.getLastStudentReviewByStudentIdAndThemeId(studentId, themeId);
    }

    @Override
    public Boolean isThemePassedByStudent(Long studentId, Long themeId) {
        return studentReviewDao.isThemePassedByStudent(studentId, themeId);
    }

    /* Метод устанавливает статус "Пройдено" выбранной теме и всем предыдущим для выбранного студента. Для этого используется
    * Пользователь По-Умолчанию и к нему привязываются все новосозданные ревью.
    *
    * @param studentId - ID выбранного студента
    * @param themeId - ID выбранной темы */
    @Transactional
    @Override
    public void setPassedThisAndPreviousThemesForStudent(Long studentId, Long themeId) {
        User student = userDao.getById(studentId);
        List<Theme> themesUpToPosition = themeDao.getAllThemesUpToPosition(themeDao.getById(themeId).getPosition());
        List<Theme> passedThemeByStudent = themeDao.getPassedThemesByUser(student.getVkId());
        themesUpToPosition.removeAll(passedThemeByStudent);

        User defaultUser = userDao.getByVkId(0).get();
        if (themesUpToPosition.size() > 0) {
            for (int i = 0; i < themesUpToPosition.size(); i++) {
                Review passedReviewToDefaultUser = new Review();
                passedReviewToDefaultUser.setUser(defaultUser);
                passedReviewToDefaultUser.setIsOpen(false);
                passedReviewToDefaultUser.setTheme(themesUpToPosition.get(i));
                passedReviewToDefaultUser.setDate(LocalDateTime.now());
                reviewDao.save(passedReviewToDefaultUser);

                StudentReview passedStudentReviewToDefaultUser = new StudentReview();
                passedStudentReviewToDefaultUser.setUser(userDao.getById(studentId));
                passedStudentReviewToDefaultUser.setReview(passedReviewToDefaultUser);
                passedStudentReviewToDefaultUser.setIsPassed(true);
                studentReviewDao.save(passedStudentReviewToDefaultUser);
            }
        }
    }
}

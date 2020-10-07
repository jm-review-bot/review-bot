package spring.app.dao.abstraction;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import spring.app.model.StudentReview;
import spring.app.model.Theme;

import java.util.List;
import java.util.Optional;

public interface StudentReviewDao extends GenericDao<Long, StudentReview> {

    Optional<StudentReview> getStudentReviewIfAvailableAndOpen(Long idUser);

    Long getNumberStudentReviewByIdReview(Long idReview);

    List<StudentReview> getAllStudentReviewsByStudentIdAndTheme(Long studentId, Theme theme);

    @Transactional(propagation = Propagation.MANDATORY)
    void deleteStudentReviewByVkId(Integer vkId);

    Optional<StudentReview> getStudentReviewByReviewIdAndStudentId(Long reviewId, Long studentId);

    Optional<StudentReview> getStudentReviewsByIdWithFetchReviewUserThemeAndReviewer(Long id);

    List<StudentReview> getOpenReviewByStudentVkId(Integer vkId);

    List<StudentReview> getAllStudentReviewsByReviewId(Long reviewId);

    List<StudentReview> getStudentReviewsByStudentId(Long studentId);

    Optional<StudentReview> getLastStudentReviewByStudentIdAndThemeId(Long studentId, Long themeId);

    Boolean isThemePassedByStudent(Long studentId, Long themeId);
}

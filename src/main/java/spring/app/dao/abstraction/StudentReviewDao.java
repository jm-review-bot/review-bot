package spring.app.dao.abstraction;

import spring.app.model.StudentReview;
import spring.app.model.Theme;

import java.util.List;

public interface StudentReviewDao extends GenericDao<Long, StudentReview> {

    StudentReview getStudentReviewIfAvailableAndOpen(Long idUser);

    Long getNumberStudentReviewByIdReview(Long idReview);

    List<StudentReview> getAllStudentReviewsByStudentVkIdAndTheme(Long vkId, Theme theme);

    void bulkDeleteByUserId(Long id);

    void deleteStudentReviewByVkId(Integer vkId);

    StudentReview getStudentReviewByReviewIdAndStudentId(Long reviewId, Long studentId);

    StudentReview getStudentReviewsByIdWithFetchReviewUserThemeAndReviewer(Long id);
}

package spring.app.service.abstraction;

import spring.app.model.StudentReview;
import spring.app.model.Theme;

import java.util.List;

public interface StudentReviewService {

    void addStudentReview(StudentReview studentReview);

    StudentReview getStudentReviewById(Long id);

    List<StudentReview> getAllStudentReview();

    List<StudentReview> getAllStudentReviewsByStudentIdAndTheme(Long studentId, Theme theme);

    void updateStudentReview(StudentReview studentReview);

    void deleteStudentReviewById(Long id);

    StudentReview getStudentReviewIfAvailableAndOpen(Long idUser);

    Long getNumberStudentReviewByIdReview(Long idReview);

    StudentReview getStudentReviewByReviewIdAndStudentId(Long reviewId, Long studentId);

    void deleteStudentReviewByVkId(Integer vkId);

    List<StudentReview> getOpenReviewByStudentVkId(Integer vkId);

    StudentReview getStudentReviewsByIdWithFetchReviewUserThemeAndReviewer(Long id);

    List<StudentReview> getAllStudentReviewsByReviewId(Long reviewId);

    void removeAll(List<StudentReview> studentReviews);

    List<StudentReview> getStudentReviewsByStudentId(Long studentId);

    StudentReview getLastStudentReviewByStudentIdAndThemeId(Long studentId, Long themeId);

    Boolean isThemePassedByStudent(Long studentId, Long themeId);

    void setPassedThisAndPreviousThemesForStudent(Long studentId, Long themeId);
}

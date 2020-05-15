package spring.app.service.abstraction;

import spring.app.model.StudentReview;
import spring.app.model.Theme;

import java.util.List;

public interface StudentReviewService {

    void addStudentReview(StudentReview studentReview);

    StudentReview getStudentReviewById(Long id);

    List<StudentReview> getAllStudentReview();

    List<StudentReview> getAllStudentReviewsByStudentVkIdAndTheme(Long vkId, Theme theme);

    void updateStudentReview(StudentReview studentReview);

    void deleteStudentReviewById(Long id);

    StudentReview getStudentReviewIfAvailableAndOpen(Long idUser);

    Long getNumberStudentReviewByIdReview(Long idReview);

    StudentReview getStudentReviewByReviewIdAndStudentId(Long reviewId, Long studentId);

    void deleteStudentReviewByVkId(Integer vkId);
}

package spring.app.service.abstraction;

import spring.app.model.StudentReview;

import java.util.List;

public interface StudentReviewService {

    void addStudentReview(StudentReview studentReview);

    StudentReview getStudentReviewById(Long id);

    List<StudentReview> getAllStudentReview();

    void updateStudentReview(StudentReview studentReview);

    void deleteStudentReviewById(Long id);

    StudentReview getStudentReviewIfAvailableAndOpen(Long idUser);

    Long getNumberStudentReviewByIdReview(Long idReview);

    StudentReview getStudentReviewByReviewIdAndStudentId (Long reviewId, Long studentId);

    void deleteStudentReviewByVkId(Integer vkId);

    List<StudentReview> getOpenReviewByStudentVkId(Integer vkId);
}

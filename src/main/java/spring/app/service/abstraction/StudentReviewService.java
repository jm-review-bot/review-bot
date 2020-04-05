package spring.app.service.abstraction;

import spring.app.model.StudentReview;

import java.util.List;

public interface StudentReviewService {

    void addStudentReview(StudentReview studentReview);

    StudentReview getStudentReviewById(Long id);

    List<StudentReview> getAllStudentReview();

    void updateStudentReview(StudentReview studentReview);

    void deleteStudentReviewById(Long id);

    void deleteStudentReviewByVkId(Integer vkId);
}

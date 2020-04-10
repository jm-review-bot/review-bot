package spring.app.service.abstraction;

import spring.app.model.StudentReviewAnswer;

import java.util.List;

public interface StudentReviewAnswerService {

    void addStudentReviewAnswer(StudentReviewAnswer studentReviewAnswer);

    StudentReviewAnswer getStudentReviewAnswerById(Long id);

    List<StudentReviewAnswer> getAllStudentReviewAnswers();

    void updateStudentReviewAnswer(StudentReviewAnswer studentReviewAnswer);

    void deleteStudentReviewAnswerById(Long id);

    List<StudentReviewAnswer> getStudentReviewAnswersByStudentReviewId (Long studentReviewId);

}

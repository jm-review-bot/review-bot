package spring.app.dao.abstraction;

import spring.app.model.StudentReviewAnswer;

import java.util.List;

public interface StudentReviewAnswerDao extends GenericDao<Long, StudentReviewAnswer> {

    void bulkDeleteByUserId(Long id);

    List<StudentReviewAnswer> getStudentReviewAnswersByStudentReviewId(Long studentReviewId);

    List<StudentReviewAnswer> getStudentReviewAnswersByQuestionId(Long questionId);
}

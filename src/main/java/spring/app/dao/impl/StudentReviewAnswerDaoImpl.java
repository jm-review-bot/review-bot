package spring.app.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import spring.app.dao.abstraction.StudentReviewAnswerDao;
import spring.app.model.StudentReview;
import spring.app.model.StudentReviewAnswer;

import java.util.List;

@Repository
public class StudentReviewAnswerDaoImpl extends AbstractDao<Long, StudentReviewAnswer> implements StudentReviewAnswerDao {

    public StudentReviewAnswerDaoImpl() {
        super(StudentReviewAnswer.class);
    }

    @Override
    public List<StudentReviewAnswer> getStudentReviewAnswersByStudentReviewId(Long studentReviewId) {
        return entityManager.createQuery("SELECT sra FROM StudentReviewAnswer sra LEFT JOIN FETCH sra.question q WHERE sra.studentReview.id = :student_review_id", StudentReviewAnswer.class)
                .setParameter("student_review_id", studentReviewId)
                .getResultList();
    }

    @Override
    public List<StudentReviewAnswer> getStudentReviewAnswersByQuestionId(Long questionId) {
        return entityManager.createQuery("SELECT sra FROM StudentReviewAnswer  sra WHERE  sra.question.id = :question_id", StudentReviewAnswer.class)
                .setParameter("question_id", questionId)
                .getResultList();
    }
}

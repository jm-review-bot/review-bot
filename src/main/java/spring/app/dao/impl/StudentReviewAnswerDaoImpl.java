package spring.app.dao.impl;

import org.springframework.stereotype.Repository;
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
    public List<StudentReviewAnswer> getStudentReviewAnswersByStudentReviewId (Long studentReviewId) {
        return entityManager.createQuery("SELECT sra FROM StudentReviewAnswer sra WHERE sra.studentReview.id = :student_review_id", StudentReviewAnswer.class)
                .setParameter("student_review_id", studentReviewId)
                .getResultList();
    }
}

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
    @Transactional(propagation = Propagation.MANDATORY)
    public void bulkDeleteByUserId(Long id) {
        // Write all pending changes to the DB
        entityManager.flush();
        // Remove all entities from the persistence context
        entityManager.clear();
        entityManager.createQuery("DELETE FROM StudentReviewAnswer x " +
                "WHERE x IN (SELECT sra FROM StudentReviewAnswer sra " +
                "JOIN StudentReview sr ON sra.studentReview.id = sr.id " +
                "WHERE sr.user.id =:id)")
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public List<StudentReviewAnswer> getStudentReviewAnswersByStudentReviewId(Long studentReviewId) {
        return entityManager.createQuery("SELECT sra FROM StudentReviewAnswer sra LEFT JOIN FETCH sra.question q WHERE sra.studentReview.id = :student_review_id", StudentReviewAnswer.class)
                .setParameter("student_review_id", studentReviewId)
                .getResultList();
    }
}

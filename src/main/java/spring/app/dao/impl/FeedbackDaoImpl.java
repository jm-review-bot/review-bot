package spring.app.dao.impl;

import org.springframework.stereotype.Repository;
import spring.app.dao.abstraction.FeedbackDao;
import spring.app.model.Feedback;

import java.util.List;


@Repository
public class FeedbackDaoImpl extends AbstractDao<Long, Feedback> implements FeedbackDao {
    FeedbackDaoImpl() {
        super(Feedback.class);
    }

    @Override
    public List<Feedback> getFeedbackByStudentReviewId(Long studentReviewId) {
        return entityManager.createQuery("SELECT f FROM Feedback f WHERE f.studentReview.id = :student_review_id", Feedback.class)
                .setParameter("student_review_id", studentReviewId)
                .getResultList();
    }
}

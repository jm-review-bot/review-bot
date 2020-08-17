package spring.app.dao.impl;

import org.springframework.stereotype.Repository;
import spring.app.dao.abstraction.FeedbackDao;
import spring.app.dto.FeedbackDto;
import spring.app.model.Feedback;

import java.util.List;


@Repository
public class FeedbackDaoImpl extends AbstractDao<Long, Feedback> implements FeedbackDao {
    FeedbackDaoImpl() {
        super(Feedback.class);
    }

    @Override
    public String getStudentCommentByFeedbackId(Long id) {
        return entityManager.createQuery("SELECT f.comment FROM Feedback f WHERE f.id = :idFeedback", String.class)
                .setParameter("idFeedback", id).getSingleResult();
    }

    @Override
    public FeedbackDto getFeedbackDtoById(Long id) {
        List<FeedbackDto> feedbackDtoByIdList = entityManager.createQuery("SELECT new spring.app.dto.FeedbackDto(f.id, f.studentReview.review.user.firstName, f.studentReview.review.user.lastName, f.user.firstName, f.user.lastName, f.comment, f.ratingReviewer, f.ratingReview) FROM Feedback f WHERE f.id =:id", FeedbackDto.class)
                .setParameter("id", id)
                .getResultList();
        return feedbackDtoByIdList.size() > 0 ? feedbackDtoByIdList.get(0) : null;
    }

    @Override
    public List<FeedbackDto> getAllFeedbacksDto() {
        return entityManager.createQuery("SELECT new spring.app.dto.FeedbackDto(f.id, f.studentReview.review.user.firstName, f.studentReview.review.user.lastName, f.user.firstName, f.user.lastName, f.comment, f.ratingReviewer, f.ratingReview) FROM Feedback f ORDER BY f.id", FeedbackDto.class)
                .getResultList();
    }

    @Override
    public List<Feedback> getFeedbackByStudentReviewId(Long studentReviewId) {
        return entityManager.createQuery("SELECT f FROM Feedback f WHERE f.studentReview.id = :student_review_id", Feedback.class)
                .setParameter("student_review_id", studentReviewId)
                .getResultList();
    }

    @Override
    public List<Feedback> getFeedbacksByStudentId(Long studentId) {
        return entityManager
                .createQuery("SELECT f FROM Feedback f WHERE f.user.id = :student_id", Feedback.class)
                .setParameter("student_id", studentId)
                .getResultList();
    }
}

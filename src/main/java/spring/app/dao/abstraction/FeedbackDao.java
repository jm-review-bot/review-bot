package spring.app.dao.abstraction;

import spring.app.model.Feedback;

import java.util.List;

public interface FeedbackDao extends GenericDao<Long, Feedback> {

    List<Feedback> getFeedbackByStudentReviewId(Long studentReviewId);

    List<Feedback> getFeedbacksByStudentId(Long studentId);
}

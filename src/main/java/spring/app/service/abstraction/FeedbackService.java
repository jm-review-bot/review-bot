package spring.app.service.abstraction;

import spring.app.model.Feedback;

import java.util.List;

public interface FeedbackService {

    void addFeedback(Feedback feedback);

    List<Feedback> getFeedbackByStudentReviewId(Long studentReviewId);

    void removeAll(List<Feedback> feedbacks);

    List<Feedback> getFeedbacksByStudentId(Long studentId);
}

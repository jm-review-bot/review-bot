package spring.app.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spring.app.model.Feedback;
import spring.app.model.Review;
import spring.app.model.StudentReview;
import spring.app.model.User;
import spring.app.service.abstraction.FeedbackService;
import spring.app.service.abstraction.ReviewService;
import spring.app.service.abstraction.StudentReviewService;


import javax.persistence.PreRemove;
import java.util.List;

@Component
public class UserListener {

    private static ReviewService reviewService;
    private static StudentReviewService studentReviewService;
    private static FeedbackService feedbackService;

    @Autowired
    public void setReviewService (ReviewService reviewService) {
        UserListener.reviewService = reviewService;
    }

    @Autowired
    public void setStudentReviewService (StudentReviewService studentReviewService) {
        UserListener.studentReviewService = studentReviewService;
    }

    @Autowired
    public void setFeedbackService (FeedbackService feedbackService ) {
        UserListener.feedbackService = feedbackService;
    }

    @PreRemove
    private void removeRelatedEntities(User user) {
        Long userId = user.getId();
        List<Review> allReviewsByUserId = reviewService.getAllReviewsByUserId(userId);
        reviewService.removeAll(allReviewsByUserId);
        List<StudentReview> allStudentReviewsByUserId = studentReviewService.getStudentReviewsByStudentId(userId);
        studentReviewService.removeAll(allStudentReviewsByUserId);
        List<Feedback> allFeedbacksByUserId = feedbackService.getFeedbackByStudentReviewId(userId);
        feedbackService.removeAll(allFeedbacksByUserId);
    }
}

package spring.app.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spring.app.model.Feedback;
import spring.app.model.StudentReview;
import spring.app.model.StudentReviewAnswer;
import spring.app.service.abstraction.FeedbackService;
import spring.app.service.abstraction.StudentReviewAnswerService;

import javax.persistence.PreRemove;
import java.util.List;

@Component
public class StudentReviewListener {

    private static StudentReviewAnswerService studentReviewAnswerService;

    private static FeedbackService feedbackService;

    @Autowired
    public void setStudentReviewAnswerService(StudentReviewAnswerService studentReviewAnswerService) {
        StudentReviewListener.studentReviewAnswerService = studentReviewAnswerService;
    }

    @Autowired
    public void setFeedbackService(FeedbackService feedbackService) {
        StudentReviewListener.feedbackService = feedbackService;
    }

    @PreRemove
    private void removeRelatedEntities(StudentReview studentReview) {
        Long studentReviewId = studentReview.getId();
        List<StudentReviewAnswer> studentReviewAnswersByStudentReviewId = studentReviewAnswerService.getStudentReviewAnswersByStudentReviewId(studentReviewId);
        studentReviewAnswerService.removeAll(studentReviewAnswersByStudentReviewId);

        List<Feedback> feedbackByStudentReviewId = feedbackService.getFeedbackByStudentReviewId(studentReviewId);
        feedbackService.removeAll(feedbackByStudentReviewId);
    }
}

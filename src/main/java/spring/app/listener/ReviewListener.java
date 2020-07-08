package spring.app.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spring.app.model.Review;
import spring.app.model.StudentReview;
import spring.app.service.abstraction.StudentReviewService;

import javax.persistence.PreRemove;
import java.util.List;

@Component
public class ReviewListener {

    private static StudentReviewService studentReviewService;

    @Autowired
    public void setStudentReviewService(StudentReviewService studentReviewService) {
        ReviewListener.studentReviewService = studentReviewService;
    }

    @PreRemove
    private void removeRelatedEntities(Review review) {
        List<StudentReview> allStudentReviewsByReviewId = studentReviewService.getAllStudentReviewsByReviewId(review.getId());
        studentReviewService.removeAll(allStudentReviewsByReviewId);
    }
}

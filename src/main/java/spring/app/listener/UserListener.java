package spring.app.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spring.app.model.Review;
import spring.app.model.User;
import spring.app.service.abstraction.ReviewService;


import javax.persistence.PreRemove;
import java.util.List;

@Component
public class UserListener {

    private static ReviewService reviewService;

    @Autowired
    public void setReviewService (ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PreRemove
    private void removeRelatedEntities(User user) {
        List<Review> allReviewsByUserId = reviewService.getAllReviewsByUserId(user.getId());
        reviewService.deleteListRevies(allReviewsByUserId);
    }
}

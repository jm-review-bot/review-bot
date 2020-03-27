package spring.app.service.abstraction;

import spring.app.model.Review;

import java.util.List;

public interface ReviewService {

    void addReview(Review review);

    Review getReviewById(Long id);

    List<Review> getAllReviews();

    void updateReview(Review review);

    void deleteReviewById(Long id);

    List<Review> getAllExpiredReviews(String localDateTime);

}

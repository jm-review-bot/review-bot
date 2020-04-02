package spring.app.service.abstraction;

import spring.app.model.Review;

import java.time.LocalDateTime;
import java.util.List;

public interface ReviewService {

    void addReview(Review review);

    Review getReviewById(Long id);

    List<Review> getAllReviews();

    void updateReview(Review review);

    void deleteReviewById(Long id);

    List<Review> getReviewsByUserVkIdAndReviewPeriod(Integer vkId, LocalDateTime periodStart, LocalDateTime periodEnd);

    void updateAllExpiredReviewsBy(LocalDateTime localDateTime);

    List<Review> getOpenReviewsByReviewerVkId(Integer vkId);

    Review getOpenReviewByStudentVkId(Integer vkId);
}

package spring.app.service.abstraction;

import spring.app.model.Review;
import spring.app.model.Theme;

import java.time.LocalDateTime;
import java.util.List;

public interface ReviewService {

    void addReview(Review review);

    Review getReviewById(Long id);

    List<Review> getAllReviews();

    void updateReview(Review review);

    void deleteReviewById(Long id);

    List<Review> getOpenReviewsByReviewerVkId(Integer vkId, LocalDateTime periodStart, int reviewDuration);

    void updateAllExpiredReviewsBy(LocalDateTime localDateTime);

    List<Review> getAllReviewsByTheme(Long id, Theme theme, LocalDateTime localDateTime);

    List<Review> getOpenReviewsByReviewerVkId(Integer vkId);

    Review getOpenReviewByStudentVkId(Integer vkId);
}

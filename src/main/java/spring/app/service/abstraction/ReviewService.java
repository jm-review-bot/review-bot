package spring.app.service.abstraction;

import spring.app.model.FixedTheme;
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

    List<Review> getOpenReviewsByStudentVkId(Integer vkId, LocalDateTime periodStart, int reviewDuration);

    void updateAllExpiredReviewsByDate(LocalDateTime localDateTime);

    List<Review> getMyReview(Integer vkId, LocalDateTime localDateTime);

    List<Review> getMyReviewForDate(Integer vkId, LocalDateTime localDateTime, Integer numberOfMinutes);

    List<Review> getAllReviewsByThemeAndNotMyReviews(Long id, FixedTheme fixedTheme, LocalDateTime localDateTime, LocalDateTime dateTimeMyReview, Integer numberOfMinutes);

    List<Review> getAllReviewsByTheme(Long id, Theme theme, LocalDateTime localDateTime);

    List<Review> getOpenReviewsByReviewerVkId(Integer vkId);

    List<Review> getAllReviewsByUserId(Long id);

    void removeAll(List<Review> reviews);
}

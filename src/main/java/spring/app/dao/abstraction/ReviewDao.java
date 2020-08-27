package spring.app.dao.abstraction;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import spring.app.model.Review;
import spring.app.model.Theme;

import java.time.LocalDateTime;
import java.util.List;

public interface ReviewDao extends GenericDao<Long, Review> {

    List<Review> getOpenReviewsByReviewerVkId(Integer vkId, LocalDateTime periodStart, int reviewDuration);

    Long getCountOpenReviewsByReviewerVkId(Integer reviewerVkId);

    Long getCountCompletedReviewsByReviewerVkIdFromDate(Integer reviewerVkId, LocalDateTime startDateTime);

    @Transactional(propagation = Propagation.MANDATORY)
    void updateAllExpiredReviewsByDate(LocalDateTime localDateTime);

    List<Review> getOpenReviewsByReviewerVkId(Integer vkId);

    List<Review> getMyReview(Integer vkId, LocalDateTime localDateTime);

    List<Review> getMyReviewForDate(Integer vkId, LocalDateTime localDateTime, Integer numberOfMinutes);

    List<Review> getAllReviewsByThemeAndNotMyReviews(Long id, Theme theme, LocalDateTime localDateTime, LocalDateTime dateTimeMyReview, Integer numberOfMinutes);

    List<Review> getAllReviewsByTheme(Long id, Theme theme, LocalDateTime localDateTime);

    List<Review> getReviewsByThemeId(Long themeId);

    List<Review> getOpenReviewsByStudentVkId(Integer vkId, LocalDateTime periodStart, int reviewDuration);

    List<Review> getAllReviewsByUserId(Long id);
}

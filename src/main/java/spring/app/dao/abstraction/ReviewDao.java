package spring.app.dao.abstraction;

import spring.app.model.Review;
import spring.app.model.Theme;

import java.time.LocalDateTime;
import java.util.List;

public interface ReviewDao extends GenericDao<Long, Review> {

    List<Review> getOpenReviewsByReviewerVkId(Integer vkId, LocalDateTime periodStart, int reviewDuration);

    void updateAllExpiredReviewsByDate(LocalDateTime localDateTime);

    List<Review> getOpenReviewsByReviewerVkId(Integer vkId);

    Review getOpenReviewByStudentVkId(Integer vkId);

    List<Review> getAllReviewsByTheme(Long id, Theme theme, LocalDateTime localDateTime);

    List<Review> getOpenReviewsByStudentVkId(Integer vkId, LocalDateTime periodStart, int reviewDuration);

    void bulkDeleteByUserId(Long id);
}

package spring.app.dao.abstraction;

import spring.app.model.Review;

import java.time.LocalDateTime;
import java.util.List;

public interface ReviewDao extends GenericDao<Long, Review> {

    List<Review> getOpenReviewsByReviewerVkId(Integer vkId, LocalDateTime periodStart, int reviewDuration);

    void updateAllExpiredReviewsBy(LocalDateTime localDateTime);

    List<Review> getOpenReviewsByReviewerVkId(Integer vkId);

    Review getOpenReviewByStudentVkId(Integer vkId);
}

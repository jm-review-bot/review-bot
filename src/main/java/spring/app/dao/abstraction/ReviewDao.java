package spring.app.dao.abstraction;

import spring.app.model.Review;

import java.time.LocalDateTime;
import java.util.List;

public interface ReviewDao extends GenericDao<Long, Review> {

    List<Review> getReviewsByUserVkIdAndReviewPeriod(Integer vkId, LocalDateTime periodStart, LocalDateTime periodEnd);

    void updateAllExpiredReviewsBy(LocalDateTime localDateTime);
}

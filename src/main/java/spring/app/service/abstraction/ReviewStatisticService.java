package spring.app.service.abstraction;

import org.springframework.transaction.annotation.Transactional;
import spring.app.model.ReviewStatistic;

public interface ReviewStatisticService {

    @Transactional
    void addReviewStatistic(ReviewStatistic reviewStatistic);

    @Transactional
    void updateReviewStatistic(ReviewStatistic reviewStatistic);

    ReviewStatistic getReviewStatisticByUserVkId(Integer userVkId);

    ReviewStatistic getReviewStatisticByUserId(Long userId);

}

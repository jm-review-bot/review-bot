package spring.app.service.abstraction;

import org.springframework.transaction.annotation.Transactional;
import spring.app.model.ReviewStatistic;

public interface ReviewStatisticService {

    @Transactional
    void updateReviewStatistic(ReviewStatistic reviewStatistic);

    ReviewStatistic getReviewStatisticByUserVkId(Integer userVkId);

    ReviewStatistic getReviewStatisticByUserId(Long userId);

    @Transactional
    ReviewStatistic getUpdatedStatisticForUser(Integer userVkId);

    @Transactional
    void unblockTakingReviewForUser(Long userId);

}

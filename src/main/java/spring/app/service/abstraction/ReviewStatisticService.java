package spring.app.service.abstraction;

import spring.app.model.ReviewStatistic;

public interface ReviewStatisticService {

    void updateReviewStatistic(ReviewStatistic reviewStatistic);

    void startReviewStatisticForUser(Integer userVkId);

    ReviewStatistic getReviewStatisticByUserVkId(Integer userVkId);

    ReviewStatistic getReviewStatisticByUserId(Long userId);

    void unblockTakingReviewForUser(Long userId);

}

package spring.app.service.abstraction;

import spring.app.model.ReviewStatistic;

import java.util.Optional;

public interface ReviewStatisticService {

    void updateReviewStatistic(ReviewStatistic reviewStatistic);

    void startReviewStatisticForUser(Integer userVkId);

    Optional<ReviewStatistic> getReviewStatisticByUserVkId(Integer userVkId);

    Optional<ReviewStatistic> getReviewStatisticByUserId(Long userId);

    void unblockTakingReviewForUser(Long userId);

}

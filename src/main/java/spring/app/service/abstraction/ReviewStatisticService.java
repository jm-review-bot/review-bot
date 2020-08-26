package spring.app.service.abstraction;

import spring.app.model.ReviewStatistic;

public interface ReviewStatisticService {

    void addReviewStatistic(ReviewStatistic reviewStatistic);

    ReviewStatistic getReviewStatisticByUserId(Long userId);

    void updateReviewStatistic(ReviewStatistic reviewStatistic);

}

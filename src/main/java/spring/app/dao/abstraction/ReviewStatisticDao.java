package spring.app.dao.abstraction;

import spring.app.model.ReviewStatistic;

public interface ReviewStatisticDao extends GenericDao<Long, ReviewStatistic> {

    ReviewStatistic getReviewStatisticByUserId(Long userId);
}

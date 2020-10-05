package spring.app.dao.abstraction;

import spring.app.model.ReviewStatistic;

import java.util.Optional;

public interface ReviewStatisticDao extends GenericDao<Long, ReviewStatistic> {

    Optional<ReviewStatistic> getReviewStatisticByUserVkId(Integer userVkId);

    Optional<ReviewStatistic> getReviewStatisticByUserId(Long userId);
}

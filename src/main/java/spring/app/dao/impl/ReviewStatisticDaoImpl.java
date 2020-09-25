package spring.app.dao.impl;

import org.springframework.stereotype.Repository;
import spring.app.dao.abstraction.ReviewStatisticDao;
import spring.app.model.ReviewStatistic;
import spring.app.util.SingleResultHelper;

import java.util.Optional;

@Repository
public class ReviewStatisticDaoImpl extends AbstractDao<Long, ReviewStatistic> implements ReviewStatisticDao {

    private final SingleResultHelper<ReviewStatistic> singleResultHelper = new SingleResultHelper<>();

    public ReviewStatisticDaoImpl() {
        super(ReviewStatistic.class);
    }

    @Override
    public Optional<ReviewStatistic> getReviewStatisticByUserVkId(Integer userVkId) {
        return singleResultHelper.singleResult(entityManager.createQuery("SELECT rs FROM ReviewStatistic rs WHERE rs.user.vkId = :user_vkId", ReviewStatistic.class)
                .setParameter("user_vkId", userVkId));
    }

    @Override
    public Optional<ReviewStatistic> getReviewStatisticByUserId(Long userId) {
        return singleResultHelper.singleResult(entityManager.createQuery("SELECT rs FROM ReviewStatistic rs WHERE rs.user.id = :user_id", ReviewStatistic.class)
                .setParameter("user_id", userId));
    }
}

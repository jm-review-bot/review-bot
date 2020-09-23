package spring.app.dao.impl;

import org.springframework.stereotype.Repository;
import spring.app.dao.abstraction.ReviewStatisticDao;
import spring.app.model.ReviewStatistic;
import spring.app.util.SingleResultHelper;

import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

@Repository
public class ReviewStatisticDaoImpl extends AbstractDao<Long, ReviewStatistic> implements ReviewStatisticDao {

    private final SingleResultHelper<ReviewStatistic> singleResultHelper = new SingleResultHelper<>();

    public ReviewStatisticDaoImpl() {
        super(ReviewStatistic.class);
    }

    @Override
    public Optional<ReviewStatistic> getReviewStatisticByUserVkId(Integer userVkId) {
        Query query = entityManager.createQuery("SELECT rs FROM ReviewStatistic rs WHERE rs.user.vkId = :user_vkId", ReviewStatistic.class)
                .setParameter("user_vkId", userVkId);
        return singleResultHelper.singleResult(query);
    }

    @Override
    public Optional<ReviewStatistic> getReviewStatisticByUserId(Long userId) {
        Query query = entityManager.createQuery("SELECT rs FROM ReviewStatistic rs WHERE rs.user.id = :user_id", ReviewStatistic.class)
                .setParameter("user_id", userId);
        return singleResultHelper.singleResult(query);
    }
}

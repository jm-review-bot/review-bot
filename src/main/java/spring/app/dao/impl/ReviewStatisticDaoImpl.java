package spring.app.dao.impl;

import org.springframework.stereotype.Repository;
import spring.app.dao.abstraction.ReviewStatisticDao;
import spring.app.model.ReviewStatistic;

import java.util.List;

@Repository
public class ReviewStatisticDaoImpl extends AbstractDao<Long, ReviewStatistic> implements ReviewStatisticDao {

    public ReviewStatisticDaoImpl() {
        super(ReviewStatistic.class);
    }

    @Override
    public ReviewStatistic getReviewStatisticByUserVkId(Integer userVkId) {
        List<ReviewStatistic> reviewStatisticList = entityManager.createQuery("SELECT rs FROM ReviewStatistic rs WHERE rs.user.vkId = :user_vkId", ReviewStatistic.class)
                .setParameter("user_vkId", userVkId)
                .getResultList();
        return reviewStatisticList.size() > 0 ? reviewStatisticList.get(0) : null;
    }

    @Override
    public ReviewStatistic getReviewStatisticByUserId(Long userId) {
        List<ReviewStatistic> reviewStatisticList = entityManager.createQuery("SELECT rs FROM ReviewStatistic rs WHERE rs.user.id = :user_id", ReviewStatistic.class)
                .setParameter("user_id", userId)
                .getResultList();
        return reviewStatisticList.size() > 0 ? reviewStatisticList.get(0) : null;
    }
}

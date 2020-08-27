package spring.app.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.app.dao.abstraction.ReviewStatisticDao;
import spring.app.model.ReviewStatistic;
import spring.app.service.abstraction.ReviewStatisticService;

@Service
public class ReviewStatisticServiceImpl implements ReviewStatisticService {

    private final ReviewStatisticDao reviewStatisticDao;

    public ReviewStatisticServiceImpl(ReviewStatisticDao reviewStatisticDao) {
        this.reviewStatisticDao = reviewStatisticDao;
    }

    @Transactional
    @Override
    public void addReviewStatistic(ReviewStatistic reviewStatistic) {
        reviewStatisticDao.save(reviewStatistic);
    }

    @Override
    public void updateReviewStatistic(ReviewStatistic reviewStatistic) {
        reviewStatisticDao.update(reviewStatistic);
    }

    @Override
    public ReviewStatistic getReviewStatisticByUserVkId(Integer userVkId) {
        return reviewStatisticDao.getReviewStatisticByUserVkId(userVkId);
    }

    @Override
    public ReviewStatistic getReviewStatisticByUserId(Long userId) {
        return reviewStatisticDao.getReviewStatisticByUserId(userId);
    }

}

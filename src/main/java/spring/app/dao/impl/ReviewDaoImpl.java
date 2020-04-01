package spring.app.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import spring.app.dao.abstraction.ReviewDao;
import spring.app.model.Review;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Query;

@Repository
public class ReviewDaoImpl extends AbstractDao<Long, Review> implements ReviewDao {

    public ReviewDaoImpl() {
        super(Review.class);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void updateAllExpiredReviewsBy(LocalDateTime localDateTime) {
        final String CLOSE_EXPIRED_REVIEWS = "UPDATE Review e SET e.isOpen = false WHERE e.date < :localDateTime and e.isOpen = true";
        Query query = entityManager.createQuery(CLOSE_EXPIRED_REVIEWS);
        query.setParameter("localDateTime", localDateTime);
        query.executeUpdate();
    }

    @Override
    public List<Review> getReviewsByUserVkIdAndReviewPeriod(Integer vkId, LocalDateTime periodStart, LocalDateTime periodEnd) {
        return (List<Review>) entityManager.createNativeQuery("SELECT r.* FROM review r JOIN users u ON r.reviewer_id = u.id WHERE u.vk_id =:vk_id AND " +
                "r.date BETWEEN :period_start AND :period_end AND r.is_open = true OR (r.date + INTERVAL '59' MINUTE) BETWEEN :period_start AND :period_end AND r.is_open = true", Review.class)
                .setParameter("period_start", periodStart)
                .setParameter("period_end", periodEnd)
                .setParameter("vk_id", vkId)
                .getResultList();
    }
}
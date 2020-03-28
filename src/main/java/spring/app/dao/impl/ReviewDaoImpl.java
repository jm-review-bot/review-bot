package spring.app.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import spring.app.dao.abstraction.ReviewDao;
import spring.app.model.Review;

import javax.persistence.Query;
import java.time.LocalDateTime;

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

}

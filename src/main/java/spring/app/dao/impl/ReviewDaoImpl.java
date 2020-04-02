package spring.app.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import spring.app.dao.abstraction.ReviewDao;
import spring.app.model.Review;
import spring.app.model.Theme;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;

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

    public List<Review> getAllReviewsByTheme(Theme theme) {
//        String hql = "SELECT r FROM Review r INNER JOIN (SELECT COUNT(*) AS all FROM StudentReview sr) b ON b.all < 3 WHERE r.theme = :theme AND r.isOpen = true";
        /*
        SELECT r,COUNT(*) AS all
FROM Review r
join student_review sr ON sr.review.id = r.id
where r.theme_id = :theme and r.is_open = true
group by r
having count(*) <3
        */
        // TODO добавить проверку на количество записей в таблице StudentReview
        String hql = "SELECT r FROM Review r JOIN FETCH r.user WHERE r.theme.id = :theme AND r.isOpen = true";
        TypedQuery<Review> query = entityManager.createQuery(hql, Review.class);
        query.setParameter("theme", theme.getId());
        return query.getResultList();
    }

}

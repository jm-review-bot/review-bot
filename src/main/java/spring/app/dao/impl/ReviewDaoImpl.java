package spring.app.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import spring.app.dao.abstraction.ReviewDao;
import spring.app.model.Review;
import spring.app.model.StudentReview;
import spring.app.model.Theme;

import javax.persistence.NoResultException;
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

    @Override
    public List<Review> getOpenReviewsByReviewerVkId(Integer vkId, LocalDateTime periodStart, int reviewDuration) {
        return (List<Review>) entityManager.createNativeQuery("SELECT r.* FROM review r JOIN users u ON r.reviewer_id = u.id WHERE u.vk_id =:vk_id AND " +
                "r.date BETWEEN :period_start AND :period_end AND r.is_open = true OR" +
                " (r.date + (INTERVAL '1' MINUTE * :review_duration)) BETWEEN :period_start AND :period_end AND r.is_open = true", Review.class)
                .setParameter("period_start", periodStart)
                .setParameter("period_end", periodStart.plusMinutes(reviewDuration))
                .setParameter("vk_id", vkId)
                .setParameter("review_duration", reviewDuration)
                .getResultList();
    }

    /**
     * Метод возвращает все открытые ревью, которые юзер с данным vkId будет принимать
     * @param vkId
     */
    @Override
    public List<Review> getOpenReviewsByReviewerVkId(Integer vkId) {
        return entityManager.createQuery("SELECT r FROM Review r WHERE r.user.vkId = :id AND r.isOpen = true", Review.class)
        .setParameter("id", vkId).getResultList();
    }

    @Override
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

    /**
     * Метод возвращает открытое ревью, на сдачу которого которое записался юзер с
     * @param vkId
     */
    @Override
    public Review getOpenReviewByStudentVkId(Integer vkId) throws NoResultException {
        return entityManager.createQuery(
                "SELECT sr FROM StudentReview sr JOIN FETCH sr.review srr JOIN FETCH srr.theme JOIN FETCH srr.user JOIN Review r ON r.id = sr.review.id WHERE r.isOpen = true AND sr.user.vkId = :vkId", StudentReview.class)
                .setParameter("vkId", vkId)
                .getSingleResult()
                .getReview();
    }
}
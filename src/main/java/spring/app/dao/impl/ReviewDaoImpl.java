package spring.app.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import spring.app.dao.abstraction.ReviewDao;
import spring.app.model.Review;
import spring.app.model.StudentReview;

import javax.persistence.NoResultException;
import javax.persistence.Query;
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
    public List<Review> getReviewsByUserVkIdAndReviewPeriod(Integer vkId, LocalDateTime periodStart, LocalDateTime periodEnd) {
        return (List<Review>) entityManager.createNativeQuery("SELECT r.* FROM review r JOIN users u ON r.reviewer_id = u.id WHERE u.vk_id =:vk_id AND " +
                "r.date BETWEEN :period_start AND :period_end AND r.is_open = true OR (r.date + INTERVAL '59' MINUTE) BETWEEN :period_start AND :period_end AND r.is_open = true", Review.class)
                .setParameter("period_start", periodStart)
                .setParameter("period_end", periodEnd)
                .setParameter("vk_id", vkId)
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
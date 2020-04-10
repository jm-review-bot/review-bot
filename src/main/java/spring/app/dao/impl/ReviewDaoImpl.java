package spring.app.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import spring.app.dao.abstraction.ReviewDao;
import spring.app.model.Review;
import spring.app.model.StudentReview;

import javax.persistence.NoResultException;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class ReviewDaoImpl extends AbstractDao<Long, Review> implements ReviewDao {

    public ReviewDaoImpl() {
        super(Review.class);
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void updateAllExpiredReviewsByDate(LocalDateTime localDateTime) {
        entityManager.createQuery("UPDATE Review e SET e.isOpen = false WHERE e.date < :localDateTime and e.isOpen = true")
                .setParameter("localDateTime", localDateTime)
                .executeUpdate();
    }

    /**
     * Метод возвращает все открытые ревью, которые будут пересекаться по времени с новым ревью, которое юзер хочет принять.
     * Например, если юзер планирует провести ревью 10:00 02.06.2020, продолжительностью 59 минут,
     * то метод вернет список всех открытых этим пользователем ревью в интервале с 09:01 по 10:59 02.06.2020
     *
     * @param vkId           - id юзера в vk.com
     * @param periodStart    - запланированное юзером время начала ревью
     * @param reviewDuration - продолжительность ревью в минутах
     * @return
     */

    @Override
    public List<Review> getOpenReviewsByReviewerVkId(Integer vkId, LocalDateTime periodStart, int reviewDuration) {
        return (List<Review>) entityManager.createNativeQuery("SELECT r.* FROM review r JOIN users u ON r.reviewer_id = u.id WHERE " +
                "r.date BETWEEN :period_start AND :period_end AND r.is_open = true AND u.vk_id =:vk_id OR" +
                " (r.date + (INTERVAL '1' MINUTE * :review_duration)) BETWEEN :period_start AND :period_end AND r.is_open = true AND u.vk_id =:vk_id", Review.class)
                .setParameter("period_start", periodStart)
                .setParameter("period_end", periodStart.plusMinutes(reviewDuration))
                .setParameter("vk_id", vkId)
                .setParameter("review_duration", reviewDuration)
                .getResultList();
    }

    /**
     * Метод возвращает все открытые ревью, которые юзер с данным vkId будет принимать
     *
     * @param vkId
     */
    @Override
    public List<Review> getOpenReviewsByReviewerVkId(Integer vkId) {
        return entityManager.createQuery("SELECT r FROM Review r WHERE r.user.vkId = :id AND r.isOpen = true", Review.class)
                .setParameter("id", vkId).getResultList();
    }

    /**
     * Метод возвращает открытое ревью, на сдачу которого которое записался юзер с
     *
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

    @Override
    @Transactional(propagation= Propagation.MANDATORY)
    public void bulkDeleteByUserId(Long id) {
        // Write all pending changes to the DB
        entityManager.flush();
        // Remove all entities from the persistence context
        entityManager.clear();
        entityManager.createQuery("DELETE FROM Review WHERE user.id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }
}
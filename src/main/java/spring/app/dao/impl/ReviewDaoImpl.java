package spring.app.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import spring.app.dao.abstraction.ReviewDao;
import spring.app.model.Review;
import spring.app.model.Theme;
import spring.app.model.StudentReview;

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

    /**
     * Метод закрывает просроченные ревью в указанное время
     *
     * @param localDateTime
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void updateAllExpiredReviewsBy(LocalDateTime localDateTime) {
        Query query = entityManager.createQuery("UPDATE Review e SET e.isOpen = false WHERE e.date < :localDateTime and e.isOpen = true");
        query.setParameter("localDateTime", localDateTime);
        query.executeUpdate();
    }

    /**
     * Метод возвращает ревью по выбранной теме при условии, что записанных на ревью менее трех
     *
     * @param theme
     */
    public List<Review> getAllReviewsByTheme(Theme theme) {
        // TODO добавить проверку на количество записей в таблице StudentReview
        return entityManager.createQuery("SELECT r FROM Review r join fetch r.user ru LEFT JOIN StudentReview sr ON r.id = sr.review.id WHERE r.theme = :theme AND r.isOpen = true group by r, ru having count(all r) < 3", Review.class)
                .setParameter("theme", theme)
                .getResultList();
    }

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
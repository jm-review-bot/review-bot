package spring.app.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import spring.app.dao.abstraction.ReviewDao;
import spring.app.model.Review;
import spring.app.model.Theme;
import spring.app.model.StudentReview;

import javax.persistence.NoResultException;
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
    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void updateAllExpiredReviewsByDate(LocalDateTime localDateTime) {
        entityManager.createQuery("UPDATE Review e SET e.isOpen = false WHERE e.date < :localDateTime and e.isOpen = true")
                .setParameter("localDateTime", localDateTime)
                .executeUpdate();
    }

    /**
     * Метод возвращает все открытые ревью, которые принимает юзер, которые будут пересекаться по времени с новым ревью, которое юзер хочет принять.
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
     * Метод возвращает все открытые ревью, в которых участвует юзер(как студент), которые будут пересекаться по времени с новым ревью, которое юзер хочет принять.
     * Например, если юзер планирует провести ревью 10:00 02.06.2020, продолжительностью 59 минут,
     * то метод вернет список всех ревью в которых принимает участие юзер(как студент) в интервале с 09:01 по 10:59 02.06.2020
     *
     * @param vkId           - id юзера в vk.com
     * @param periodStart    - запланированное юзером время начала ревью
     * @param reviewDuration - продолжительность ревью в минутах
     * @return
     */
    @Override
    public List<Review> getOpenReviewsByStudentVkId(Integer vkId, LocalDateTime periodStart, int reviewDuration) {
        return (List<Review>) entityManager.createNativeQuery("SELECT r.* FROM review r JOIN student_review sr ON sr.review_id = r.id JOIN users u ON sr.student_id = u.id WHERE " +
                "r.date BETWEEN :period_start AND :period_end AND r.is_open = true AND u.vk_id =:vk_id OR" +
                " (r.date + (INTERVAL '1' MINUTE * :review_duration)) BETWEEN :period_start AND :period_end AND r.is_open = true AND u.vk_id =:vk_id", Review.class)
                .setParameter("period_start", periodStart)
                .setParameter("period_end", periodStart.plusMinutes(reviewDuration))
                .setParameter("vk_id", vkId)
                .setParameter("review_duration", reviewDuration)
                .getResultList();
    }

    /**
     * Метод возвращает список моих открытых ревью
     *
     * @param vkId
     * @param localDateTime
     */
    @Override
    public List<Review> getMyReview(Integer vkId, LocalDateTime localDateTime) {
        return entityManager.createQuery("SELECT r FROM Review r WHERE r.user.vkId = :vkId AND r.isOpen = true AND r.date > :date", Review.class)
                .setParameter("vkId", vkId)
                .setParameter("date", localDateTime)
                .getResultList();
    }

    /**
     * Метод возвращает список моих открытых ревью, которые пересекаются с ревью дата которого передается параметром
     *
     * @param vkId
     * @param localDateTime
     * @param numberOfMinutes
     */
    @Override
    public List<Review> getMyReviewForDate(Integer vkId, LocalDateTime localDateTime, Integer numberOfMinutes) {
        return entityManager.createQuery("SELECT r FROM Review r join fetch r.theme rt WHERE r.user.vkId = :vkId AND r.isOpen = true AND r.date > :date_start AND r.date < :date_end", Review.class)
                .setParameter("vkId", vkId)
                .setParameter("date_start", localDateTime.minusMinutes(numberOfMinutes))
                .setParameter("date_end", localDateTime.plusMinutes(numberOfMinutes))
                .getResultList();
    }

    /**
     * Метод возвращает ревью по выбранной теме при условии, что записанных на ревью менее трех
     *
     * @param id               пользователя которого исключаем
     * @param theme
     * @param localDateTime
     * @param dateTimeMyReview
     * @param numberOfMinutes
     * @return
     */
    @Override
    public List<Review> getAllReviewsByThemeAndNotMyReviews(Long id, Theme theme, LocalDateTime localDateTime, LocalDateTime dateTimeMyReview, Integer numberOfMinutes) {
        return entityManager.createQuery("SELECT r FROM Review r join fetch r.user ru LEFT JOIN StudentReview sr ON r.id = sr.review.id WHERE r.theme = :theme AND r.isOpen = true AND r.date > :date AND r.date < :date_start OR r.date > :date_end AND r.user.id <> :id group by r, ru having count(all r) < 3", Review.class)
                .setParameter("id", id)
                .setParameter("theme", theme)
                .setParameter("date", localDateTime)
                .setParameter("date_start", dateTimeMyReview.minusMinutes(numberOfMinutes))
                .setParameter("date_end", dateTimeMyReview.plusMinutes(numberOfMinutes))
                .getResultList();
    }

    /**
     * Метод возвращает ревью по выбранной теме при условии, что записанных на ревью менее трех
     *
     * @param theme
     */
    @Override
    public List<Review> getAllReviewsByTheme(Long id, Theme theme, LocalDateTime localDateTime) {
        return entityManager.createQuery("SELECT r FROM Review r join fetch r.user ru LEFT JOIN StudentReview sr ON r.id = sr.review.id WHERE r.theme = :theme AND r.isOpen = true AND r.date > :date AND r.user.id <> :id group by r, ru having count(all r) < 3", Review.class)
                .setParameter("id", id)
                .setParameter("theme", theme)
                .setParameter("date", localDateTime)
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



    @Override
    @Transactional(propagation = Propagation.MANDATORY)
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
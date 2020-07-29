package spring.app.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import spring.app.dao.abstraction.UserDao;
import spring.app.dto.ReviewerDto;
import spring.app.model.FreeTheme;
import spring.app.model.Review;
import spring.app.model.User;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class UserDaoImpl extends AbstractDao<Long, User> implements UserDao {
    private static final Logger log = LoggerFactory.getLogger(UserDaoImpl.class);

    public UserDaoImpl() {
        super(User.class);
    }

    @Override
    public User getByVkId(Integer vkId) throws NoResultException {
        try {
            TypedQuery<User> query = entityManager.createQuery(
                    "SELECT u FROM User u WHERE u.vkId = :id", User.class);
            query.setParameter("id", vkId);
            return query.getSingleResult();
        } catch (NoResultException e) {
            log.error("Пользователь с vkId:{} не обнаружен в базе", vkId);
            throw e;
        }
    }

    @Override
    public boolean isExistByVkId(Integer vkId) {
        try {
            entityManager.createQuery("SELECT u FROM User u WHERE u.vkId = :id", User.class).setParameter("id", vkId).getSingleResult();
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void deleteUserByVkId(Integer vkId) throws NoResultException {
        // Write all pending changes to the DB
        entityManager.flush();
        // Remove all entities from the persistence context
        entityManager.clear();
        entityManager.createQuery("DELETE FROM User u WHERE u.vkId = :id")
                .setParameter("id", vkId)
                .executeUpdate();
    }

    @Override
    public List<User> getUsersByReviewPeriod(LocalDateTime periodStart, LocalDateTime periodEnd) {
        return (List<User>) entityManager.createNativeQuery("select u.* " +
                "from users u join review r on u.id = r.reviewer_id where r.is_open = true and r.date between :period_start and :period_end", User.class)
                .setParameter("period_start", periodStart)
                .setParameter("period_end", periodEnd)
                .getResultList();
    }

    @Override
    public List<User> getStudentsByReviewId(Long reviewId) {
        return entityManager.createQuery("SELECT u FROM StudentReview sr JOIN sr.user u JOIN sr.review r WHERE r.id = :review_id", User.class)
                .setParameter("review_id", reviewId)
                .getResultList();
    }

    @Override
    public List<User> getStudentsByReviewPeriod(LocalDateTime periodStart, LocalDateTime periodEnd) {
        return entityManager.createNativeQuery("SELECT u.* FROM users u JOIN student_review sr ON u.id = sr.student_id " +
                "WHERE sr.review_id IN " +
                "(SELECT r.id FROM review r WHERE r.is_open = TRUE AND r.date BETWEEN :period_start AND :period_end)", User.class)
                .setParameter("period_start", periodStart)
                .setParameter("period_end", periodEnd)
                .getResultList();
    }

    @Override
    public boolean isUserExaminer(Long userId) {
        Long count = entityManager.createQuery("SELECT COUNT (t) FROM FreeTheme t JOIN t.examiners u where u.id = : user_id", Long.class)
                .setParameter("user_id", userId)
                .getSingleResult();
        return count > 0;
    }

    @Override
    public List<ReviewerDto> getExaminersInThisTheme(long themeId) {
      List<ReviewerDto> reviewers = entityManager.createQuery("select distinct new spring.app.dto.ReviewerDto(u.id,u.firstName,u.lastName) from FreeTheme ft join ft.examiners u where ft.id =:theme_id")
                .setParameter("theme_id" , themeId)
                .getResultList();
        return reviewers.size() > 0 ? reviewers : null;
    }

    @Override
    public List<ReviewerDto> getExaminersInNotThisTheme(long themeId) {
        List<ReviewerDto> reviewers =  entityManager.createQuery("select distinct new spring.app.dto.ReviewerDto(u.id , u.firstName , u.lastName) from FreeTheme ft join ft.examiners u where ft.id <>:theme_id")
                .setParameter("theme_id" , themeId)
                .getResultList();
        return reviewers.size() > 0 ? reviewers : null;
    }

    @Override
    public User addNewReviewer(User user) {
        return entityManager.merge(user);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    @Override
    public void deleteReviewerFromTheme(long themeId, long reviewerId) {
        entityManager.createNativeQuery("delete from user_free_theme where free_theme_id =:theme_id and examiner_id =:reviewer_id")
                .setParameter("theme_id" , themeId)
                .setParameter("reviewer_id" , reviewerId)
                .executeUpdate();
    }

    @Override
    public List<User> getExaminersByFreeThemeId(Long freeThemeId) {
        return entityManager.createQuery("SELECT ft.examiners FROM FreeTheme ft WHERE ft.id = :free_theme_id")
                .setParameter("free_theme_id", freeThemeId)
                .getResultList();
    }

}

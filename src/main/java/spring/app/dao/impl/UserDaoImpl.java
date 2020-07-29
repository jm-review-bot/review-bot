package spring.app.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import spring.app.dao.abstraction.UserDao;
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
    public User getByUsername(String username) {
        List<User> userList = entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                .setParameter("username", username)
                .getResultList();
        return (userList.size() > 0 ? userList.get(0) : null);
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
}

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

@Repository
public class UserDaoImpl extends AbstractDao<Long, User> implements UserDao {
    private static final Logger log = LoggerFactory.getLogger(UserDaoImpl.class);

    public UserDaoImpl() {
        super(User.class);
    }

    public User getByVkId(Integer vkId) throws NoResultException {
        try {
            TypedQuery<User> query = entityManager.createQuery(
                    "SELECT u FROM User u WHERE u.vkId = :id", User.class);
            query.setParameter("id", vkId);
            return query.getSingleResult();
        } catch (NoResultException e) {
            log.info("Пользователь с vkId:{} не обнаружен в базе", vkId);
            throw e;
        }
    }

    public boolean isExistByVkId(Integer vkId) {
        try {
            entityManager.createQuery("SELECT u FROM User u WHERE u.vkId = :id", User.class).setParameter("id", vkId).getSingleResult();
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

    @Transactional(propagation= Propagation.MANDATORY)
    public void deleteUserByVkId(Integer vkId) throws NoResultException {
        entityManager.createQuery("DELETE FROM User u WHERE u.vkId = :id")
                .setParameter("id", vkId)
                .executeUpdate();
        entityManager.flush();
    }
}

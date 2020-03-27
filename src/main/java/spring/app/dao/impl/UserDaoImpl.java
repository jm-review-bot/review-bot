package spring.app.dao.impl;

import org.springframework.stereotype.Repository;
import spring.app.dao.abstraction.UserDao;
import spring.app.model.User;

import javax.persistence.TypedQuery;

@Repository
public class UserDaoImpl extends AbstractDao<Long, User> implements UserDao {

    public UserDaoImpl() {
        super(User.class);
    }

    public User getByVkId(Integer vkId) {
        TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.vkId = :id", User.class);
        query.setParameter("id", vkId);
        return query.getSingleResult();
    }

}

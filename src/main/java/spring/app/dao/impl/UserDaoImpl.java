package spring.app.dao.impl;

import org.springframework.stereotype.Repository;
import spring.app.dao.abstraction.UserDao;
import spring.app.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class UserDaoImpl extends AbstractDao<Long, User> implements UserDao {

    public UserDaoImpl() {
        super(User.class);
    }

    @Override
    public List<User> getUsersByReviewDate(LocalDateTime now) {

        // написать правильный запрос
        return (List<User>) entityManager.createNativeQuery("select * " +
                "from users join review where review.is_open = true and (:cur_time - review.date) > 2").setParameter("cur_time", now).getResultList();
    }
}

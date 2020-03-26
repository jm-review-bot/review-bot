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
    public List<User> getUsersByReviewPeriod(LocalDateTime periodStart, LocalDateTime periodEnd) {
        return (List<User>) entityManager.createNativeQuery("select u.* " +
                "from users u join review r on u.id = r.reviewer_id where r.is_open = true and r.date between :period_start and :period_end", User.class)
                .setParameter("period_start", periodStart)
                .setParameter("period_end", periodEnd)
                .getResultList();
    }
}

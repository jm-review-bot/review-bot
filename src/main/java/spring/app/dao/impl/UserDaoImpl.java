package spring.app.dao.impl;

import org.springframework.stereotype.Repository;
import spring.app.dao.abstraction.UserDao;
import spring.app.model.User;

@Repository
public class UserDaoImpl extends AbstractDao<Long, User> implements UserDao {

    public UserDaoImpl() {
        super(User.class);
    }

}

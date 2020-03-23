package spring.app.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import spring.app.dao.abstraction.UserDao;
import spring.app.model.User;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;


@Repository
public class UserDaoImpl extends AbstractDao<Long, User> implements UserDao {

	public UserDaoImpl() {
		super(User.class);
	}

}

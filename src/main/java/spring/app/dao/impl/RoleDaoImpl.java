package spring.app.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import spring.app.dao.abstraction.RoleDao;
import spring.app.model.Role;

import javax.persistence.TypedQuery;


@Repository
public class RoleDaoImpl extends AbstractDao<Long, Role> implements RoleDao {

	public RoleDaoImpl() {
		super(Role.class);
	}

}

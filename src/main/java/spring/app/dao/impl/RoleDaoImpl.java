package spring.app.dao.impl;

import org.springframework.stereotype.Repository;
import spring.app.dao.abstraction.RoleDao;
import spring.app.model.Role;

@Repository
public class RoleDaoImpl extends AbstractDao<Long, Role> implements RoleDao {

    public RoleDaoImpl() {
        super(Role.class);
    }

}

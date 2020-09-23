package spring.app.dao.impl;

import org.springframework.stereotype.Repository;
import spring.app.dao.abstraction.RoleDao;
import spring.app.model.Role;
import spring.app.util.SingleResultHelper;

import java.util.Optional;

@Repository
public class RoleDaoImpl extends AbstractDao<Long, Role> implements RoleDao {

    private final SingleResultHelper<Role> singleResultHelper = new SingleResultHelper<>();


    public RoleDaoImpl() {
        super(Role.class);
    }

    @Override
    public Optional<Role> getRoleByName(String roleName) {
        return singleResultHelper.singleResult(entityManager.createQuery("SELECT r FROM Role r WHERE r.name = :name", Role.class)
                .setParameter("name", roleName));
    }
}

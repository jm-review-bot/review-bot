package spring.app.dao.impl;

import org.springframework.stereotype.Repository;
import spring.app.dao.abstraction.RoleDao;
import spring.app.dto.RoleDto;
import spring.app.model.Role;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class RoleDaoImpl extends AbstractDao<Long, Role> implements RoleDao {

    public RoleDaoImpl() {
        super(Role.class);
    }

    @Override
    public Role getRoleByName(String roleName) {
        TypedQuery<Role> query = entityManager.createQuery("SELECT r FROM Role r WHERE r.name = :name", Role.class);
        query.setParameter("name", roleName);
        return query.getSingleResult();
    }

    @Override
    public List<RoleDto> getAllRolesDto() {
        return entityManager.createQuery("SELECT new spring.app.dto.RoleDto(r.id, r.name) FROM Role r", RoleDto.class)
                .getResultList();
    }
}

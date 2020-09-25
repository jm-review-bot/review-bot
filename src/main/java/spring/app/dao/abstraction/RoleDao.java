package spring.app.dao.abstraction;

import spring.app.dto.RoleDto;
import spring.app.model.Role;

import java.util.List;
import java.util.Optional;

public interface RoleDao extends GenericDao<Long, Role> {

    Optional<Role> getRoleByName(String roleName);

    List<RoleDto> getAllRolesDto();
}

package spring.app.dao.abstraction;

import spring.app.dto.RoleDto;
import spring.app.model.Role;

import java.util.List;

public interface RoleDao extends GenericDao<Long, Role> {
    Role getRoleByName(String roleName);

    List<RoleDto> rolesSearch(String searchString);
}

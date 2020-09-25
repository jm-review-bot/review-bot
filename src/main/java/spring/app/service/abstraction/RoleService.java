package spring.app.service.abstraction;

import spring.app.dto.RoleDto;
import spring.app.model.Role;

import java.util.List;
import java.util.Optional;


public interface RoleService {

	void addRole(Role role);

	Role getRoleById(Long id);

	List<Role> getAllRoles();

	void updateRole(Role role);

	void deleteRoleById(Long id);

	Optional<Role> getRoleByName(String roleName);

	List<RoleDto> getAllRolesDto();
}

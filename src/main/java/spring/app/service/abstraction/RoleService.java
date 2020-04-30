package spring.app.service.abstraction;

import spring.app.model.Role;

import javax.persistence.TypedQuery;
import java.util.List;


public interface RoleService {

	void addRole(Role role);

	Role getRoleById(Long id);

	List<Role> getAllRoles();

	void updateRole(Role role);

	void deleteRoleById(Long id);

	Role getRoleByName(String roleName);
}

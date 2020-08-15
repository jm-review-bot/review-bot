package spring.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.app.dao.abstraction.RoleDao;
import spring.app.dto.RoleDto;
import spring.app.model.Role;
import spring.app.service.abstraction.RoleService;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleDao roleDao;

    @Autowired
    public RoleServiceImpl(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Transactional
    @Override
    public void addRole(Role role) {
        roleDao.save(role);
    }

    @Override
    public Role getRoleById(Long id) {
        return roleDao.getById(id);
    }

    @Override
    public List<Role> getAllRoles() {
        return roleDao.getAll();
    }

    @Transactional
    @Override
    public void updateRole(Role role) {
        roleDao.update(role);
    }

    @Transactional
    @Override
    public void deleteRoleById(Long id) {
        roleDao.deleteById(id);
    }

    @Override
    public Role getRoleByName(String roleName) {
        return roleDao.getRoleByName(roleName);
    }

    @Override
    public List<RoleDto> rolesSearch(String searchString) {
        return roleDao.rolesSearch(searchString);
    }
}

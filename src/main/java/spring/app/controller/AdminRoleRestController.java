package spring.app.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.app.model.Role;
import spring.app.service.abstraction.RoleService;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/admin/role")
public class AdminRoleRestController {

    private final RoleService roleService;

    public AdminRoleRestController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public ResponseEntity<List<Role>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }
}

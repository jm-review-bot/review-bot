package spring.app.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.app.dto.RoleDto;
import spring.app.service.abstraction.RoleService;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/admin/role")
@Api(value = "Roles controller")
public class AdminRoleRestController {

    private final RoleService roleService;

    public AdminRoleRestController(RoleService roleService) {
        this.roleService = roleService;
    }

    @ApiOperation(value = "Get all roles list")
    @GetMapping
    public ResponseEntity<List<RoleDto>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRolesDto());
    }
}

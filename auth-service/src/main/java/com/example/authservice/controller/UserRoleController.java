package com.example.authservice.controller;

import com.example.authservice.model.UserRole;
import com.example.authservice.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/role")
public class UserRoleController {

    @Autowired
    UserRoleService userRoleService;

    @GetMapping("/list")
    public Iterable<UserRole> listRoles() {
        return userRoleService.listRoles();
    }

    @PostMapping
    public UserRole createUserRole(@Valid @RequestBody UserRole userRole) {
        return userRoleService.createUserRole(userRole);
    }

    @DeleteMapping("/{roleId}")
    public Integer deleteUserRoleById(@PathVariable Integer roleId) {
        return userRoleService.deleteUserRoleById(roleId);
    }
}

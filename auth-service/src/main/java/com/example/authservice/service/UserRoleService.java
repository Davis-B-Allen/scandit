package com.example.authservice.service;

import com.example.authservice.model.UserRole;

public interface UserRoleService {

    UserRole getUserRoleByName(String roleName);

    Iterable<UserRole> listRoles();

    UserRole createUserRole(UserRole userRole);

    Integer deleteUserRoleById(Integer roleId);
}

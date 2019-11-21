package com.example.userservice.service;

import com.example.userservice.model.UserRole;

public interface UserRoleService {

    UserRole getUserRoleByName(String roleName);

    Iterable<UserRole> listRoles();

    UserRole createUserRole(UserRole userRole);

    Integer deleteUserRoleById(Integer roleId);
}

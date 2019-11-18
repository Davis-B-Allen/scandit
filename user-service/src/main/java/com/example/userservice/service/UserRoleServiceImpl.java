package com.example.userservice.service;

import com.example.userservice.model.UserRole;
import com.example.userservice.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserRoleServiceImpl implements UserRoleService {

    @Autowired
    UserRoleRepository userRoleRepository;

    @Override
    public UserRole getUserRoleByName(String name) {
        return userRoleRepository.findByName(name);
    }

    @Override
    public Iterable<UserRole> listRoles() {
        return userRoleRepository.findAll();
    }

    @Override
    public UserRole createUserRole(UserRole userRole) {
        return userRoleRepository.save(userRole);
    }

    @Override
    public Integer deleteUserRoleById(Integer roleId) {
        userRoleRepository.deleteById(roleId);
        return roleId;
    }
}

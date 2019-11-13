package com.example.authservice.service;

import com.example.authservice.model.UserRole;
import com.example.authservice.repository.UserRoleRepository;
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

package com.example.userservice.repository;

import com.example.userservice.model.UserRole;
import org.springframework.data.repository.CrudRepository;

public interface UserRoleRepository extends CrudRepository<UserRole, Integer> {

    UserRole findByName(String name);
}

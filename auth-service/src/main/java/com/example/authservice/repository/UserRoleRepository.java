package com.example.authservice.repository;

import com.example.authservice.model.UserRole;
import org.springframework.data.repository.CrudRepository;

public interface UserRoleRepository extends CrudRepository<UserRole, Integer> {

    UserRole findByName(String name);
}

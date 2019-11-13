package com.example.authservice.service;

import com.example.authservice.model.User;
import com.example.authservice.util.JwtResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    JwtResponse signup(User user);

    JwtResponse login(User user);
}

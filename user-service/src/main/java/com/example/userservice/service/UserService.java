package com.example.userservice.service;

import com.example.userservice.exception.LoginException;
import com.example.userservice.model.User;
import com.example.userservice.responseobject.JwtResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    JwtResponse signup(User user) throws LoginException;

    JwtResponse login(User user) throws Exception;

    User getUser(String username);
}

package com.example.authservice.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.example.authservice.model.User;
import com.example.authservice.model.UserRole;
import com.example.authservice.repository.UserRepository;
import com.example.authservice.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

public class UserServiceTest {

    private static final String USERNAME = "user";
    private static final String EMAIL = "user@example.com";
    private static final String PASSWORD = "password";
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_EMAIL = "admin@example.com";
    private static final String ADMIN_PASSWORD = "password";

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    PasswordEncoder bCryptPasswordEncoder;

    @Mock
    UserRepository userRepository;

    @Mock
    JwtUtil jwtUtil;

    private User user;
    private User adminUser;
    private UserRole userRoleUser;
    private UserRole userRoleAdmin;
    private ObjectMapper objectMapper;

    public UserServiceTest() {
        user = new User();
        adminUser = new User();
        userRoleUser = new UserRole();
        userRoleAdmin = new UserRole();
        objectMapper = new ObjectMapper();
        MockitoAnnotations.initMocks(this);
    }

    @BeforeEach
    public void init() {
        userRoleUser.setId(1);
        userRoleUser.setName("ROLE_USER");
        userRoleAdmin.setId(2);
        userRoleAdmin.setName("ROLE_ADMIN");

        user.setId(1L);
        user.setUsername(USERNAME);
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        List<UserRole> roles = new ArrayList<>();
        roles.add(userRoleUser);
        user.setUserRoles(roles);

        adminUser.setId(2L);
        adminUser.setUsername(ADMIN_USERNAME);
        adminUser.setEmail(ADMIN_EMAIL);
        adminUser.setPassword(ADMIN_PASSWORD);
        List<UserRole> adminRoles = new ArrayList<>();
        adminRoles.add(userRoleUser);
        adminRoles.add(userRoleAdmin);
        adminUser.setUserRoles(adminRoles);

        List<User> users = new ArrayList<>();
        List<User> adminUsers = new ArrayList<>();
        users.add(user);
        users.add(adminUser);
        adminUsers.add(adminUser);

        userRoleUser.setUsers(users);
        userRoleAdmin.setUsers(adminUsers);
    }

    @Test
    public void loadUserByUsername_ValidUserName_Success() {
        when(userRepository.findByUsername(USERNAME)).thenReturn(user);
        UserDetails userDetails = userService.loadUserByUsername(USERNAME);
        assertThat(userDetails.getUsername()).isEqualTo(USERNAME);
    }

    @Test void loadUserByUsername_InvalidUsername_UsernameNotFoundException() {
        when(userRepository.findByUsername(USERNAME)).thenReturn(null);
        Throwable thrown = catchThrowable(() -> userService.loadUserByUsername("notRealUsername"));
        assertThat(thrown).isInstanceOf(UsernameNotFoundException.class);
    }

}

package com.example.userservice.controller;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.userservice.config.SecurityConfig;
import com.example.userservice.exception.ExceptionHandler;
import com.example.userservice.model.User;
import com.example.userservice.model.UserRole;
import com.example.userservice.responseobject.JwtResponse;
import com.example.userservice.service.UserService;
import com.example.userservice.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {UserController.class, SecurityConfig.class, ExceptionHandler.class})
@WebMvcTest(UserController.class)
public class UserControllerTest {

    private static final String USERNAME = "user";
    private static final String EMAIL = "user@example.com";
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_EMAIL = "admin@example.com";
    private static final String PASSWORD = "password";

    private User user;
    private User adminUser;
    private UserRole userRoleUser;
    private UserRole userRoleAdmin;
    private String generatedToken;
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    UserService userService;

    @MockBean
    JwtUtil jwtUtil;

    public UserControllerTest() {
        userRoleUser = new UserRole();
        userRoleAdmin = new UserRole();
        user = new User();
        adminUser = new User();
        objectMapper = new ObjectMapper();
        generatedToken = "lkajsd3lr.3lkfnflan3.llk3lkahl";
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
        user.addUserRole(userRoleUser);

        adminUser = new User(2L, ADMIN_USERNAME, ADMIN_EMAIL, PASSWORD);
        List<UserRole> roles = new ArrayList<>();
        roles.add(userRoleUser);
        roles.add(userRoleAdmin);
        adminUser.setUserRoles(roles);

        List<User> users = new ArrayList<>();
        List<User> admins = new ArrayList<>();

        users.add(user);
        users.add(adminUser);
        admins.add(adminUser);
        userRoleUser.setUsers(users);
        userRoleAdmin.setUsers(admins);
    }

    @Test
    public void signup_ValidUser_Success() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user));

        JwtResponse jwtResponse = new JwtResponse(generatedToken, user.getUsername());
        when(userService.signup(any())).thenReturn(jwtResponse);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(jwtResponse)));
    }

    @Test
    public void signup_BlankUsername_Failure() throws Exception {
        user.setUsername("");
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user));

        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andReturn();

    }

    @Test
    public void login_ValidUser_ReturnsJsonAndUsername() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user));

        JwtResponse jwtResponse = new JwtResponse(generatedToken, user.getUsername());
        when(userService.login(any())).thenReturn(jwtResponse);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(jwtResponse)));
    }

//    @Test
//    public void login_UsernameNotFound_BadRequest() throws Exception {
//    }

    @Test
    public void getUserByUsername_ValidUsername_Success() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/user/" + user.getUsername())
                .accept(MediaType.APPLICATION_JSON);

        when(userService.getUser(any())).thenReturn(user);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(user)));
    }
}

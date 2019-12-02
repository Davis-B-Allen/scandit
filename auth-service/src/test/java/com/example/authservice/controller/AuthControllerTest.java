package com.example.authservice.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.authservice.config.JwtRequestFilter;
import com.example.authservice.config.SecurityConfig;
import com.example.authservice.model.User;
import com.example.authservice.model.UserRole;
import com.example.authservice.service.UserService;
import com.example.authservice.util.AuthenticationUtil;
import com.example.authservice.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AuthController.class, SecurityConfig.class, JwtRequestFilter.class})
@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    private static final String USERNAME = "user";
    private static final String EMAIL = "user@example.com";
    private static final String PASSWORD = "password";
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_EMAIL = "admin@example.com";
    private static final String ADMIN_PASSWORD = "password";

    @Autowired
    MockMvc mockMvc;

    @MockBean
    JwtUtil jwtUtil;

    @MockBean
    UserService userService;

    @MockBean
    AuthenticationUtil authenticationUtil;

    @MockBean
    Authentication authentication;

    private User user;
    private User adminUser;
    private UserRole userRoleUser;
    private UserRole userRoleAdmin;
    private ObjectMapper objectMapper;

    public AuthControllerTest() {
        user = new User();
        adminUser = new User();
        userRoleUser = new UserRole();
        userRoleAdmin = new UserRole();
        objectMapper = new ObjectMapper();
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
    public void auth_Signup_Success() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/signup");

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());
    }

    @Test
    public void auth_Login_Success() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/login");

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());
    }

    @Test
    public void auth_ListPosts_Success() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/post/list");

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());
    }

    @Test
    public void auth_ShowComment_Success() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/post/1/comment");

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username=USERNAME)
    public void auth_createPostValidAuth_Success() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/post");

        when(authenticationUtil.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(USERNAME);
        when(userService.getUser(USERNAME)).thenReturn(user);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(user)));
    }

    @Test
    public void auth_createPostNoAuthentication_Failure() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/post");
        mockMvc.perform(requestBuilder)
                .andExpect(status().isUnauthorized());
    }

}

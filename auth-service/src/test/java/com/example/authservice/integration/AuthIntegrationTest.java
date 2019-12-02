package com.example.authservice.integration;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.authservice.model.User;
import com.example.authservice.model.UserRole;
import com.example.authservice.repository.UserRepository;
import com.example.authservice.repository.UserRoleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@ActiveProfiles("qa,global")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthIntegrationTest {

    private static final String USERNAME = "user";
    private static final String EMAIL = "user@example.com";
    private static final String PASSWORD = "password";
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_EMAIL = "admin@example.com";
    private static final String ADMIN_PASSWORD = "password";

    @Value("${jwt.secret}")
    private String secret;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserRoleRepository userRoleRepository;

    private User user;
    private User adminUser;
    private UserRole userRoleUser;
    private UserRole userRoleAdmin;
    private ObjectMapper objectMapper;

    public AuthIntegrationTest() {
        user = new User();
        adminUser = new User();
        userRoleUser = new UserRole();
        userRoleAdmin = new UserRole();
        objectMapper = new ObjectMapper();

        userRoleUser.setName("ROLE_USER");
        userRoleAdmin.setName("ROLE_ADMIN");

        user.setUsername(USERNAME);
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        List<UserRole> roles = new ArrayList<>();
        roles.add(userRoleUser);
        user.setUserRoles(roles);

        adminUser.setUsername(ADMIN_USERNAME);
        adminUser.setEmail(ADMIN_EMAIL);
        adminUser.setPassword(ADMIN_PASSWORD);
        List<UserRole> adminRoles = new ArrayList<>();
        adminRoles.add(userRoleUser);
        adminRoles.add(userRoleAdmin);
        adminUser.setUserRoles(adminRoles);
    }

    private String generateToken(String subject, Integer issued, Integer expires) {
        return Jwts.builder().setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis() + issued))
                .setExpiration(new Date(System.currentTimeMillis() + expires))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    @Test
    @Transactional
    public void auth_CreatePostWithValidUser_Success() throws Exception {
        userRoleRepository.save(userRoleUser);
        userRepository.save(user);

        String token = generateToken(USERNAME, 0, 60000);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/post")
                .header("Authorization", "Bearer " + token);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void auth_CreatePostWithoutUser_Unauthorized() throws Exception {
        userRoleRepository.save(userRoleUser);
        userRepository.save(user);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/post");

        mockMvc.perform(requestBuilder)
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Transactional
    public void auth_CreatePostExpiredToken_Unauthorized() throws Exception {
        userRoleRepository.save(userRoleUser);
        userRepository.save(user);

        String token = generateToken(USERNAME, -2, -1);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/post")
                .header("Authorization", "Bearer " + token);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Transactional
    public void auth_CreatePostNoBearerString_Unauthorized() throws Exception {
        userRoleRepository.save(userRoleUser);
        userRepository.save(user);

        String token = generateToken(USERNAME, 0, 60000);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/post")
                .header("Authorization", token);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Transactional
    public void auth_CreateUserRole_Success() throws Exception {
        userRoleRepository.save(userRoleUser);
        userRoleRepository.save(userRoleAdmin);
        userRepository.save(adminUser);

        String token = generateToken(ADMIN_USERNAME, 0, 60000);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/user/role")
                .header("Authorization", "Bearer " + token);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void auth_CreateUserRoleWithoutHavingAdminPermissions_Forbidden() throws Exception {
        userRoleRepository.save(userRoleUser);
        userRepository.save(user);

        String token = generateToken(USERNAME, 0, 60000);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/user/role")
                .header("Authorization", "Bearer " + token);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isForbidden());
    }

}

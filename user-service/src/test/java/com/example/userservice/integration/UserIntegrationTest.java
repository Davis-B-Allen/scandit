package com.example.userservice.integration;

import static org.assertj.core.api.Assertions.*;

import com.example.userservice.controller.UserController;
import com.example.userservice.model.User;
import com.example.userservice.model.UserRole;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.repository.UserRoleRepository;
import com.example.userservice.responseobject.JwtResponse;
import com.example.userservice.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("qa,global")
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
public class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    UserRoleRepository userRoleRepository;

    @Autowired
    UserService userService;

    private static final String USERNAME = "user";
    private static final String EMAIL = "user@example.com";
    private static final String PASSWORD = "password";

    private static final String ROLE_NAME = "ROLE_USER";

    private User user;
    private ObjectMapper objectMapper;

    public UserIntegrationTest() {
        user = new User();
        objectMapper = new ObjectMapper();
    }

    @BeforeEach
    public void init() {
        UserRole userRole = new UserRole();
        userRole.setName(ROLE_NAME);
        if (userRoleRepository.findByName(userRole.getName()) == null) {
            userRoleRepository.save(userRole);
        }
        user.setUsername(USERNAME);
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
    }

    @Test
    @Transactional
    public void signup_ValidUser_Success() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user));


        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        String responseString = mvcResult.getResponse().getContentAsString();
        JwtResponse jwtResponse = objectMapper.readValue(responseString, JwtResponse.class);
        assertThat(jwtResponse.getUsername()).isEqualTo(user.getUsername());
    }

    @Test
    @Transactional
    public void login_ValidUser_Success() throws Exception {
        RequestBuilder signupRequestBuilder = MockMvcRequestBuilders
                .post("/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user));

        mockMvc.perform(signupRequestBuilder)
                .andExpect(status().isOk());

        RequestBuilder loginRequestBuilder = MockMvcRequestBuilders
                .post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user));

        MvcResult mvcResult = mockMvc.perform(loginRequestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        String responseString = mvcResult.getResponse().getContentAsString();
        JwtResponse jwtResponse = objectMapper.readValue(responseString, JwtResponse.class);
        assertThat(jwtResponse.getUsername()).isEqualTo(user.getUsername());
    }
}

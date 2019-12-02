package com.example.userservice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.userservice.config.SecurityConfig;
import com.example.userservice.model.UserRole;
import com.example.userservice.service.UserRoleService;
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
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {UserRoleController.class, SecurityConfig.class})
@WebMvcTest(UserRoleController.class)
public class UserRoleControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserRoleService userRoleService;

    private UserRole userRole;
    private ObjectMapper objectMapper;

    public UserRoleControllerTest() {
        userRole = new UserRole();
        objectMapper = new ObjectMapper();
    }

    @BeforeEach
    public void init() {
        userRole.setId(1);
        userRole.setName("ROLE_USER");
    }

    @Test
    public void listRoles_NoError_Success() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/role/list")
                .accept(MediaType.APPLICATION_JSON);

        List<UserRole> roles = new ArrayList<>();
        roles.add(userRole);
        when(userRoleService.listRoles()).thenReturn(roles);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(roles)));
    }

    @Test
    public void createUserRole_Role_Success() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/role")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRole));

        when(userRoleService.createUserRole(any())).thenReturn(userRole);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userRole)));
    }

    @Test
    public void deleteUserRoleById_RoleId_Success() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/role/" + userRole.getId());

        when(userRoleService.deleteUserRoleById(userRole.getId())).thenReturn(userRole.getId());

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().string(userRole.getId().toString()));
    }

}

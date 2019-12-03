package com.example.userservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.example.userservice.model.UserRole;
import com.example.userservice.repository.UserRoleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

public class UserRoleServiceTest {

    private UserRole userRole;
    private ObjectMapper objectMapper;

    @InjectMocks
    UserRoleServiceImpl userRoleService;

    @Mock
    UserRoleRepository userRoleRepository;

    public UserRoleServiceTest() {
        userRole = new UserRole();
        objectMapper = new ObjectMapper();
        MockitoAnnotations.initMocks(this);
    }

    @BeforeEach
    public void init() {
        userRole.setId(1);
        userRole.setName("ROLE_ADMIN");
    }

    @Test
    public void getUserRoleByName_ValidName_Success() {
        when(userRoleRepository.findByName(userRole.getName())).thenReturn(userRole);
        UserRole foundUserRole = userRoleService.getUserRoleByName(userRole.getName());
        assertThat(foundUserRole).isNotNull();
        assertThat(foundUserRole).isEqualToComparingFieldByField(userRole);
    }

    @Test
    public void listRoles_NoError_Success() {
        List<UserRole> roles = new ArrayList<>();
        roles.add(userRole);
        when(userRoleRepository.findAll()).thenReturn(roles);
        Iterable<UserRole> foundRoles = userRoleService.listRoles();
        assertThat(foundRoles).isNotNull();
        assertThat(foundRoles).contains(userRole);
    }

    @Test
    public void createUserRole_ValidUser_Success() {
        when(userRoleRepository.save(userRole)).thenReturn(userRole);
        UserRole savedUserRole = userRoleService.createUserRole(userRole);
        assertThat(savedUserRole).isNotNull();
        assertThat(savedUserRole).isEqualToComparingFieldByField(userRole);
    }

    @Test
    public void deleteUserRoleById_ValidId_Success() {
        userRoleService.deleteUserRoleById(userRole.getId());
    }

}

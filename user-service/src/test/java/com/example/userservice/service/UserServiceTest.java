package com.example.userservice.service;

import com.example.userservice.exception.LoginException;
import com.example.userservice.model.User;
import com.example.userservice.model.UserRole;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.responseobject.JwtResponse;
import com.example.userservice.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private UserRole userRole;
    private User user;
    private String encodedPassword;
    private String generatedToken;
    private ObjectMapper objectMapper;

    public UserServiceTest() {
        user = new User();
        userRole = new UserRole();
        encodedPassword = "asdjkhfjlejhalj3hflkajhf3khakjfhja";
        generatedToken = "alkjsdlfajlekjlj3lkajflkjaj.flajl3jkldjksfla.lasdjfljelfakje";
        objectMapper = new ObjectMapper();
        MockitoAnnotations.initMocks(this);
    }

    @BeforeEach
    public void init() {
        userRole.setId(1);
        userRole.setName("ROLE_ADMIN");

        user.setId(1L);
        user.setUsername("user");
        user.setEmail("user@example.com");
        user.setPassword("password");
        user.addUserRole(userRole);
    }

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    @Mock
    UserRoleService userRoleService;

    @Mock
    PasswordEncoder bCryptPasswordEncoder;

    @Mock
    JwtUtil jwtUtil;

    @Test
    public void signup_ValidUser_Success() throws Exception {
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn(encodedPassword);
        when(userRoleService.getUserRoleByName(anyString())).thenReturn(userRole);
        when(userRepository.save(any())).thenReturn(user);
        when(jwtUtil.generateToken(any())).thenReturn(generatedToken);
        when(userRepository.findByUsername(anyString())).thenReturn(user);
        when(userRepository.findByEmail(anyString())).thenReturn(user);

        JwtResponse jwtResponse = new JwtResponse(generatedToken, user.getUsername());
        JwtResponse returnedJwtResponse = userService.signup(user);

        assertThat(returnedJwtResponse).isNotNull();
        assertThat(returnedJwtResponse).isEqualToComparingFieldByField(jwtResponse);
        verify(jwtUtil, times(1)).generateToken(any());
    }

    @Test
    public void signup_UserRoleDoesntExist_Exception() {
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn(encodedPassword);
        when(userRoleService.getUserRoleByName(anyString())).thenReturn(null);

        Throwable thrown = catchThrowable(() -> userService.signup(user));

        assertThat(thrown).isInstanceOf(LoginException.class);
        verify(jwtUtil, never()).generateToken(any());
    }

    @Test
    public void signup_CouldntSaveUser_Exception() {
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn(encodedPassword);
        when(userRoleService.getUserRoleByName(anyString())).thenReturn(userRole);

        Throwable thrown = catchThrowable(() -> userService.signup(user));

        assertThat(thrown).isInstanceOf(LoginException.class);
        verify(jwtUtil, never()).generateToken(any());
    }

    @Test
    public void login_ValidUser_Success() throws Exception {
        when(userRepository.findByEmail(anyString())).thenReturn(user);
        when(bCryptPasswordEncoder.matches(any(), any())).thenReturn(true);
        when(jwtUtil.generateToken(any())).thenReturn(generatedToken);
        when(userRepository.findByUsername(anyString())).thenReturn(user);

        JwtResponse jwtResponse = new JwtResponse(generatedToken, user.getUsername());
        JwtResponse returnedJwtResponse = userService.login(user);

        assertThat(returnedJwtResponse).isNotNull();
        assertThat(returnedJwtResponse).isEqualToComparingFieldByField(jwtResponse);
        verify(jwtUtil, times(1)).generateToken(any());
    }

    @Test
    public void login_EmailNotFound_LoginException() throws Exception {
        when(userRepository.findByEmail(anyString())).thenReturn(null);

        Throwable thrown = catchThrowable(() -> userService.login(user));

        assertThat(thrown).isInstanceOf(LoginException.class);
        verify(jwtUtil, never()).generateToken(any());
    }

    @Test
    public void login_IncorrectPassword_LoginException() throws Exception {
        when(userRepository.findByEmail(anyString())).thenReturn(user);
        when(bCryptPasswordEncoder.matches(any(), any())).thenReturn(false);

        Throwable thrown = catchThrowable(() -> userService.login(user));

        assertThat(thrown).isInstanceOf(LoginException.class);
        verify(jwtUtil, never()).generateToken(any());
    }

}

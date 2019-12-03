package com.example.profileservice.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;

import com.example.profileservice.exception.ExceptionHandler;
import com.example.profileservice.exception.ProfileServiceException;
import com.example.profileservice.model.Profile;
import com.example.profileservice.responseobjects.ProfileResponse;
import com.example.profileservice.responseobjects.User;
import com.example.profileservice.service.ProfileService;
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

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {UserProfileController.class, ExceptionHandler.class})
@WebMvcTest(UserProfileController.class)
public class UserProfileControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ProfileService profileService;

    private Profile profile;
    private ObjectMapper objectMapper;
    private String username;

    public UserProfileControllerTest() {
        profile = new Profile();
        objectMapper = new ObjectMapper();
    }

    @BeforeEach
    public void init() {
        username = "user";
        profile.setId(1L);
        profile.setAdditionalEmail("hello@example.com");
        profile.setAddress("123 4th Street, New York, NY 56789");
        profile.setMobile("123-456-7890");
        profile.setUsername(username);
    }

    @Test
    public void createProfile_ValidProfile_Success() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .header("username", username)
                .content(objectMapper.writeValueAsString(profile));

        User user = new User(username);
        ProfileResponse profileResponse = new ProfileResponse(profile.getId(),
                profile.getAdditionalEmail(),
                profile.getMobile(),
                profile.getAddress(),
                user);
        when(profileService.createProfile(any(), anyString())).thenReturn(profileResponse);

        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(profileResponse)))
                .andReturn();
    }

    @Test
    public void getProfileByUsername_ValidUsername_Success() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/profile")
                .accept(MediaType.APPLICATION_JSON)
                .header("username", username);

        User user = new User(username);
        ProfileResponse profileResponse = new ProfileResponse(profile.getId(),
                profile.getAdditionalEmail(),
                profile.getMobile(),
                profile.getAddress(),
                user);
        when(profileService.getProfileByUsername(anyString())).thenReturn(profileResponse);

        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(profileResponse)))
                .andReturn();
    }

    @Test
    public void getProfileByUsername_ValidUsernameNoProfile_Exception() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/profile")
                .accept(MediaType.APPLICATION_JSON)
                .header("username", username);

        when(profileService.getProfileByUsername(anyString())).thenThrow(new ProfileServiceException("Couldn't find profile for user " + username));

//        Throwable thrown = catchThrowable(() -> mockMvc.perform(requestBuilder).andExpect(status().isInternalServerError()));
//        assertThat(thrown.getCause()).isInstanceOf(ProfileServiceException.class);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteProfileByUsername_ValidUsername_Success() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/profile/user");

        when(profileService.deleteProfileByUsername(anyString())).thenReturn("Profile successfully deleted");

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().string("Profile successfully deleted"));
    }

}


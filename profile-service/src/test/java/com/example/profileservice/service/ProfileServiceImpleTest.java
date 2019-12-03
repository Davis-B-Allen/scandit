package com.example.profileservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.example.profileservice.exception.ProfileServiceException;
import com.example.profileservice.model.Profile;
import com.example.profileservice.repository.ProfileRepository;
import com.example.profileservice.responseobjects.ProfileResponse;
import com.example.profileservice.responseobjects.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ProfileServiceImpleTest {

    private Profile profile;
    private String username;
    private ObjectMapper objectMapper;

    public ProfileServiceImpleTest() {
        profile = new Profile();
        username = "user";
        objectMapper = new ObjectMapper();
        MockitoAnnotations.initMocks(this);
    }

    @BeforeEach
    public void init() {
        profile.setId(1L);
        profile.setAdditionalEmail("hello@example.com");
        profile.setMobile("123-456-7890");
        profile.setAddress("123 4th St, New York, NY 56789");
        profile.setUsername(username);
    }

    @InjectMocks
    ProfileServiceImpl profileService;

    @Mock
    ProfileRepository profileRepository;

    @Test
    public void createProfile_ValidProfile_Success() throws ProfileServiceException {
        when(profileRepository.findProfileByUsername(anyString())).thenReturn(null);
        when(profileRepository.save(any())).thenReturn(profile);

        User user = new User(username);

        ProfileResponse profileResponse = new ProfileResponse(profile.getId(),
                profile.getAdditionalEmail(),
                profile.getMobile(),
                profile.getAddress(),
                user);

        ProfileResponse savedProfileResponse = profileService.createProfile(profile, username);

        assertThat(savedProfileResponse).isEqualToComparingFieldByFieldRecursively(profileResponse);
    }

    @Test
    public void createProfile_ValidProfileExistingUser_UpdateProfileSuccess() throws ProfileServiceException {
        when(profileRepository.findProfileByUsername(anyString())).thenReturn(profile);
        when(profileRepository.save(any())).thenReturn(profile);

        User user = new User(username);

        ProfileResponse profileResponse = new ProfileResponse(profile.getId(),
                profile.getAdditionalEmail(),
                profile.getMobile(),
                profile.getAddress(),
                user);

        ProfileResponse savedProfileResponse = profileService.createProfile(profile, username);

        assertThat(savedProfileResponse).isEqualToComparingFieldByFieldRecursively(profileResponse);
    }

    @Test
    public void createProfile_DatabaseError_Exception() throws ProfileServiceException {
        when(profileRepository.findProfileByUsername(anyString())).thenReturn(profile);
        when(profileRepository.save(any())).thenReturn(null);

        Throwable thrown = catchThrowable(() -> profileService.createProfile(profile, username));

        assertThat(thrown).isInstanceOf(ProfileServiceException.class);
    }

    @Test
    public void getProfileByUsername_UserHasProfile_Success() throws ProfileServiceException {
        when(profileRepository.findProfileByUsername(anyString())).thenReturn(profile);
        User user = new User(username);

        ProfileResponse profileResponse = new ProfileResponse(profile.getId(),
                profile.getAdditionalEmail(),
                profile.getMobile(),
                profile.getAddress(),
                user);

        ProfileResponse foundProfileResponse = profileService.getProfileByUsername(username);

        assertThat(foundProfileResponse).isEqualToComparingFieldByFieldRecursively(profileResponse);
    }

    @Test
    public void getProfileByUsername_UserHasNoProfile_Exception() throws ProfileServiceException {
        when(profileRepository.findProfileByUsername(anyString())).thenReturn(null);

        Throwable thrown = catchThrowable(() -> profileService.getProfileByUsername(username));

        assertThat(thrown).isInstanceOf(ProfileServiceException.class);
    }

    @Test
    public void deleteProfileByUsername_UserHasProfile_Success() throws ProfileServiceException {
        when(profileRepository.findProfileByUsername(anyString())).thenReturn(profile);

        String res = profileService.deleteProfileByUsername(username);

        assertThat(res).isEqualTo("Profile successfully deleted");
    }

    @Test
    public void deleteProfileByUsername_UserHasNoProfile_Exception() throws ProfileServiceException {
        when(profileRepository.findProfileByUsername(anyString())).thenReturn(null);

        Throwable thrown = catchThrowable(() -> profileService.deleteProfileByUsername(username));

        assertThat(thrown).isInstanceOf(ProfileServiceException.class);
    }

}

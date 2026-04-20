package com.skillmatch.profile.service;

import com.skillmatch.profile.dto.auth.AuthenticatedUser;
import com.skillmatch.profile.dto.request.CreateProfileRequest;
import com.skillmatch.profile.dto.request.UpdateProfileRequest;
import com.skillmatch.profile.dto.response.ProfileResponse;
import com.skillmatch.profile.entity.Profile;
import com.skillmatch.profile.exception.DuplicateResourceException;
import com.skillmatch.profile.exception.ResourceNotFoundException;
import com.skillmatch.profile.mapper.ProfileMapper;
import com.skillmatch.profile.repository.ProfileRepository;
import com.skillmatch.profile.security.CurrentUserService;
import com.skillmatch.profile.support.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

    @Mock
    private ProfileRepository profileRepository;
    @Mock
    private CurrentUserService currentUserService;
    @Mock
    private ProfileMapper profileMapper;

    @InjectMocks
    private ProfileService profileService;

    private AuthenticatedUser authenticatedUser;
    private Profile profile;

    @BeforeEach
    void setUp() {
        authenticatedUser = new AuthenticatedUser(1L, "test@example.com", "JOB_SEEKER");
        profile = TestDataFactory.profile(10L, 1L);
    }

    @Test
    void createProfile_shouldCreateProfile_whenUserHasNoProfile() {
        CreateProfileRequest request = TestDataFactory.createProfileRequest();
        ProfileResponse expected = ProfileResponse.builder().id(10L).userId(1L).build();

        when(currentUserService.getCurrentUser()).thenReturn(authenticatedUser);
        when(profileRepository.existsByUserId(1L)).thenReturn(false);
        when(profileRepository.save(any(Profile.class))).thenReturn(profile);
        when(profileMapper.toProfileResponse(profile)).thenReturn(expected);

        ProfileResponse result = profileService.createProfile(request);

        assertThat(result).isEqualTo(expected);
        verify(profileRepository).save(any(Profile.class));
    }

    @Test
    void createProfile_shouldThrow_whenProfileAlreadyExists() {
        CreateProfileRequest request = TestDataFactory.createProfileRequest();

        when(currentUserService.getCurrentUser()).thenReturn(authenticatedUser);
        when(profileRepository.existsByUserId(1L)).thenReturn(true);

        assertThatThrownBy(() -> profileService.createProfile(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Profile already exists for this user");

        verify(profileRepository, never()).save(any());
    }

    @Test
    void getMyProfile_shouldReturnProfile_whenExists() {
        ProfileResponse expected = ProfileResponse.builder().id(10L).userId(1L).build();

        when(currentUserService.getCurrentUser()).thenReturn(authenticatedUser);
        when(profileRepository.findByUserId(1L)).thenReturn(Optional.of(profile));
        when(profileMapper.toProfileResponse(profile)).thenReturn(expected);

        ProfileResponse result = profileService.getMyProfile();

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void getMyProfile_shouldThrow_whenProfileNotFound() {
        when(currentUserService.getCurrentUser()).thenReturn(authenticatedUser);
        when(profileRepository.findByUserId(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> profileService.getMyProfile())
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Profile not found");
    }

    @Test
    void updateProfile_shouldUpdateFields_whenProfileExists() {
        UpdateProfileRequest request = TestDataFactory.updateProfileRequest();
        ProfileResponse expected = ProfileResponse.builder().id(10L).userId(1L).headline("Updated Headline").build();

        when(currentUserService.getCurrentUser()).thenReturn(authenticatedUser);
        when(profileRepository.findByUserId(1L)).thenReturn(Optional.of(profile));
        when(profileRepository.save(profile)).thenReturn(profile);
        when(profileMapper.toProfileResponse(profile)).thenReturn(expected);

        ProfileResponse result = profileService.updateProfile(request);

        assertThat(result).isEqualTo(expected);
        assertThat(profile.getFirstName()).isEqualTo("Updated");
        assertThat(profile.getLocation()).isEqualTo("Toronto");
    }

    @Test
    void updateProfile_shouldNormalizeBlankOptionalFieldsToNull() {
        UpdateProfileRequest request = new UpdateProfileRequest();
        request.setFirstName("   ");
        request.setLastName(null);
        request.setHeadline("  ");
        request.setSummary("  ");
        request.setLocation("  ");

        when(currentUserService.getCurrentUser()).thenReturn(authenticatedUser);
        when(profileRepository.findByUserId(1L)).thenReturn(Optional.of(profile));
        when(profileRepository.save(profile)).thenReturn(profile);
        when(profileMapper.toProfileResponse(profile)).thenReturn(ProfileResponse.builder().build());

        profileService.updateProfile(request);

        assertThat(profile.getFirstName()).isNull();
        assertThat(profile.getHeadline()).isNull();
        assertThat(profile.getSummary()).isNull();
        assertThat(profile.getLocation()).isNull();
    }

    @Test
    void getCurrentUserProfile_shouldReturnEntity_whenExists() {
        when(currentUserService.getCurrentUser()).thenReturn(authenticatedUser);
        when(profileRepository.findByUserId(1L)).thenReturn(Optional.of(profile));

        Profile result = profileService.getCurrentUserProfile();

        assertThat(result).isEqualTo(profile);
    }

    @Test
    void getCurrentUserProfile_shouldThrow_whenMissing() {
        when(currentUserService.getCurrentUser()).thenReturn(authenticatedUser);
        when(profileRepository.findByUserId(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> profileService.getCurrentUserProfile())
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Profile not found");
    }
}
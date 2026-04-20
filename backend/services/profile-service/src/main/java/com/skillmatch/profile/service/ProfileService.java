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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final CurrentUserService currentUserService;
    private final ProfileMapper profileMapper;

    @Transactional
    public ProfileResponse createProfile(CreateProfileRequest request) {
        AuthenticatedUser currentUser = currentUserService.getCurrentUser();

        if (profileRepository.existsByUserId(currentUser.userId())) {
            throw new DuplicateResourceException("Profile already exists for this user");
        }

        Profile profile = new Profile();
        profile.setUserId(currentUser.userId());
        profile.setFirstName(normalize(request.getFirstName()));
        profile.setLastName(normalize(request.getLastName()));
        profile.setHeadline(normalize(request.getHeadline()));
        profile.setSummary(normalize(request.getSummary()));
        profile.setLocation(normalize(request.getLocation()));

        Profile saved = profileRepository.save(profile);
        return profileMapper.toProfileResponse(saved);
    }

    @Transactional(readOnly = true)
    public ProfileResponse getMyProfile() {
        return profileMapper.toProfileResponse(getCurrentUserProfile());
    }

    @Transactional
    public ProfileResponse updateProfile(UpdateProfileRequest request) {
        Profile profile = getCurrentUserProfile();

        profile.setFirstName(normalize(request.getFirstName()));
        profile.setLastName(normalize(request.getLastName()));
        profile.setHeadline(normalize(request.getHeadline()));
        profile.setSummary(normalize(request.getSummary()));
        profile.setLocation(normalize(request.getLocation()));

        return profileMapper.toProfileResponse(profileRepository.save(profile));
    }

    @Transactional(readOnly = true)
    public Profile getCurrentUserProfile() {
        Long userId = currentUserService.getCurrentUser().userId();

        return profileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));
    }

    private String normalize(String value) {
        if (value == null) return null;
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
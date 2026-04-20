package com.skillmatch.profile.controller;

import com.skillmatch.profile.dto.request.CreateProfileRequest;
import com.skillmatch.profile.dto.request.UpdateProfileRequest;
import com.skillmatch.profile.dto.response.ProfileResponse;
import com.skillmatch.profile.service.ProfileService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profiles/me")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProfileResponse createProfile(@Valid @RequestBody CreateProfileRequest request) {
        return profileService.createProfile(request);
    }

    @GetMapping
    public ProfileResponse getMyProfile() {
        return profileService.getMyProfile();
    }

    @PutMapping
    public ProfileResponse updateProfile(@Valid @RequestBody UpdateProfileRequest request) {
        return profileService.updateProfile(request);
    }
}
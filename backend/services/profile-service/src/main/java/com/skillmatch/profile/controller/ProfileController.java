package com.skillmatch.profile.controller;

import com.skillmatch.profile.dto.request.CreateProfileRequest;
import com.skillmatch.profile.dto.request.UpdateProfileRequest;
import com.skillmatch.profile.dto.response.ProfileResponse;
import com.skillmatch.profile.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Create profile", description = "Creates a profile for the current authenticated user")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Profile created successfully"),
            @ApiResponse(responseCode = "409", description = "Profile already exists"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProfileResponse createProfile(@Valid @RequestBody CreateProfileRequest request) {
        return profileService.createProfile(request);
    }

    @Operation(summary = "Get profile", description = "Retrieves the current user's profile")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Profile not found")
    })
    @GetMapping
    public ProfileResponse getMyProfile() {
        return profileService.getMyProfile();
    }

    @Operation(summary = "Update profile", description = "Updates the current user's profile")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile updated successfully"),
            @ApiResponse(responseCode = "404", description = "Profile not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PutMapping
    public ProfileResponse updateProfile(@Valid @RequestBody UpdateProfileRequest request) {
        return profileService.updateProfile(request);
    }
}
package com.skillmatch.profile.controller;

import com.skillmatch.profile.dto.response.ProfileResponse;
import com.skillmatch.profile.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profiles/internal")
@RequiredArgsConstructor
public class InternalProfileController {

    private final ProfileService profileService;

    @GetMapping("/user/{userId}")
    public ProfileResponse getProfileByUserId(@PathVariable Long userId) {
        return profileService.getProfileByUserId(userId);
    }
}
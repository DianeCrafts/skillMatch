package com.skillmatch.profile.controller;

import com.skillmatch.profile.dto.request.AddWorkExperienceRequest;
import com.skillmatch.profile.dto.request.UpdateWorkExperienceRequest;
import com.skillmatch.profile.dto.response.WorkExperienceResponse;
import com.skillmatch.profile.service.WorkExperienceService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profiles/me/experiences")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class WorkExperienceController {

    private final WorkExperienceService workExperienceService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WorkExperienceResponse addExperience(@Valid @RequestBody AddWorkExperienceRequest request) {
        return workExperienceService.addExperience(request);
    }

    @PutMapping("/{experienceId}")
    public WorkExperienceResponse updateExperience(@PathVariable Long experienceId,
                                                   @Valid @RequestBody UpdateWorkExperienceRequest request) {
        return workExperienceService.updateExperience(experienceId, request);
    }

    @DeleteMapping("/{experienceId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteExperience(@PathVariable Long experienceId) {
        workExperienceService.deleteExperience(experienceId);
    }
}
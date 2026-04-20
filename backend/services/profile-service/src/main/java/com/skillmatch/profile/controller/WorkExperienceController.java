package com.skillmatch.profile.controller;

import com.skillmatch.profile.dto.request.AddWorkExperienceRequest;
import com.skillmatch.profile.dto.request.UpdateWorkExperienceRequest;
import com.skillmatch.profile.dto.response.WorkExperienceResponse;
import com.skillmatch.profile.service.WorkExperienceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Add work experience", description = "Adds a new work experience entry to the profile")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Work experience created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WorkExperienceResponse addExperience(@Valid @RequestBody AddWorkExperienceRequest request) {
        return workExperienceService.addExperience(request);
    }

    @Operation(summary = "Update work experience", description = "Updates an existing work experience entry")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Work experience updated successfully"),
            @ApiResponse(responseCode = "404", description = "Work experience not found")
    })
    @PutMapping("/{experienceId}")
    public WorkExperienceResponse updateExperience(@PathVariable Long experienceId,
                                                   @Valid @RequestBody UpdateWorkExperienceRequest request) {
        return workExperienceService.updateExperience(experienceId, request);
    }

    @Operation(summary = "Delete work experience", description = "Deletes a work experience entry")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Work experience deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Work experience not found")
    })
    @DeleteMapping("/{experienceId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteExperience(@PathVariable Long experienceId) {
        workExperienceService.deleteExperience(experienceId);
    }
}
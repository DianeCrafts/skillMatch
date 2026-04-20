package com.skillmatch.profile.controller;

import com.skillmatch.profile.dto.request.AddEducationRequest;
import com.skillmatch.profile.dto.request.UpdateEducationRequest;
import com.skillmatch.profile.dto.response.EducationResponse;
import com.skillmatch.profile.service.EducationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profiles/me/education")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class EducationController {

    private final EducationService educationService;

    @Operation(summary = "Add education", description = "Adds a new education entry to the current user's profile")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Education created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EducationResponse addEducation(@Valid @RequestBody AddEducationRequest request) {
        return educationService.addEducation(request);
    }

    @Operation(summary = "Update education", description = "Updates an existing education entry for the current user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Education updated successfully"),
            @ApiResponse(responseCode = "404", description = "Education not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PutMapping("/{educationId}")
    public EducationResponse updateEducation(@PathVariable Long educationId,
                                             @Valid @RequestBody UpdateEducationRequest request) {
        return educationService.updateEducation(educationId, request);
    }

    @Operation(summary = "Delete education", description = "Deletes an education entry from the current user's profile")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Education deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Education not found")
    })
    @DeleteMapping("/{educationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEducation(@PathVariable Long educationId) {
        educationService.deleteEducation(educationId);
    }
}
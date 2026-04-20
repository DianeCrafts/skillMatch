package com.skillmatch.profile.controller;

import com.skillmatch.profile.dto.request.AddEducationRequest;
import com.skillmatch.profile.dto.request.UpdateEducationRequest;
import com.skillmatch.profile.dto.response.EducationResponse;
import com.skillmatch.profile.service.EducationService;
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EducationResponse addEducation(@Valid @RequestBody AddEducationRequest request) {
        return educationService.addEducation(request);
    }

    @PutMapping("/{educationId}")
    public EducationResponse updateEducation(@PathVariable Long educationId,
                                             @Valid @RequestBody UpdateEducationRequest request) {
        return educationService.updateEducation(educationId, request);
    }

    @DeleteMapping("/{educationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEducation(@PathVariable Long educationId) {
        educationService.deleteEducation(educationId);
    }
}
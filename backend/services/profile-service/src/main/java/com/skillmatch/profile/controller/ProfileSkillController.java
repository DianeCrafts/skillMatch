package com.skillmatch.profile.controller;

import com.skillmatch.profile.dto.request.AddSkillRequest;
import com.skillmatch.profile.dto.request.UpdateSkillRequest;
import com.skillmatch.profile.dto.response.SkillResponse;
import com.skillmatch.profile.service.ProfileSkillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profiles/me/skills")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ProfileSkillController {

    private final ProfileSkillService profileSkillService;

    @Operation(summary = "Add skill", description = "Adds a skill to the current user's profile")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Skill added successfully"),
            @ApiResponse(responseCode = "409", description = "Skill already exists"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SkillResponse addSkill(@Valid @RequestBody AddSkillRequest request) {
        return profileSkillService.addSkill(request);
    }

    @Operation(summary = "Update skill", description = "Updates an existing skill")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Skill updated successfully"),
            @ApiResponse(responseCode = "404", description = "Skill not found")
    })
    @PutMapping("/{skillId}")
    public SkillResponse updateSkill(@PathVariable Long skillId,
                                     @Valid @RequestBody UpdateSkillRequest request) {
        return profileSkillService.updateSkill(skillId, request);
    }

    @Operation(summary = "Delete skill", description = "Deletes a skill from the profile")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Skill deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Skill not found")
    })
    @DeleteMapping("/{skillId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSkill(@PathVariable Long skillId) {
        profileSkillService.deleteSkill(skillId);
    }
}
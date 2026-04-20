package com.skillmatch.profile.controller;

import com.skillmatch.profile.dto.request.AddSkillRequest;
import com.skillmatch.profile.dto.request.UpdateSkillRequest;
import com.skillmatch.profile.dto.response.SkillResponse;
import com.skillmatch.profile.service.ProfileSkillService;
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SkillResponse addSkill(@Valid @RequestBody AddSkillRequest request) {
        return profileSkillService.addSkill(request);
    }

    @PutMapping("/{skillId}")
    public SkillResponse updateSkill(@PathVariable Long skillId,
                                     @Valid @RequestBody UpdateSkillRequest request) {
        return profileSkillService.updateSkill(skillId, request);
    }

    @DeleteMapping("/{skillId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSkill(@PathVariable Long skillId) {
        profileSkillService.deleteSkill(skillId);
    }
}
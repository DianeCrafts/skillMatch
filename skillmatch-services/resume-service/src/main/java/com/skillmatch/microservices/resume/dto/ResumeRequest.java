package com.skillmatch.microservices.resume.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record ResumeRequest(
        @NotBlank String title,
        String summary,
        List<@Valid EducationRequest> education,
        List<@Valid ExperienceRequest> experience,
        List<@Valid SkillRequest> skills
) {}

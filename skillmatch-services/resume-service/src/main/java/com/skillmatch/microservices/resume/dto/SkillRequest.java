package com.skillmatch.microservices.resume.dto;

import jakarta.validation.constraints.NotBlank;

public record SkillRequest(
        @NotBlank String name
) {}

package com.skillmatch.microservices.resume.dto.ai;

import java.util.List;

public record ParsedResumeDTO(
        String summary,
        String name,
        String email,
        String phone,
        List<ParsedEducationDTO> education,
        List<ParsedExperienceDTO> experience,
        List<String> skills
) {}

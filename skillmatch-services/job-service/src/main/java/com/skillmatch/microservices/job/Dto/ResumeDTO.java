package com.skillmatch.microservices.job.Dto;

import java.util.List;

public record ResumeDTO(
        Long id,
        String summary,
        String name,
        String email,
        String phone,
        List<EducationDTO> education,
        List<ExperienceDTO> experience,
        List<String> skills
) {}

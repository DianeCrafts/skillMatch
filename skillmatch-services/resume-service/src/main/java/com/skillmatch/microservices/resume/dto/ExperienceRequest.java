package com.skillmatch.microservices.resume.dto;

import jakarta.validation.constraints.NotBlank;
import java.sql.Date;

public record ExperienceRequest(
        @NotBlank String company,
        @NotBlank String position,
        Date startDate,
        Date endDate,
        String description
) {}
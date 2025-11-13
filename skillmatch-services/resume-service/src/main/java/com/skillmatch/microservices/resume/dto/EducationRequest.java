package com.skillmatch.microservices.resume.dto;

import jakarta.validation.constraints.NotBlank;
import java.sql.Date;

public record EducationRequest(
        @NotBlank String institution,
        @NotBlank String degree,
        String field,
        Date startDate,
        Date endDate
) {}

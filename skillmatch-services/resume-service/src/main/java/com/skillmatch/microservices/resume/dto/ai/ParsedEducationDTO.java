package com.skillmatch.microservices.resume.dto.ai;

public record ParsedEducationDTO(
        String institution,
        String degree,
        String field,
        String startDate,  // match entity
        String endDate     // match entity
) {}

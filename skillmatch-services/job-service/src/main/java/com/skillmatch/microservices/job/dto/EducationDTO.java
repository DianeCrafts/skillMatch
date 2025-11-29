package com.skillmatch.microservices.job.dto;

public record EducationDTO(
        String institution,
        String degree,
        String field,
        String startDate,  // match entity
        String endDate     // match entity
) {}

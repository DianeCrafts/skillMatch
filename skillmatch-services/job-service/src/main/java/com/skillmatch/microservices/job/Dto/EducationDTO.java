package com.skillmatch.microservices.job.Dto;

public record EducationDTO(
        String institution,
        String degree,
        String field,
        String startDate,  // match entity
        String endDate     // match entity
) {}

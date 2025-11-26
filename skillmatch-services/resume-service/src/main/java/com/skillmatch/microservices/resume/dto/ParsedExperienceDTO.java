package com.skillmatch.microservices.resume.dto;

public record ParsedExperienceDTO(
        String company,
        String position,
        String startDate,   // match entity
        String endDate,     // match entity
        String description
) {}

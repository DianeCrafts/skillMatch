package com.skillmatch.microservices.job.dto;


public record ExperienceDTO(
        String company,
        String position,
        String startDate,   // match entity
        String endDate,     // match entity
        String description
) {}

package com.skillmatch.microservices.job.Dto;


public record ExperienceDTO(
        String company,
        String position,
        String startDate,   // match entity
        String endDate,     // match entity
        String description
) {}

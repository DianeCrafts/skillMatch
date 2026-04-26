package com.skillmatch.recommendation.dto.response;

import java.util.List;
import java.util.Set;

public record JobDataResponse(
        Long id,
        Long recruiterId,
        String status,
        String title,
        String description,
        String location,
        Set<String> requiredSkills
) {}
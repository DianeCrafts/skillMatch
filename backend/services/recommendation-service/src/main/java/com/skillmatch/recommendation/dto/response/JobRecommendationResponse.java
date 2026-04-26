package com.skillmatch.recommendation.dto.response;

public record JobRecommendationResponse(
        Long jobId,
        String title,
        String companyName,
        String location,
        Double similarityScore
) {}
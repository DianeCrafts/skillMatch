package com.skillmatch.microservices.job.dto;

public record ApplicationResponse(
        Long applicationId,
        Long userId,
        String userName,
        Long resumeId,
        double matchScore,
        String status
) {}

package com.skillmatch.recommendation.security;
public record CurrentUser(
        Long userId,
        String role
) {}
package com.skillmatch.profile.dto.auth;

public record AuthenticatedUser(
        Long userId,
        String email,
        String role
) {
}
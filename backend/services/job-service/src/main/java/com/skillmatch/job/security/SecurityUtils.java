package com.skillmatch.job.security;

import com.skillmatch.job.exception.UnauthorizedException;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtils {

    private static JwtService jwtService;

    private SecurityUtils() {
    }

    public static void setJwtService(JwtService service) {
        jwtService = service;
    }

    public static Long getCurrentUserId() {
        Claims claims = getCurrentClaims();

        Object userId = claims.get("userId");
        if (userId == null) {
            throw new UnauthorizedException("Missing userId in token");
        }

        return Long.valueOf(userId.toString());
    }

    public static String getCurrentUserRole() {
        Claims claims = getCurrentClaims();

        Object role = claims.get("role");
        if (role == null) {
            throw new UnauthorizedException("Missing role in token");
        }

        return role.toString();
    }

    private static Claims getCurrentClaims() {
        String token = getTokenFromSecurityContext();

        if (jwtService == null) {
            throw new IllegalStateException("JwtService not initialized in SecurityUtils");
        }

        return jwtService.extractAllClaims(token);
    }

    private static String getTokenFromSecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getCredentials() == null) {
            throw new UnauthorizedException("Unauthorized");
        }

        String token = authentication.getCredentials().toString();

        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        return token;
    }
}
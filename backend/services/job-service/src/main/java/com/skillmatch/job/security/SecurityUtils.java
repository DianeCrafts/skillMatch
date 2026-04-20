package com.skillmatch.job.security;

import com.skillmatch.job.exception.UnauthorizedException;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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
        String token = extractTokenFromRequest();

        if (jwtService == null) {
            throw new IllegalStateException("JwtService not initialized");
        }

        return jwtService.extractAllClaims(token);
    }

    private static String extractTokenFromRequest() {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes == null) {
            throw new UnauthorizedException("No request context available");
        }

        HttpServletRequest request = attributes.getRequest();
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("Missing or invalid Authorization header");
        }

        return authHeader.substring(7);
    }
}
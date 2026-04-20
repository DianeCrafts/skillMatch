package com.skillmatch.file.security;

import io.jsonwebtoken.Claims;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public final class SecurityUtils {

    private SecurityUtils() {
    }

    public static Long getCurrentUserId() {
        Claims claims = getClaims();
        Object userId = claims.get("userId");

        if (userId == null) {
            throw new IllegalStateException("No userId found in JWT claims");
        }

        return Long.valueOf(userId.toString());
    }

    public static String getCurrentUserRole() {
        Claims claims = getClaims();
        Object role = claims.get("role");

        if (role == null) {
            throw new IllegalStateException("No role found in JWT claims");
        }

        return role.toString();
    }

    public static String getCurrentToken() {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes == null) {
            throw new IllegalStateException("No current request context");
        }

        String authHeader = attributes.getRequest().getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalStateException("Missing or invalid Authorization header");
        }

        return authHeader.substring(7);
    }

    private static Claims getClaims() {
        Object authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof UsernamePasswordAuthenticationToken token)) {
            throw new IllegalStateException("No authenticated user");
        }

        Object details = token.getDetails();
        if (!(details instanceof Claims claims)) {
            throw new IllegalStateException("No JWT claims available");
        }

        return claims;
    }
}
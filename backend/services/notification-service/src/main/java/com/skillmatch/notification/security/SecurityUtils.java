package com.skillmatch.notification.security;

import com.skillmatch.notification.exception.UnauthorizedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtils {

    private SecurityUtils() {
    }

    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            throw new UnauthorizedException("User is not authenticated");
        }

        try {
            return Long.valueOf(authentication.getName());
        } catch (NumberFormatException ex) {
            throw new UnauthorizedException("Invalid authenticated user");
        }
    }

    public static String getCurrentUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getAuthorities() == null || authentication.getAuthorities().isEmpty()) {
            throw new UnauthorizedException("User role is not available");
        }

        return authentication.getAuthorities().iterator().next().getAuthority();
    }
}
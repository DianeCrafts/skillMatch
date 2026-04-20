package com.skillmatch.application.security;

import com.skillmatch.application.exception.UnauthorizedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtils {

    private SecurityUtils() {
    }

    public static Long getCurrentUserId() {
        return getPrincipal().getUserId();
    }

    public static String getCurrentUserRole() {
        return getPrincipal().getRole();
    }

    public static String getCurrentUserEmail() {
        return getPrincipal().getEmail();
    }

    private static CustomUserPrincipal getPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserPrincipal principal)) {
            throw new UnauthorizedException("User is not authenticated");
        }

        return principal;
    }

    public static String getCurrentToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getCredentials() == null) {
            throw new UnauthorizedException("User is not authenticated");
        }

        return authentication.getCredentials().toString();
    }
}
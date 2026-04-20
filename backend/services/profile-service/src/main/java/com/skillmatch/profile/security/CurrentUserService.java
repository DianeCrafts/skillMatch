package com.skillmatch.profile.security;

import com.skillmatch.profile.dto.auth.AuthenticatedUser;
import com.skillmatch.profile.exception.ForbiddenOperationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {

    public AuthenticatedUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof JwtAuthenticationToken jwtAuthenticationToken)) {
            throw new ForbiddenOperationException("Invalid authentication context");
        }

        Jwt jwt = jwtAuthenticationToken.getToken();

        Long userId = extractLongClaim(jwt, "userId");
        String email = jwt.getClaimAsString("email");
        String role = jwt.getClaimAsString("role");

        return new AuthenticatedUser(userId, email, role);
    }

    private Long extractLongClaim(Jwt jwt, String claimName) {
        Object claim = jwt.getClaim(claimName);
        if (claim == null) {
            throw new ForbiddenOperationException("Missing JWT claim: " + claimName);
        }

        if (claim instanceof Integer i) {
            return i.longValue();
        }
        if (claim instanceof Long l) {
            return l;
        }
        if (claim instanceof String s) {
            return Long.parseLong(s);
        }

        throw new ForbiddenOperationException("Invalid JWT claim type for: " + claimName);
    }
}
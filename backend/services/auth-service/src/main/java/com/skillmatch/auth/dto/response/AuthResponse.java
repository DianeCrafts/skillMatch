package com.skillmatch.auth.dto.response;

import com.skillmatch.auth.enums.UserRole;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {
    private String token;
    private String tokenType;
    private Long userId;
    private String email;
    private UserRole role;
    private String firstName;
    private String lastName;
}
package com.skillmatch.microservices.user.dto;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String role;
    private Long userId;   // NEW
    private String name;   // NEW
    private String email;  // NEW (optional)
}

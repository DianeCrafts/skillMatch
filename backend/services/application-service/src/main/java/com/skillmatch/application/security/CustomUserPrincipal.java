package com.skillmatch.application.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomUserPrincipal {
    private Long userId;
    private String role;
    private String email;
}
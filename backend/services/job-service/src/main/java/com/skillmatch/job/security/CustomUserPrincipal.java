package com.skillmatch.job.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomUserPrincipal {
    private Long userId;
    private String email;
    private String role;
}
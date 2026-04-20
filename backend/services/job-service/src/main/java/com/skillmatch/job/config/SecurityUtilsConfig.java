package com.skillmatch.job.config;

import com.skillmatch.job.security.JwtService;
import com.skillmatch.job.security.SecurityUtils;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityUtilsConfig {

    private final JwtService jwtService;

    @PostConstruct
    public void init() {
        SecurityUtils.setJwtService(jwtService);
    }
}
package com.skillmatch.auth.service;

import com.skillmatch.auth.dto.request.LoginRequest;
import com.skillmatch.auth.dto.request.RegisterRequest;
import com.skillmatch.auth.dto.response.AuthResponse;
import com.skillmatch.auth.dto.response.CurrentUserResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    CurrentUserResponse getCurrentUser();
}
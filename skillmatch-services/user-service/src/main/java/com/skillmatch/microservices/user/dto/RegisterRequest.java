package com.skillmatch.microservices.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import com.skillmatch.microservices.user.model.Role;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank @Size(max = 100)
    private String name;

    @NotBlank @Email @Size(max = 150)
    private String email;

    @NotBlank @Size(min = 6, max = 255)
    private String password;

    @NotNull
    private Role role;   // or make optional and default to USER in service
}
package com.skillmatch.microservices.user.dto;


import com.skillmatch.microservices.user.model.Role;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UpdateUserRequest {
    @NotBlank @Size(max = 100)
    private String name;

    @NotBlank @Email @Size(max = 150)
    private String email;

    @NotNull
    private Role role;
}
package com.skillmatch.microservices.user.mapper;

import com.skillmatch.microservices.user.dto.UserResponse;
import com.skillmatch.microservices.user.model.User;

public class UserMapper {

    // Convert User entity -> UserResponse DTO
    public static UserResponse toResponse(User user) {
        if (user == null) {
            return null;
        }

        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}

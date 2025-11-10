
package com.skillmatch.microservices.user.dto;

import com.skillmatch.microservices.user.model.Role;
import lombok.*;
import java.time.OffsetDateTime;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private Role role;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}

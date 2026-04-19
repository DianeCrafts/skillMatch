package com.skillmatch.auth.dto.response;

import com.skillmatch.auth.enums.UserRole;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CurrentUserResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private UserRole role;
}
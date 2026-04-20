package com.skillmatch.profile.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddSkillRequest {

    @NotBlank
    @Size(max = 100)
    private String name;
}
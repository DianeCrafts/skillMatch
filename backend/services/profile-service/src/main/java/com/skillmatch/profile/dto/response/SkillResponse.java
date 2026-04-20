package com.skillmatch.profile.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SkillResponse {
    private Long id;
    private String name;
}
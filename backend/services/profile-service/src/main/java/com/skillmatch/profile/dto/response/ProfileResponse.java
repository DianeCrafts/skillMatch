package com.skillmatch.profile.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.List;

@Getter
@Builder
public class ProfileResponse {
    private Long id;
    private Long userId;
    private String firstName;
    private String lastName;
    private String headline;
    private String summary;
    private String location;
    private List<SkillResponse> skills;
    private List<WorkExperienceResponse> experiences;
    private List<EducationResponse> education;
    private Instant createdAt;
    private Instant updatedAt;
}
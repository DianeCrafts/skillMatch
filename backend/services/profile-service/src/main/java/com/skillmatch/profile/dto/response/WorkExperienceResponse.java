package com.skillmatch.profile.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Builder
public class WorkExperienceResponse {
    private Long id;
    private String jobTitle;
    private String companyName;
    private String location;
    private String employmentType;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean currentlyWorking;
    private String description;
    private Integer sortOrder;
    private Instant createdAt;
    private Instant updatedAt;
}
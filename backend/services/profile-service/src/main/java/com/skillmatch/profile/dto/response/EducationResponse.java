package com.skillmatch.profile.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Builder
public class EducationResponse {
    private Long id;
    private String schoolName;
    private String degree;
    private String fieldOfStudy;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
    private Integer sortOrder;
    private Instant createdAt;
    private Instant updatedAt;
}
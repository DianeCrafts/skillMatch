package com.skillmatch.job.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Builder
public class InternalJobSummaryResponse {
    private Long id;
    private Long recruiterId;
    private String status;
    private String title;
    private String description;
    private String location;
    private Set<String> requiredSkills;
}
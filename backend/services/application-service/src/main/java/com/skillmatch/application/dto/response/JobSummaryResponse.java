package com.skillmatch.application.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobSummaryResponse {
    private Long id;
    private Long recruiterId;
    private String status;
}
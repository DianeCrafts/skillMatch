package com.skillmatch.job.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class InternalJobSummaryResponse {
    private Long id;
    private Long recruiterId;
    private String status;
}
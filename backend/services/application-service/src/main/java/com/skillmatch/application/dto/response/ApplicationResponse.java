package com.skillmatch.application.dto.response;

import com.skillmatch.application.entity.ApplicationStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ApplicationResponse {
    private Long id;
    private Long jobId;
    private Long applicantId;
    private ApplicationStatus status;
    private LocalDateTime appliedAt;
    private LocalDateTime withdrawnAt;
    private LocalDateTime updatedAt;
    private List<ApplicationStatusHistoryResponse> statusHistory;
}
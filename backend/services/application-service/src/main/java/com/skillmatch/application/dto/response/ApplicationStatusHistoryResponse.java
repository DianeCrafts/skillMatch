package com.skillmatch.application.dto.response;

import com.skillmatch.application.entity.ApplicationStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ApplicationStatusHistoryResponse {
    private Long id;
    private ApplicationStatus status;
    private LocalDateTime changedAt;
    private Long changedBy;
}
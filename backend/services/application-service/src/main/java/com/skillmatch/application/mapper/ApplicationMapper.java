package com.skillmatch.application.mapper;

import com.skillmatch.application.dto.response.ApplicationResponse;
import com.skillmatch.application.dto.response.ApplicationStatusHistoryResponse;
import com.skillmatch.application.entity.Application;
import com.skillmatch.application.entity.ApplicationStatusHistory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApplicationMapper {

    public ApplicationResponse toResponse(Application application) {
        List<ApplicationStatusHistoryResponse> historyResponses =
                application.getStatusHistory()
                        .stream()
                        .sorted((a, b) -> a.getChangedAt().compareTo(b.getChangedAt()))
                        .map(this::toHistoryResponse)
                        .toList();

        return ApplicationResponse.builder()
                .id(application.getId())
                .jobId(application.getJobId())
                .applicantId(application.getApplicantId())
                .status(application.getStatus())
                .appliedAt(application.getAppliedAt())
                .withdrawnAt(application.getWithdrawnAt())
                .updatedAt(application.getUpdatedAt())
                .statusHistory(historyResponses)
                .build();
    }

    public ApplicationStatusHistoryResponse toHistoryResponse(ApplicationStatusHistory history) {
        return ApplicationStatusHistoryResponse.builder()
                .id(history.getId())
                .status(history.getStatus())
                .changedAt(history.getChangedAt())
                .changedBy(history.getChangedBy())
                .build();
    }
}
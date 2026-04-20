package com.skillmatch.application.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplyRequest {

    @NotNull(message = "jobId is required")
    private Long jobId;
}
package com.skillmatch.application.dto.request;

import com.skillmatch.application.entity.ApplicationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateApplicationStatusRequest {

    @NotNull(message = "status is required")
    private ApplicationStatus status;
}
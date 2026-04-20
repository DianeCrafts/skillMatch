package com.skillmatch.notification.exception;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ErrorResponse {
    private int status;
    private String error;
    private LocalDateTime timestamp;
}
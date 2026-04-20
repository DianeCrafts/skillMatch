package com.skillmatch.notification.dto.request;

import com.skillmatch.notification.entity.NotificationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateNotificationRequest {

    @NotNull
    private Long recipientUserId;

    @NotNull
    private NotificationType type;

    @NotBlank
    private String title;

    @NotBlank
    private String message;
}
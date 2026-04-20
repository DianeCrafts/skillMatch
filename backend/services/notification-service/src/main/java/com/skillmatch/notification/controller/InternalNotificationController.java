package com.skillmatch.notification.controller;

import com.skillmatch.notification.dto.request.CreateNotificationRequest;
import com.skillmatch.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications/internal")
@RequiredArgsConstructor
public class InternalNotificationController {

    private final NotificationService notificationService;

    @PostMapping
    @Operation(summary = "Create notification internally from another service")
    public ResponseEntity<Void> createNotification(@Valid @RequestBody CreateNotificationRequest request) {
        notificationService.createNotification(request);
        return ResponseEntity.ok().build();
    }
}
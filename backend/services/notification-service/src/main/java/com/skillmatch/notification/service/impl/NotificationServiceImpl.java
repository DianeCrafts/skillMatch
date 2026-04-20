package com.skillmatch.notification.service.impl;

import com.skillmatch.notification.dto.request.CreateNotificationRequest;
import com.skillmatch.notification.dto.response.NotificationResponse;
import com.skillmatch.notification.entity.Notification;
import com.skillmatch.notification.exception.ResourceNotFoundException;
import com.skillmatch.notification.repository.NotificationRepository;
import com.skillmatch.notification.security.SecurityUtils;
import com.skillmatch.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    public void createNotification(CreateNotificationRequest request) {
        Notification notification = Notification.builder()
                .recipientUserId(request.getRecipientUserId())
                .type(request.getType())
                .title(request.getTitle())
                .message(request.getMessage())
                .build();

        notificationRepository.save(notification);
    }

    @Override
    public List<NotificationResponse> getMyNotifications() {
        Long currentUserId = SecurityUtils.getCurrentUserId();

        return notificationRepository.findByRecipientUserIdOrderByCreatedAtDesc(currentUserId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public NotificationResponse markAsRead(Long notificationId) {
        Long currentUserId = SecurityUtils.getCurrentUserId();

        Notification notification = notificationRepository
                .findByIdAndRecipientUserId(notificationId, currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));

        notification.setRead(true);
        notificationRepository.save(notification);

        return mapToResponse(notification);
    }

    @Override
    public void markAllAsRead() {
        Long currentUserId = SecurityUtils.getCurrentUserId();

        List<Notification> notifications =
                notificationRepository.findByRecipientUserIdAndReadFalse(currentUserId);

        notifications.forEach(notification -> notification.setRead(true));

        notificationRepository.saveAll(notifications);
    }

    private NotificationResponse mapToResponse(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .type(notification.getType().name())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .read(notification.isRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
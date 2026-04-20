package com.skillmatch.notification.service;

import com.skillmatch.notification.dto.request.CreateNotificationRequest;
import com.skillmatch.notification.dto.response.NotificationResponse;

import java.util.List;

public interface NotificationService {

    void createNotification(CreateNotificationRequest request);

    List<NotificationResponse> getMyNotifications();

    NotificationResponse markAsRead(Long notificationId);

    void markAllAsRead();
}
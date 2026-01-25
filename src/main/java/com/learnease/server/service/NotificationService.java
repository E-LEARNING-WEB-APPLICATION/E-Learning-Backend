package com.learnease.server.service;

import com.learnease.server.dto.notification.NotificationResponseDTO;
import com.learnease.server.dto.notification.SendNotificationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface NotificationService {
    public void sendNotification(SendNotificationDTO notification);
    public NotificationResponseDTO getNotificationById(UUID notificationId);
    public NotificationResponseDTO getNotificationByIdAndUserId(UUID notificationId, UUID userId);
    public void updateNotificationRead(UUID userId , UUID notificationId);
    public Page<NotificationResponseDTO> getUserNotifications(UUID userAuthId, Pageable pageable);
    public Page<NotificationResponseDTO> getUserUnreadNotification(UUID userAuthId, Pageable pageable);
    public long getNotificationCount(UUID userAuthId, Boolean includeRead);
}

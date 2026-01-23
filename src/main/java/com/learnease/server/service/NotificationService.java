package com.learnease.server.service;

import com.learnease.server.dto.notification.CreateNotificationDTO;
import com.learnease.server.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface NotificationService {
    public Notification createNotification(CreateNotificationDTO request);
    public Page<Notification> getAdminNotifications(Pageable pageable);
    public Page<Notification> getAdminUnreadNotifications(Pageable pageable);
    public Page<Notification> getUserNotifications(UUID userAuthId, Pageable pageable);
    public Page<Notification> getUserUnreadNotification(UUID userAuthId, Pageable pageable);
}

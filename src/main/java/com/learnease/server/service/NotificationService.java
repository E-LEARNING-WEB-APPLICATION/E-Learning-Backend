package com.learnease.server.service;

import com.learnease.server.dto.notification.CreateNotificationDTO;
import com.learnease.server.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationService {
    public Notification createNotification(CreateNotificationDTO request);
    public Page<Notification> getAdminNotifications(Pageable pageable);
}

package com.learnease.server.dto.notification;

import com.learnease.server.model.enums.NotificationPriority;
import com.learnease.server.model.enums.NotificationType;

import java.util.UUID;

public record CreateNotificationDTO(
        NotificationType type,
        NotificationPriority priority,
        NotificationAudience receiver,
        String title,
        String message,
        UUID recipientId,
        String actionUrl
) {}


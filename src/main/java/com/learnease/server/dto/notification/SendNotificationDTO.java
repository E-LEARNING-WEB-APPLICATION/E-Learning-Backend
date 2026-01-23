package com.learnease.server.dto.notification;

import com.learnease.server.model.enums.NotificationPriority;
import com.learnease.server.model.enums.NotificationType;
import com.learnease.server.model.enums.Role;

import java.util.UUID;

public record SendNotificationDTO(
        NotificationType type,
        NotificationPriority priority,
        String title,
        String message,
        String actionUrl,

        UUID userId,     // optional
        Role role        // optional
) {}

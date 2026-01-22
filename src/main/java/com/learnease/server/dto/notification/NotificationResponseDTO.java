package com.learnease.server.dto.notification;

import com.learnease.server.model.enums.NotificationPriority;
import com.learnease.server.model.enums.NotificationType;

import java.time.LocalDateTime;
import java.util.UUID;

public record NotificationResponseDTO(
        UUID id,
        NotificationType type,
        NotificationPriority priority,
        String title,
        String message,
        boolean read,
        LocalDateTime createdAt,
        String actionUrl
) {}

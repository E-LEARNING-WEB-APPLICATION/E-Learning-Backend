package com.learnease.server.dto.notification;

import com.learnease.server.model.enums.NotificationPriority;
import com.learnease.server.model.enums.NotificationSubjectType;
import com.learnease.server.model.enums.NotificationType;
import com.learnease.server.model.enums.Role;
import lombok.Builder;

import java.util.UUID;

@Builder
public record SendNotificationDTO(
        NotificationType type,
        NotificationPriority priority,
        String title,
        String message,
        NotificationSubjectType subjectType,
        UUID subjectId,

        UUID userId,     // optional
        Role role        // optional
) {}

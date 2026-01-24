package com.learnease.server.dto.notification;

import com.learnease.server.model.Notification;
import com.learnease.server.model.enums.NotificationPriority;
import com.learnease.server.model.enums.NotificationType;

import java.util.UUID;

public record NotificationPushDTO(
        UUID id,
        NotificationType type,
        NotificationPriority priority,
        String title
) {
    public static NotificationPushDTO from(Notification n) {
        return new NotificationPushDTO(
                n.getId(),
                n.getType(),
                n.getPriority(),
                n.getTitle()
        );
    }
}

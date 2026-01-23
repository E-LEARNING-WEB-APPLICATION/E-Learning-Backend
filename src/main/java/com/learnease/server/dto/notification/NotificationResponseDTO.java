package com.learnease.server.dto.notification;

import com.learnease.server.model.Notification;
import com.learnease.server.model.NotificationRecipient;
import com.learnease.server.model.enums.NotificationPriority;
import com.learnease.server.model.enums.NotificationSubjectType;
import com.learnease.server.model.enums.NotificationType;

import java.time.LocalDateTime;
import java.util.UUID;

public record NotificationResponseDTO(
        UUID id,
        NotificationType type,
        NotificationPriority priority,
        String title,
        String message,
        UUID subject,
        NotificationSubjectType subjectType,
        boolean read,
        LocalDateTime createdAt
) {
    public static NotificationResponseDTO from(NotificationRecipient nr) {
        Notification n = nr.getNotification();

        return new NotificationResponseDTO(
                n.getId(),
                n.getType(),
                n.getPriority(),
                n.getTitle(),
                n.getMessage(),
                n.getSubjectId(),
                n.getSubjectType(),
                nr.isRead(),
                nr.getCreatedAt()
        );
    }
}

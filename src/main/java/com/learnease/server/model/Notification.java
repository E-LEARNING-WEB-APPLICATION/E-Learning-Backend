package com.learnease.server.model;

import com.learnease.server.dto.notification.NotificationResponseDTO;
import com.learnease.server.model.enums.NotificationPriority;
import com.learnease.server.model.enums.NotificationType;
import com.learnease.server.model.enums.Role;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Entity
@AttributeOverride(name = "id", column = @Column(name = "notification_id"))
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class Notification extends BaseEntity {
    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @Enumerated(EnumType.STRING)
    private NotificationPriority priority;

    private String title;
    private String message;

    @ManyToOne
    private UserAuth recipient;

    private boolean read;
    private LocalDateTime readAt;

    private boolean deleted;

    private String actionUrl;

    public NotificationResponseDTO toDto() {
        return new NotificationResponseDTO(
                this.getId(),
                this.getType(),
                this.getPriority(),
                this.getTitle(),
                this.getMessage(),
                this.isRead(),
                this.getCreatedAt(),
                this.getActionUrl()
        );
    }
}


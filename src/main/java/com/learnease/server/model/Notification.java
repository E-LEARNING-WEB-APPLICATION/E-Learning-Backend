package com.learnease.server.model;

import com.learnease.server.model.enums.NotificationPriority;
import com.learnease.server.model.enums.NotificationSubjectType;
import com.learnease.server.model.enums.NotificationType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.UUID;

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

    // Generic reference
    private UUID subjectId;

    // Optional: polymorphic reference
    @Enumerated(EnumType.STRING)
    private NotificationSubjectType subjectType;

    // metadata only — no read/delete state here

}


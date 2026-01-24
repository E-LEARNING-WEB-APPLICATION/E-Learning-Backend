package com.learnease.server.model;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Entity
@AttributeOverride(name = "id", column = @Column(name = "notification_recipient_id"))
public class NotificationRecipient extends BaseEntity {

    @ManyToOne(optional = false)
    private Notification notification;

    @ManyToOne(optional = false)
    private UserAuth recipient;

    private boolean isRead;
    private LocalDateTime readAt;

    private boolean deleted;
}


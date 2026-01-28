package com.learnease.server.repository;

import com.learnease.server.model.NotificationRecipient;
import com.learnease.server.model.UserAuth;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface NotificationRecipientRepository extends JpaRepository<NotificationRecipient, UUID> {
    Page<NotificationRecipient> findByRecipientAndDeletedFalse(UserAuth user, Pageable pageable);
    Page<NotificationRecipient> findByRecipientAndDeletedFalseAndIsRead(UserAuth user, boolean isRead, Pageable pageable);
    Optional<NotificationRecipient> findByNotificationIdAndRecipientId(UUID notificationId, UUID recipientId);
    long countAllByRecipient(UserAuth userAuth);
    long countAllByRecipientAndIsRead(UserAuth userAuth, boolean isRead);
}

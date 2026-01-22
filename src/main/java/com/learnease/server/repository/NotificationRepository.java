package com.learnease.server.repository;

import com.learnease.server.model.Notification;
import com.learnease.server.model.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    public Page<Notification> findByRecipient_RoleAndDeletedFalse(Role role, Pageable pageable);
}

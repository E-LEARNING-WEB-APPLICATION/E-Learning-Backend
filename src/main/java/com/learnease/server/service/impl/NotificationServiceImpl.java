package com.learnease.server.service.impl;

import com.learnease.server.dto.notification.CreateNotificationDTO;
import com.learnease.server.model.Notification;
import com.learnease.server.model.UserAuth;
import com.learnease.server.model.enums.Role;
import com.learnease.server.repository.NotificationRepository;
import com.learnease.server.repository.UserAuthRepository;
import com.learnease.server.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final UserAuthRepository userAuthRepository;
    private final NotificationRepository notificationRepository;


    @Override
    public Notification createNotification(CreateNotificationDTO dto) {
        UserAuth recipient = userAuthRepository.findById(dto.recipientId())
                .orElseThrow(() -> new IllegalArgumentException("Recipient not found"));

        Notification notification = new Notification();
        notification.setType(dto.type());
        notification.setPriority(dto.priority());
        notification.setTitle(dto.title());
        notification.setMessage(dto.message());
        notification.setRecipient(recipient);
        notification.setActionUrl(dto.actionUrl());
        notification.setRead(false);
        notification.setDeleted(false);

        return notificationRepository.save(notification);
    }

    @Override
    public Page<Notification> getAdminNotifications(Pageable pageable) {
        return notificationRepository.findByRecipient_RoleAndDeletedFalse(Role.ADMIN, pageable);
    }

}

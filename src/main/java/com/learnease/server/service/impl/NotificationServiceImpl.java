package com.learnease.server.service.impl;

import com.learnease.server.dto.notification.NotificationPushDTO;
import com.learnease.server.dto.notification.NotificationResponseDTO;
import com.learnease.server.dto.notification.SendNotificationDTO;
import com.learnease.server.exception.custom_exception.BadClientRequestException;
import com.learnease.server.exception.custom_exception.ResourceNotFoundException;
import com.learnease.server.model.Notification;
import com.learnease.server.model.NotificationRecipient;
import com.learnease.server.model.UserAuth;
import com.learnease.server.repository.NotificationRecipientRepository;
import com.learnease.server.repository.NotificationRepository;
import com.learnease.server.repository.UserAuthRepository;
import com.learnease.server.service.NotificationService;
import com.learnease.server.service.NotificationSseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final UserAuthRepository userAuthRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationRecipientRepository notificationRecipientRepository;
    private final NotificationSseService notificationSseService;


    @Transactional
    @Override
    public void sendNotification(SendNotificationDTO dto) {

        Notification notification = new Notification()
                .setType(dto.type())
                .setPriority(dto.priority())
                .setTitle(dto.title())
                .setMessage(dto.message())
                .setSubjectId(dto.subjectId())
                .setSubjectType(dto.subjectType());

        notificationRepository.save(notification);

        List<UserAuth> users = resolveRecipients(dto);

        List<NotificationRecipient> recipients = users.stream()
                .map(user -> {
                    NotificationRecipient nr = new NotificationRecipient();
                    nr.setNotification(notification);
                    nr.setRecipient(user);
                    nr.setRead(false);
                    nr.setDeleted(false);
                    return nr;
                })
                .toList();

        notificationRecipientRepository.saveAll(recipients);

        pushNotifications(notification, recipients);
    }

    private List<UserAuth> resolveRecipients(SendNotificationDTO dto) {
        if (dto.userId() != null) {
            return List.of(
                    userAuthRepository.findById(dto.userId())
                            .orElseThrow(() -> new IllegalArgumentException("User not found"))
            );
        }
        return userAuthRepository.findByRole(dto.role());
    }

    private void pushNotifications(
            Notification notification,
            List<NotificationRecipient> recipients
    ) {
        for (NotificationRecipient r : recipients) {
            notificationSseService.pushNotification(
                    r.getRecipient().getId(),
                    NotificationPushDTO.from(notification)
            );
        }
    }


    @Override
    public NotificationResponseDTO getNotificationById(UUID notificationId) {
        NotificationRecipient nr = notificationRecipientRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("notification not found"));
        return NotificationResponseDTO.from(nr);
    }

    @Override
    public NotificationResponseDTO getNotificationByIdAndUserId(UUID notificationId, UUID userId) {
        NotificationRecipient nr = notificationRecipientRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("notification not found"));
        if (nr.getRecipient().getId() != userId) throw new BadClientRequestException("Invalid access to notification");
        return NotificationResponseDTO.from(nr);
    }

    @Transactional
    @Override
    public void updateNotificationRead(UUID userId, UUID notificationId) {
        NotificationRecipient nr = notificationRecipientRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("notification not found"));
        if (nr.getRecipient().getId() != userId) throw new BadClientRequestException("Invalid access to notification");
        nr.setRead(true);
        nr.setReadAt(LocalDateTime.now());
        notificationRecipientRepository.save(nr);
    }


    @Override
    public Page<NotificationResponseDTO> getUserNotifications(UUID userAuthId, Pageable pageable) {
        UserAuth user = userAuthRepository.findById(userAuthId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return notificationRecipientRepository
                .findByRecipientAndDeletedFalse(user, pageable)
                .map(nr -> NotificationResponseDTO.from(nr));
    }

    @Override
    public Page<NotificationResponseDTO> getUserUnreadNotification(UUID userAuthId, Pageable pageable) {
        UserAuth user = userAuthRepository.findById(userAuthId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return notificationRecipientRepository
                .findByRecipientAndDeletedFalseAndIsRead(user, false, pageable)
                .map(nr -> NotificationResponseDTO.from(nr));
    }

}

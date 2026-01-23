package com.learnease.server.service.impl;

import com.learnease.server.dto.notification.NotificationResponseDTO;
import com.learnease.server.dto.notification.SendNotificationDTO;
import com.learnease.server.model.Notification;
import com.learnease.server.model.NotificationRecipient;
import com.learnease.server.model.UserAuth;
import com.learnease.server.repository.NotificationRecipientRepository;
import com.learnease.server.repository.NotificationRepository;
import com.learnease.server.repository.UserAuthRepository;
import com.learnease.server.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final UserAuthRepository userAuthRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationRecipientRepository notificationRecipientRepository;


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

        List<UserAuth> recipients;

        //If dto has userId set it will get priority over role
        if (dto.userId() != null) {
            recipients = List.of(
                    userAuthRepository.findById(dto.userId())
                            .orElseThrow(() -> new IllegalArgumentException("User not found"))
            );
        } else {
            recipients = userAuthRepository.findByRole(dto.role());
        }

        //TODO: think of some elegant solution to solve this n inserts problem
        recipients.forEach(user -> {
            NotificationRecipient nr = new NotificationRecipient();
            nr.setNotification(notification);
            nr.setRecipient(user);
            nr.setRead(false);
            nr.setDeleted(false);

            notificationRecipientRepository.save(nr);
        });
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

package com.learnease.server.service;

import com.learnease.server.dto.notification.NotificationPushDTO;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;

public interface NotificationSseService {
    public SseEmitter subscribe(UUID userId);
    public void pushNotification(UUID userId, NotificationPushDTO dto);
}

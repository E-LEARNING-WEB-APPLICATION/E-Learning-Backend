package com.learnease.server.controller;

import com.learnease.server.dto.JWTDTO;
import com.learnease.server.dto.notification.NotificationResponseDTO;
import com.learnease.server.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/")
    @PreAuthorize("hasAnyRole('ADMIN','STUDENT','INSTRUCTOR')")
    public Page<NotificationResponseDTO> getAdminNotifications(
            @AuthenticationPrincipal JWTDTO dto,
            @RequestParam(required = false) Boolean isRead,
            Pageable pageable
    ) {
        if(isRead!=null && !isRead){
            return notificationService.getUserUnreadNotification(dto.getUserId(), pageable);

        } else{
            return notificationService
                    .getUserNotifications(dto.getUserId(), pageable);
        }
    }
}

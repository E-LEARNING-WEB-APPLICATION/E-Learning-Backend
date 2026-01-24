package com.learnease.server.controller;

import com.learnease.server.dto.ApiResponse;
import com.learnease.server.dto.JWTDTO;
import com.learnease.server.dto.notification.NotificationResponseDTO;
import com.learnease.server.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','STUDENT','INSTRUCTOR')")
    @PageableAsQueryParam
    public Page<NotificationResponseDTO> getUserNotifications(
            @AuthenticationPrincipal JWTDTO dto,
            @RequestParam(required = false) Boolean isRead,
            @RequestParam(required = false) Pageable pageable
    ) {
        if (isRead != null && !isRead) {
            return notificationService.getUserUnreadNotification(dto.getUserId(), pageable);

        } else {
            return notificationService
                    .getUserNotifications(dto.getUserId(), pageable);
        }
    }

    @GetMapping("/{notificationId}")
    public ResponseEntity<NotificationResponseDTO> getNotification(
            @AuthenticationPrincipal JWTDTO jwtdto,
            @PathVariable UUID notificationId
    ) {
        return ResponseEntity.ok(notificationService.getNotificationByIdAndUserId(notificationId, jwtdto.getUserId()));
    }

    public ResponseEntity<ApiResponse> markNotificationRead(
            @AuthenticationPrincipal JWTDTO jwtdto,
            @PathVariable UUID notificationId
    ) {
        notificationService.updateNotificationRead(jwtdto.getUserId(), notificationId);
        return ResponseEntity.ok(new ApiResponse(true, "notification marked read successfully"));
    }
}

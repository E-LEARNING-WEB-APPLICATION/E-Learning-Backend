package com.learnease.server.controller;

import com.learnease.server.dto.JWTDTO;
import com.learnease.server.service.NotificationSseService;
import com.learnease.server.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationSseController {

    private final NotificationSseService sseService;
    private final JwtUtil jwtUtil;

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamNotifications(
//            @AuthenticationPrincipal JWTDTO user
            @RequestParam String token
    ) {
//        return sseService.subscribe(user.getUserId());
        Claims claims = jwtUtil.getClaims(token.trim());
        return sseService.subscribe(UUID.fromString(
                claims.get("user_id", String.class)));
    }
}


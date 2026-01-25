package com.learnease.server.controller;

import com.learnease.server.service.NotificationSseService;
import com.learnease.server.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
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
            @RequestParam String token
    ) {
        try {
            Claims claims = jwtUtil.getClaims(token.trim());
            return sseService.subscribe(UUID.fromString(
                    claims.get("user_id", String.class)));
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid or expired token"
            );
        }
    }
}


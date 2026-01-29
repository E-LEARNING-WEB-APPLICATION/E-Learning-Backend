package com.learnease.server.service.impl;

import com.learnease.server.dto.notification.EmailEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class EmailClientImpl {

    private final WebClient emailWebClient;

    public void sendEmail(EmailEvent request) {
        emailWebClient.post()
                .uri("/api/emails/send")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Void.class);
    }
}


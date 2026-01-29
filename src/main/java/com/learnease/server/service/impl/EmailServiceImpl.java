package com.learnease.server.service.impl;

import com.learnease.server.dto.notification.EmailEvent;
import com.learnease.server.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final WebClient emailWebClient;


    public void sendEmail(EmailEvent request) {
        emailWebClient.post()
                .uri("/api/emails/send")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        response.bodyToMono(String.class)
                                .flatMap(body -> {
                                    log.error("Email API error: {}", body);
                                    return Mono.error(new RuntimeException(body));
                                })
                )
                .bodyToMono(Void.class)
                .subscribe(
                        success -> {
                            // email sent successfully
                            log.info("Email sent successfully");
                        },
                        error -> {
                            // email sending failed
                            log.error("Email sending failed", error);
                        }
                );;
    }
}


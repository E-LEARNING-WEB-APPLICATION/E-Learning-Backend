package com.learnease.server.service;

import com.learnease.server.dto.notification.EmailEvent;

public interface EmailService {
    void sendEmail(EmailEvent request);
}

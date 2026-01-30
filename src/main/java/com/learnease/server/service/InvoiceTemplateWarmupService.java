package com.learnease.server.service;

import com.learnease.server.util.enums.InvoiceType;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class InvoiceTemplateWarmupService {

    private final TemplateEngine templateEngine;

    @PostConstruct
    public void preload() {
        for (InvoiceType type : InvoiceType.values()) {
            try {
                templateEngine.process(
                        type.getTemplatePath(),
                        new Context() // empty context, just parse template
                );
            } catch (Exception e) {
                log.error("Invoice template invalid: {}", type, e);
//                throw e; // fail fast in prod
            }
        }
    }
}


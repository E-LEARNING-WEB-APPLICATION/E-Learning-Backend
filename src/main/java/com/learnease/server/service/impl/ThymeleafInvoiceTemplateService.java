package com.learnease.server.service.impl;

import com.learnease.server.service.InvoiceTemplateService;
import com.learnease.server.util.enums.InvoiceType;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;
@Service
public class ThymeleafInvoiceTemplateService
        implements InvoiceTemplateService {

    private final TemplateEngine templateEngine;

    public ThymeleafInvoiceTemplateService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Override
    public String renderInvoice(
            InvoiceType type,
            Map<String, Object> variables
    ) {
        Context context = new Context();
        context.setVariables(variables);

        return templateEngine.process(
                type.getTemplatePath(),
                context
        );
    }
}



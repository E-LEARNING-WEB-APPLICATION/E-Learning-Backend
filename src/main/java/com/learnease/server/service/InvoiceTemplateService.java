package com.learnease.server.service;

import com.learnease.server.util.enums.InvoiceType;

import java.util.Map;

public interface InvoiceTemplateService {

    String renderInvoice(
            InvoiceType type,
            Map<String, Object> variables
    );
}

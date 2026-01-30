package com.learnease.server.service;

import com.learnease.server.dto.invoice.InvoiceDto;
import com.learnease.server.util.enums.InvoiceType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceTemplateService templateService;
    private final PdfGenerator pdfGenerator;

    public byte[] generateInvoice(
            InvoiceType type,
            InvoiceDto data
    ) {
        String html = templateService.renderInvoice(
                type,
                Map.of("invoice", data)
        );

        return pdfGenerator.generate(html);
    }
}


package com.learnease.server.service;

import com.learnease.server.service.impl.S3Service;
import com.learnease.server.util.enums.InvoiceType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InvoiceStorageService {

    private final S3Service s3Service;

    public String storeInvoice(
            InvoiceType type,
            String invoiceNumber,
            byte[] pdfBytes
    ) {

        String folder = "invoices/" + type.name().toLowerCase();
        String fileName = invoiceNumber + ".pdf";

        return s3Service.uploadBytes(
                pdfBytes,
                folder,
                fileName,
                "application/pdf"
        );
    }
}

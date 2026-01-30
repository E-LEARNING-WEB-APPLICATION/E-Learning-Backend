package com.learnease.server.exception.custom_exception;

public class InvoiceGenerationException extends RuntimeException {
    public InvoiceGenerationException(String message){super(message);}
    public InvoiceGenerationException(String message, Throwable cause) {
        super(message, cause); // Preserve the original IOException as the cause
    }
}

package com.learnease.server.exception.custom_exception;

public class EmailServiceException extends RuntimeException {
    public EmailServiceException(String message){super(message);}
    public EmailServiceException(String message, Throwable cause) {
        super(message, cause); // Preserve the original IOException as the cause
    }
}
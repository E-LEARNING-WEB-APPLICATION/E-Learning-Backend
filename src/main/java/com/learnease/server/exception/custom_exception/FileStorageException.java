package com.learnease.server.exception.custom_exception;

public class FileStorageException extends RuntimeException {

    public FileStorageException(String message) {
        super(message);
    }

    public FileStorageException(String message, Throwable cause) {
        super(message, cause); // Preserve the original IOException as the cause
    }
}

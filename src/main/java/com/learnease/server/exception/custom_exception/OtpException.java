package com.learnease.server.exception.custom_exception;

import com.learnease.server.model.enums.OtpErrorCode;
import lombok.Getter;

@Getter
public class OtpException extends RuntimeException {
    private final OtpErrorCode errorCode;

    public OtpException(OtpErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}

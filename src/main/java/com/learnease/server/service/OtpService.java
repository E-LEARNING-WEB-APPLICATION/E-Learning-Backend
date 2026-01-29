package com.learnease.server.service;

import com.learnease.server.model.enums.OtpPurpose;

import java.util.UUID;

public interface OtpService {

    /**
     * Generates a new OTP for the given user and purpose,
     * invalidating any existing active OTP.
     *
     * @return raw OTP (to be sent via email)
     */
    String generateAndSaveOtp(UUID userId, OtpPurpose purpose);

    /**
     * Validates the OTP for the given user and purpose.
     * Does NOT consume the OTP.
     */
    void validateOtp(UUID userId, OtpPurpose purpose, String otp);

    /**
     * Consumes (marks as used) the OTP after successful business action.
     */
    void consumeOtp(UUID userId, OtpPurpose purpose);
}

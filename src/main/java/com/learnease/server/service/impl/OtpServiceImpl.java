package com.learnease.server.service.impl;

import com.learnease.server.exception.custom_exception.OtpException;
import com.learnease.server.model.OtpVerification;
import com.learnease.server.model.enums.OtpErrorCode;
import com.learnease.server.model.enums.OtpPurpose;
import com.learnease.server.repository.OtpVerificationRepository;
import com.learnease.server.service.OtpService;
import com.learnease.server.util.OtpUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class OtpServiceImpl implements OtpService {

    private static final int OTP_LENGTH = 6;
    private static final int MAX_ATTEMPTS = 3;
    private static final long PASSWORD_RESET_EXPIRY_MINUTES = 5;

    private final OtpVerificationRepository otpRepository;
    private final PasswordEncoder passwordEncoder;
    private final OtpUtil otpUtil;

    @Override
    public String generateAndSaveOtp(UUID userId, OtpPurpose purpose) {

        // Invalidate any existing active OTP
        otpRepository.invalidateActiveOtps(userId,
                purpose,
                LocalDateTime.now());

        String rawOtp = otpUtil.generateOtp(OTP_LENGTH);
        String hashedOtp = passwordEncoder.encode(rawOtp);

        OtpVerification otp = new OtpVerification();
        otp.setUserId(userId);
        otp.setPurpose(purpose);
        otp.setOtpHash(hashedOtp);
        otp.setExpiresAt(LocalDateTime.now().plusMinutes(PASSWORD_RESET_EXPIRY_MINUTES));
        otp.setAttemptCount(0);

        otpRepository.save(otp);

        log.info("OTP generated for userId={} purpose={}", userId, purpose);
        return rawOtp;
    }

    @Override
    @Transactional( propagation = Propagation.REQUIRES_NEW,
            noRollbackFor = OtpException.class)
    public void validateOtp(UUID userId, OtpPurpose purpose, String otp) {
        OtpVerification otpEntity = otpRepository
                .findByUserIdAndPurposeAndConsumedAtIsNullAndExpiresAtAfter(
                        userId,
                        purpose,
                        LocalDateTime.now()
                ).orElseThrow(() ->
                        new OtpException(
                                OtpErrorCode.OTP_NOT_FOUND,
                                "OTP not found or expired"
                        )
                );

        if (otpEntity.isExpired()) {
            throw new OtpException(
                    OtpErrorCode.OTP_EXPIRED,
                    "OTP has expired"
            );
        };

        if (otpEntity.getAttemptCount() >= MAX_ATTEMPTS) {
            throw new OtpException(
                    OtpErrorCode.OTP_ATTEMPTS_EXCEEDED,
                    "OTP attempts exceeded"
            );
        }

        if (!passwordEncoder.matches(otp, otpEntity.getOtpHash())) {
            otpEntity.setAttemptCount(otpEntity.getAttemptCount() + 1);
            otpRepository.save(otpEntity);
            throw new OtpException(
                    OtpErrorCode.OTP_INVALID,
                    "Invalid OTP"
            );
        }

        log.info("OTP validated | userId={} purpose={}", userId, purpose);
    };

    @Override
    public void consumeOtp(UUID userId, OtpPurpose purpose) {
        OtpVerification otpEntity = otpRepository
                .findByUserIdAndPurposeAndConsumedAtIsNullAndExpiresAtAfter(
                        userId,
                        purpose,
                        LocalDateTime.now()
                )
                .orElseThrow(() ->
                        new OtpException(
                                OtpErrorCode.OTP_NOT_FOUND,
                                "OTP already used or expired"
                        )
                );

        otpEntity.markConsumed();
        otpRepository.save(otpEntity);

        log.info("OTP consumed | userId={} purpose={}", userId, purpose);
    }
}

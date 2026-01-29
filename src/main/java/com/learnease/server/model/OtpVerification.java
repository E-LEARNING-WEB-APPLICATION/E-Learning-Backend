package com.learnease.server.model;


import com.learnease.server.model.enums.OtpPurpose;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "otp_verification")
@AttributeOverride(
        name = "id",
        column = @Column(name = "otp_id", nullable = false, updatable = false)
)
public class OtpVerification extends BaseEntity {

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "purpose", nullable = false, length = 30)
    private OtpPurpose purpose;

    @Column(name = "otp_hash", nullable = false, length = 255)
    private String otpHash;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "attempt_count", nullable = false)
    private int attemptCount = 0;

    @Column(name = "consumed_at")
    private LocalDateTime consumedAt;

    /* ---------- Domain helper methods ---------- */

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiresAt);
    }

    public boolean isConsumed() {
        return this.consumedAt != null;
    }

    public void markConsumed() {
        this.consumedAt = LocalDateTime.now();
    }

    public void incrementAttempt() {
        this.attemptCount++;
    }
}

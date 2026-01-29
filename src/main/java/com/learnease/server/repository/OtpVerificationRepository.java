package com.learnease.server.repository;

import com.learnease.server.model.OtpVerification;
import com.learnease.server.model.enums.OtpPurpose;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface OtpVerificationRepository extends JpaRepository<OtpVerification , UUID> {

    Optional<OtpVerification> findByUserIdAndPurposeAndConsumedAtIsNullAndExpiresAtAfter(
            UUID userId,
            OtpPurpose purpose,
            LocalDateTime now
    );

    @Modifying
    @Query("""
        update OtpVerification o
           set o.consumedAt = :now
         where o.userId = :userId
           and o.purpose = :purpose
           and o.consumedAt is null
    """)
    int invalidateActiveOtps(
            @Param("userId") UUID userId,
            @Param("purpose") OtpPurpose purpose,
            @Param("now") LocalDateTime now
    );

    @Modifying
    @Query("""
        delete from OtpVerification o
         where o.expiresAt < :now
    """)
    int deleteExpiredOtps(@Param("now") LocalDateTime now);
}

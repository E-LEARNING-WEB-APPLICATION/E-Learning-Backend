package com.learnease.server.repository;

import com.learnease.server.model.Booking;
import com.learnease.server.model.WalletTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public interface WalletTransactionRepository
        extends JpaRepository<WalletTransaction, UUID> {

    boolean existsByBooking(Booking booking);

    @Query("""
    SELECT COALESCE(SUM(wt.amount), 0)
    FROM WalletTransaction wt
    WHERE wt.instructor.id = :instructorId
      AND wt.createdAt >= :startDate
""")
    BigDecimal findTotalRevenueLast30Days(
            @Param("instructorId") UUID instructorId,
            @Param("startDate") LocalDateTime startDate
    );

    @Query("""
        SELECT COALESCE(SUM(wt.amount), 0)
        FROM WalletTransaction wt
        WHERE wt.instructor.id = :instructorId
    """)
    BigDecimal findTotalRevenueByInstructorId(
            @Param("instructorId") UUID instructorId
    );

    @Query("""
    SELECT COALESCE(SUM(w.amount), 0)
    FROM WalletTransaction w
    WHERE w.instructor.id = :instructorId
      AND w.payoutStatus = com.learnease.server.model.enums.PayoutStatus.AVAILABLE
    """)
    BigDecimal getAvailableBalance(@Param("instructorId") UUID instructorId);

    @Modifying
    @Query("""
        UPDATE WalletTransaction w
        SET w.payoutStatus = com.learnease.server.model.enums.PayoutStatus.PAID_OUT,
            w.paidOutAt = :paidOutAt
        WHERE w.instructor.id = :instructorId
          AND w.payoutStatus = com.learnease.server.model.enums.PayoutStatus.AVAILABLE
    """)
    int withdrawAvailableAmount(
            @Param("instructorId") UUID instructorId,
            @Param("paidOutAt") LocalDateTime paidOutAt
    );
}


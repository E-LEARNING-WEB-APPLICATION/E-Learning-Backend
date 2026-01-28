package com.learnease.server.model;

import com.learnease.server.model.enums.PayoutStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(
        name = "wallet_transaction",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_wallet_booking", columnNames = "booking_id")
        }
)
@AttributeOverride(name = "id", column = @Column(name = "transaction_id"))
public class WalletTransaction extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructor_id", nullable = false)
    private Instructor instructor;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private PayoutStatus payoutStatus = PayoutStatus.AVAILABLE;

    private LocalDateTime paidOutAt;
}

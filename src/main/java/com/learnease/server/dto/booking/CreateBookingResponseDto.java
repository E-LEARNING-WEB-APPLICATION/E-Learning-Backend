package com.learnease.server.dto.booking;


import com.learnease.server.model.enums.BookingStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateBookingResponseDto {

    private UUID bookingId;

    private String razorpayOrderId;

    private long amount;

    private String currency;

    private LocalDateTime expiresAt;

    private BookingStatus status;
}

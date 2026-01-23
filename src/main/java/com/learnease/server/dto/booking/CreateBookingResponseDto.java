package com.learnease.server.dto.booking;


import com.learnease.server.model.enums.BookingStatus;
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
public class CreateBookingResponseDto {

    private UUID bookingId;

    private String razorpayOrderId;

    private long amount;

    private String currency;

    private LocalDateTime expiresAt;

    private BookingStatus status;
}

package com.learnease.server.util.mappers;


import com.learnease.server.dto.booking.CreateBookingResponseDto;
import com.learnease.server.model.Booking;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class BookingMapper {

    public CreateBookingResponseDto toCreateBookingResponse(Booking booking) {

        long amountInPaise = booking
                .getPricePaid()
                .multiply(BigDecimal.valueOf(100))
                .longValueExact();

        return CreateBookingResponseDto.builder()
                .bookingId(booking.getId())
                .razorpayOrderId(booking.getRazorpayOrderId())
                .amount(amountInPaise)
                .currency(booking.getCurrency())
                .expiresAt(booking.getExpiresAt())
                .status(booking.getStatus())
                .build();
    };
}

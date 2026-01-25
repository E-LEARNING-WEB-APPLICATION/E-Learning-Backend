package com.learnease.server.service;

import com.learnease.server.dto.booking.CreateBookingRequestDto;
import com.learnease.server.dto.booking.CreateBookingResponseDto;
import com.learnease.server.dto.booking.VerifyPaymentRequestDto;
import jakarta.validation.Valid;

import java.util.UUID;

public interface BookingService {
    CreateBookingResponseDto createBooking(@Valid CreateBookingRequestDto request, UUID authId);

    void verifyPayment(UUID bookingId, @Valid VerifyPaymentRequestDto request);
}

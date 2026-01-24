package com.learnease.server.service;

import com.learnease.server.dto.booking.CreateBookingRequestDto;
import com.learnease.server.dto.booking.CreateBookingResponseDto;
import jakarta.validation.Valid;

import java.util.UUID;

public interface BookingService {
    CreateBookingResponseDto createBooking(@Valid CreateBookingRequestDto request, UUID authId);
}

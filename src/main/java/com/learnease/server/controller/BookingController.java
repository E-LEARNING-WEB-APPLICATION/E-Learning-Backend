package com.learnease.server.controller;


import com.learnease.server.dto.JWTDTO;
import com.learnease.server.dto.booking.CreateBookingRequestDto;
import com.learnease.server.dto.booking.CreateBookingResponseDto;
import com.learnease.server.dto.booking.VerifyPaymentRequestDto;
import com.learnease.server.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<CreateBookingResponseDto> createBooking(
            @RequestBody @Valid CreateBookingRequestDto request,
            @AuthenticationPrincipal JWTDTO jwtdto
            ){
        CreateBookingResponseDto response = bookingService.createBooking(
                request ,
                jwtdto.getUserId()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/{bookingId}/verify-payment")
    public ResponseEntity<Void> verifyPayment(
            @PathVariable UUID bookingId,
            @RequestBody @Valid VerifyPaymentRequestDto request
    ) {
        bookingService.verifyPayment(bookingId, request);
        return ResponseEntity.ok().build();
    }

}

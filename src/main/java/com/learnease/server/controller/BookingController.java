package com.learnease.server.controller;


import com.learnease.server.dto.ApiResponse;
import com.learnease.server.dto.JWTDTO;
import com.learnease.server.dto.booking.CreateBookingRequestDto;
import com.learnease.server.dto.booking.CreateBookingResponseDto;
import com.learnease.server.dto.booking.VerifyPaymentRequestDto;
import com.learnease.server.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/bookings")
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
@Tag(
        name = "Booking",
        description = "APIs for course booking and payment verification"
)
public class BookingController {

    private final BookingService bookingService;

    @Operation(
            summary = "Create a booking for a course",
            description = "Creates a pending booking and generates a Razorpay order for payment"
    )
    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping
    public ResponseEntity<?> createBooking(
            @RequestBody @Valid CreateBookingRequestDto request,
            @AuthenticationPrincipal JWTDTO jwtdto
            ){
        CreateBookingResponseDto response = bookingService.createBooking(
                request ,
                jwtdto.getUserId()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<CreateBookingResponseDto>(true , response));
    }

    @Operation(
            summary = "Verify course payment",
            description = "Verifies Razorpay payment signature and enrolls the student after successful payment"
    )
    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/{bookingId}/verify-payment")
    public ResponseEntity<Void> verifyPayment(
            @PathVariable UUID bookingId,
            @RequestBody @Valid VerifyPaymentRequestDto request
    ) {
        bookingService.verifyPayment(bookingId, request);
        return ResponseEntity.ok().build();
    }

}

package com.learnease.server.controller;


import com.learnease.server.dto.JWTDTO;
import com.learnease.server.dto.booking.CreateBookingRequestDto;
import com.learnease.server.dto.booking.CreateBookingResponseDto;
import com.learnease.server.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        System.out.println("Inside Controller" + jwtdto.toString());
        CreateBookingResponseDto response = bookingService.createBooking(
                request ,
                jwtdto.getUserId()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}

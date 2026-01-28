package com.learnease.server.dto.admin;

import com.learnease.server.model.enums.BookingStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record EnrolledStudentAdminDTO(
        // Booking
        UUID bookingId,
        BookingStatus bookingStatus,
        LocalDateTime purchaseTime,
        LocalDateTime paidAt,

        // Course
        UUID courseId,
        String courseTitle,

        // Student
        UUID studentId,
        UUID userId,
        String studentName,
        String studentEmail,
        String phoneNo,

        // Instructor
        UUID instructorId,
        String instructorName,

        // Payment
        BigDecimal pricePaid,
        String currency,
        String paymentMethod
) {}


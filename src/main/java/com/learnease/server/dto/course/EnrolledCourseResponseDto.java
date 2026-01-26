package com.learnease.server.dto.course;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EnrolledCourseResponseDto {

    private UUID bookingId;
    private LocalDateTime enrolledAt;

    private UUID courseId;
    private String courseTitle;
    private String courseThumbnail;

    private String categoryName;
    private String courseDuration;

    private BigDecimal coursePrice;

    private Double rating;
}


package com.learnease.server.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
@Data
@AllArgsConstructor
public class AdminCourseOverviewDto {

    // course
    private UUID courseId;
    private String courseName;
    private UUID categoryId;
    private String categoryName;

    // pricing
    private BigDecimal price;
    private Integer discount;
    private BigDecimal effectivePrice;

    // enrollment stats
    private Long totalEnrollments;
    private Long recentEnrollments;

    // revenue
    private BigDecimal totalRevenue;
    private BigDecimal avgRevenuePerStudent;

    // rating
    private Double avgRating;
    private Long totalFeedbacks;

    // instructor
    private UUID instructorId;
    private String instructorName;

    // course meta
    private Integer durationInHours;

    // ranking
    private Long revenueRank;

    // timestamps
    private LocalDateTime createdAt;
}

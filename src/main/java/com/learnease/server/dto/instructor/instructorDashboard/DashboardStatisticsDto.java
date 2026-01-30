package com.learnease.server.dto.instructor.instructorDashboard;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DashboardStatisticsDto {
    private int totalStudents;
    private int totalCourse;
    private int totalEnrollments;
    private BigDecimal totalRevenue;
    private long totalReviews;
    private double averageRating;
    private BigDecimal lastMonthRevenue;
    private long newStudent;
    private BigDecimal availableAmount;
}

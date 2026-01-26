package com.learnease.server.dto.admin;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public interface AdminCourseOverviewProjection {

    UUID getCourseId();
    String getCourseName();
    UUID getCategoryId();
    String getCategoryName();

    BigDecimal getPrice();
    Integer getDiscount();
    BigDecimal getEffectivePrice();

    Long getTotalEnrollments();
    Long getRecentEnrollments();

    BigDecimal getTotalRevenue();
    BigDecimal getAvgRevenuePerStudent();

    Double getAvgRating();
    Long getTotalFeedbacks();

    UUID getInstructorId();
    String getInstructorName();

    Integer getDurationInHours();
    Long getRevenueRank();

    LocalDateTime getCreatedAt();
}


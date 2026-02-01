package com.learnease.server.service;

import com.learnease.server.dto.admin.*;
import com.learnease.server.util.enums.InstructorSortBy;

import java.util.List;
import java.util.UUID;

public interface InstructorStatisticsService {
    List<InstructorMonthlyRevenueDTO> getTopInstructorByMonthlyRevenue(int top);

    List<InstructorEnrollmentsDTO> getTopInstructorByEnrollments(int top);

    List<CourseRevenueDTO> getTopCoursesByRevenue(UUID instructorId, int top);

    List<RatingCountDTO> getInstructorRatingDistribution(UUID instructorId);

    List<InstructorLeaderboardDTO> getTopInstructors(
            InstructorSortBy sortBy,
            int limit
    );
}

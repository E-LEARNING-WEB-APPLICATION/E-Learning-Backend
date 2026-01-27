package com.learnease.server.service;

import com.learnease.server.dto.admin.InstructorEnrollmentsDTO;
import com.learnease.server.dto.admin.InstructorLeaderboardDTO;
import com.learnease.server.dto.admin.InstructorMonthlyRevenueDTO;
import com.learnease.server.util.enums.InstructorSortBy;

import java.util.List;

public interface InstructorStatisticsService {
    List<InstructorMonthlyRevenueDTO> getTopInstructorByMonthlyRevenue(int top);

    List<InstructorEnrollmentsDTO> getTopInstructorByEnrollments(int top);

    List<InstructorLeaderboardDTO> getTopInstructors(
            InstructorSortBy sortBy,
            int limit
    );
}

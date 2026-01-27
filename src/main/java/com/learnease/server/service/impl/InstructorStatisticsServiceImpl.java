package com.learnease.server.service.impl;

import com.learnease.server.dto.admin.InstructorEnrollmentsDTO;
import com.learnease.server.dto.admin.InstructorLeaderboardDTO;
import com.learnease.server.dto.admin.InstructorMonthlyRevenueDTO;
import com.learnease.server.model.enums.BookingStatus;
import com.learnease.server.repository.BookingRepository;
import com.learnease.server.service.InstructorStatisticsService;
import com.learnease.server.util.enums.InstructorSortBy;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InstructorStatisticsServiceImpl implements InstructorStatisticsService {
    private static final Log log = LogFactory.getLog(InstructorStatisticsServiceImpl.class);
    private final BookingRepository bookingRepository;

    @Override
    public List<InstructorMonthlyRevenueDTO> getTopInstructorByMonthlyRevenue(int top) {
        return bookingRepository.findTopInstructorsByRevenue(BookingStatus.PAID, top);
    }

    @Override
    public List<InstructorEnrollmentsDTO> getTopInstructorByEnrollments(int top){
        return bookingRepository.findTopInstructorsByEnrollments(BookingStatus.PAID, top);
    }

    @Override
    public List<InstructorLeaderboardDTO> getTopInstructors(
            InstructorSortBy sortBy,
            int limit
    ) {
        return bookingRepository.findTopInstructors(
                BookingStatus.PAID.name(),
                sortBy.name(),
                limit
        );
    }


}

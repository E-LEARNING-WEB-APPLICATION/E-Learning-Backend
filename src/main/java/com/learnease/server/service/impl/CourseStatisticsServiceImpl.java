package com.learnease.server.service.impl;

import com.learnease.server.dto.admin.CourseEnrollmentDTO;
import com.learnease.server.repository.BookingRepository;
import com.learnease.server.service.CourseStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseStatisticsServiceImpl implements CourseStatisticsService {
    private final BookingRepository bookingRepository;

    @Override
    public List<CourseEnrollmentDTO> getTopCoursesByEnrollments(int top) {
        return bookingRepository.findTopCoursesByEnrollments(Pageable.ofSize(top));
    }
}

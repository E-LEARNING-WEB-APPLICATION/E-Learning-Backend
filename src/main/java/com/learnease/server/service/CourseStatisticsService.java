package com.learnease.server.service;

import com.learnease.server.dto.admin.CourseEnrollmentDTO;

import java.util.List;

public interface CourseStatisticsService {
    List<CourseEnrollmentDTO> getTopCoursesByEnrollments(int top);
}

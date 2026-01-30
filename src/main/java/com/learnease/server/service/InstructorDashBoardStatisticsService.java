package com.learnease.server.service;

import com.learnease.server.dto.instructor.instructorDashboard.CategoryCoursesCountDto;
import com.learnease.server.dto.instructor.instructorDashboard.CourseStudentCountDto;
import com.learnease.server.dto.instructor.instructorDashboard.DashboardStatisticsDto;

import java.util.List;
import java.util.UUID;

public interface InstructorDashBoardStatisticsService {
    List<CourseStudentCountDto> getStudentPerCourses(UUID authId);

    List<CategoryCoursesCountDto> getCoursesPerCategory(UUID authId);

    DashboardStatisticsDto getOverAllStat(UUID authId);
}

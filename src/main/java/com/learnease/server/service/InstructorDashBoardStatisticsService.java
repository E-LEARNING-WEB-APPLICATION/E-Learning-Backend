package com.learnease.server.service;

import com.learnease.server.dto.instructor.instructorDashboard.CourseStudentCountDto;

import java.util.List;
import java.util.UUID;

public interface InstructorDashBoardStatisticsService {
    List<CourseStudentCountDto> getStudentPerCourses(UUID authId);
}

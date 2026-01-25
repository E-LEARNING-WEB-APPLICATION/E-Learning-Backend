package com.learnease.server.service;

import com.learnease.server.dto.course.CourseResponseDto;
import com.learnease.server.dto.course.DashboardCoursesResponseDto;

import java.util.List;
import java.util.UUID;

public interface CourseService {

    CourseResponseDto getCourseById(UUID courseId);

    List<DashboardCoursesResponseDto> getAllCourses();
}

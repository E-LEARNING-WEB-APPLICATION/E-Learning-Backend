package com.learnease.server.service;

import com.learnease.server.dto.ApiResponse;
import com.learnease.server.dto.course.CourseResponseDto;
import com.learnease.server.dto.course.DashboardCoursesResponseDto;
import com.learnease.server.dto.course.EnrolledCourseResponseDto;

import java.util.List;
import java.util.UUID;

public interface CourseService {

    CourseResponseDto getCourseById(UUID courseId);

    List<DashboardCoursesResponseDto> getAllCourses();

    List<DashboardCoursesResponseDto> getCategoryCourses(UUID categoryId);

    ApiResponse getCoursePaymentStatus(UUID courseId, UUID authId);

    public List<EnrolledCourseResponseDto> getMyEnrolledCourses(UUID authId);
    long getCourseCount();
}

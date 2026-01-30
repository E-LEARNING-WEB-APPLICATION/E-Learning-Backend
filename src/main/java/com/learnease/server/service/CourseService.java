package com.learnease.server.service;

import com.learnease.server.dto.ApiResponse;
import com.learnease.server.dto.course.CourseResponseDto;
import com.learnease.server.dto.course.DashboardCourseFilterDto;
import com.learnease.server.dto.course.DashboardCoursesResponseDto;
import com.learnease.server.dto.course.EnrolledCourseResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface CourseService {

    CourseResponseDto getCourseById(UUID courseId);

    List<DashboardCoursesResponseDto> getAllCourses();

    List<DashboardCoursesResponseDto> getCategoryCourses(UUID categoryId);

    ApiResponse getCoursePaymentStatus(UUID courseId, UUID authId);

    List<EnrolledCourseResponseDto> getMyEnrolledCourses(UUID authId);
    long getCourseCount();

    Page<DashboardCoursesResponseDto> getDashboardCourses(DashboardCourseFilterDto filter, Pageable pageable);
}

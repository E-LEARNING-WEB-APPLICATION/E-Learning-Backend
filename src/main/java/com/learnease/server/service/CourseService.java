package com.learnease.server.service;

import com.learnease.server.dto.course.CourseResponseDto;

import java.util.UUID;

public interface CourseService {

    CourseResponseDto getCourseById(UUID courseId);
}

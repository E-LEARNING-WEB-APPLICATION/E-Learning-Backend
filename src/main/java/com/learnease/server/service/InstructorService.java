package com.learnease.server.service;

import com.learnease.server.dto.ApiResponse;
import com.learnease.server.dto.CourseInstructorResponseDto;
import com.learnease.server.dto.CoursesDto;
import com.learnease.server.dto.JWTDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;


public interface InstructorService {

    ApiResponse addCourse(String courseName, String courseDesc, double fees, int discountPercentage, int hour, UUID categoryId, MultipartFile image,
                          MultipartFile video,
                          JWTDTO user);

    List<CoursesDto> getAllCourses(JWTDTO user);

    public CourseInstructorResponseDto getInstructorById(UUID instructorId);
}

package com.learnease.server.service;

import com.learnease.server.dto.ApiResponse;
import com.learnease.server.dto.CourseInstructorResponseDto;
import com.learnease.server.dto.CoursesDto;
import com.learnease.server.dto.JWTDTO;
import com.learnease.server.dto.course.AddSectionReqDto;
import com.learnease.server.dto.course.AddTopicReqDto;
import com.learnease.server.dto.course.ShowSectionsResDto;
import com.learnease.server.dto.course.TopicResponseDto;
import com.learnease.server.dto.instructor.DashboardInstructorResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;


public interface InstructorService {

    ApiResponse addCourse(String courseName, String courseDesc, double fees, int discountPercentage, int hour, UUID categoryId, MultipartFile image,
                          MultipartFile video,
                          JWTDTO user);

    List<CoursesDto> getAllCourses(JWTDTO user);

    public CourseInstructorResponseDto getInstructorById(UUID instructorId);

    List<DashboardInstructorResponseDto> getAllInstructors();

    ApiResponse addSection(UUID userId, AddSectionReqDto addSectionReqDto);

    List<ShowSectionsResDto> getAllSections(UUID courseId,UUID userId);

    ApiResponse addTopic(UUID userId, AddTopicReqDto addTopicReqDto);

    List<TopicResponseDto> getTopics(UUID sectionId, UUID userId);
}

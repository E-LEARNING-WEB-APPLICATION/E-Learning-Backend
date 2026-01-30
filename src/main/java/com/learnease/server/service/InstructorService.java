package com.learnease.server.service;

import com.learnease.server.dto.ApiResponse;
import com.learnease.server.dto.CourseInstructorResponseDto;
import com.learnease.server.dto.CoursesDto;
import com.learnease.server.dto.JWTDTO;
import com.learnease.server.dto.course.*;
import com.learnease.server.dto.instructor.DashboardInstructorResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;


public interface InstructorService {

    ApiResponse addCourse(String courseName, String courseDesc, double fees, int discountPercentage, int hour, UUID categoryId, MultipartFile image,
                          MultipartFile video,
                          JWTDTO user);

    List<CoursesDto> getAllCourses(JWTDTO user);

    CourseInstructorResponseDto getInstructorById(UUID instructorId);

    List<DashboardInstructorResponseDto> getAllInstructors();

    ApiResponse addSection(UUID userId, AddSectionReqDto addSectionReqDto);

    List<ShowSectionsResDto> getAllSections(UUID courseId,UUID userId);

    ApiResponse addTopic(UUID userId, AddTopicReqDto addTopicReqDto);

    ApiResponse updateTopic(UUID userId, UpdateTopicReqDto updateTopicReqDto);


    List<TopicResponseDto> getTopics(UUID sectionId, UUID userId);

    ApiResponse updateCourse(UUID courseId, String courseName, String courseDesc, double fees, int discountPercentage, int hour, UUID categoryId, MultipartFile image, MultipartFile video, JWTDTO user);

    SectionDto getSection(UUID sectionId);

    ApiResponse updateSection(UUID userId, UUID sectionId, AddSectionReqDto addSectionReqDto);

    TopicResponseDto getTopic(UUID topicId);

    ApiResponse withdrawMoney(UUID authId);

    List<CourseStudentDto> getCoursesData(UUID userId);

    List<StudentListDto> getStudentsList(UUID userId,UUID courseId) ;
}

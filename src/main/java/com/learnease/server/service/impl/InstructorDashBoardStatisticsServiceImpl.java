package com.learnease.server.service.impl;

import com.learnease.server.dto.instructor.instructorDashboard.CourseStudentCountDto;
import com.learnease.server.exception.custom_exception.UserNotFoundException;
import com.learnease.server.model.Instructor;
import com.learnease.server.repository.InstructorRepository;
import com.learnease.server.service.InstructorDashBoardStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InstructorDashBoardStatisticsServiceImpl implements InstructorDashBoardStatisticsService {

    private final InstructorRepository instructorRepository;

    @Override
    public List<CourseStudentCountDto> getStudentPerCourses(UUID authId) {

        Instructor instructor = instructorRepository.findByUserDetails_UserAuth_Id(authId)
                .orElseThrow(()->new UserNotFoundException("No such Instructor Exist"));

        return instructor.getCourses()
                .stream()
                .map((c) -> new CourseStudentCountDto(c.getTitle(), c.getStudents().size()))
                .toList();
    }
}

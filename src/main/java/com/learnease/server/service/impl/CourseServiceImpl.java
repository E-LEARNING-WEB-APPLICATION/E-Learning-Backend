package com.learnease.server.service.impl;

import com.learnease.server.dto.course.CourseResponseDto;
import com.learnease.server.exception.custom_exception.CourseNotFoundException;
import com.learnease.server.model.Course;
import com.learnease.server.repository.CourseRepository;
import com.learnease.server.repository.FeedbackRepository;
import com.learnease.server.service.CourseService;
import com.learnease.server.util.mappers.CourseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final FeedbackRepository feedbackRepository;
    private final CourseMapper courseMapper;

    @Override
    public CourseResponseDto getCourseById(UUID courseId) {

        Course course = courseRepository.findCourseGraphById(courseId)
                .orElseThrow(() -> new CourseNotFoundException(courseId));

        Object result = feedbackRepository.findRatingSummaryByCourseId(courseId);

        Object[] ratingSummary = (Object[]) result;

        Double rating = (Double) ratingSummary[0];
        Long numberOfReviews = (Long) ratingSummary[1];

        return courseMapper.toCourseResponseDto(course , rating , numberOfReviews);
    }
}

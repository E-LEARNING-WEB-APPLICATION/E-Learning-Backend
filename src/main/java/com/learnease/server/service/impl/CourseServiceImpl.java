package com.learnease.server.service.impl;

import com.learnease.server.dto.ApiResponse;
import com.learnease.server.dto.course.CourseResponseDto;
import com.learnease.server.dto.course.DashboardCoursesResponseDto;
import com.learnease.server.exception.custom_exception.CourseNotFoundException;
import com.learnease.server.exception.custom_exception.UserNotFoundException;
import com.learnease.server.model.Course;
import com.learnease.server.model.Student;
import com.learnease.server.model.UserDetails;
import com.learnease.server.repository.CourseRepository;
import com.learnease.server.repository.FeedbackRepository;
import com.learnease.server.repository.StudentRepository;
import com.learnease.server.repository.UserDetailRepository;
import com.learnease.server.service.CourseService;
import com.learnease.server.util.mappers.CourseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final StudentRepository studentRepository;
    private final UserDetailRepository userDetailRepository;
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

    @Override
    public List<DashboardCoursesResponseDto> getAllCourses() {
        return courseRepository.findDashboardCourses()
                .stream()
                .map(p -> new DashboardCoursesResponseDto(
                        p.getId(),
                        p.getCategoryId(),
                        p.getThumbnail(),
                        p.getTitle(),
                        p.getFees(),
                        p.getRating(),
                        p.getReviews(),
                        p.getDuration(),
                        p.getDiscount()
                ))
                .toList();
    }

    @Override
    public List<DashboardCoursesResponseDto> getCategoryCourses(UUID categoryId) {
        return courseRepository.findDashboardCoursesByCategoryId(categoryId)
                .stream()
                .map(p -> new DashboardCoursesResponseDto(
                        p.getId(),
                        p.getCategoryId(),
                        p.getThumbnail(),
                        p.getTitle(),
                        p.getFees(),
                        p.getRating(),
                        p.getReviews(),
                        p.getDuration(),
                        p.getDiscount()
                ))
                .toList();
    }

    @Override
    public ApiResponse getCoursePaymentStatus(UUID courseId, UUID authId) {

        UserDetails userDetails = userDetailRepository.findByUserAuth_Id(authId)
                .orElseThrow(() -> new UserNotFoundException("No Such User Exist"));
        Student student = studentRepository.findByUserDetails_Id(userDetails.getId())
                .orElseThrow(() -> new UserNotFoundException("No Such Student Exist"));

        for (Course course : student.getCourses()){
            if (course.getId() == courseId){
                return new ApiResponse(true,"Course is assessable");
            }
        }
        return new ApiResponse(false,"course is not assessable");
    }
}

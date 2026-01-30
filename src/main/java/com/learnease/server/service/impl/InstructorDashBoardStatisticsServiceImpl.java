package com.learnease.server.service.impl;

import com.learnease.server.dto.instructor.instructorDashboard.CategoryCoursesCountDto;
import com.learnease.server.dto.instructor.instructorDashboard.CourseStudentCountDto;
import com.learnease.server.dto.instructor.instructorDashboard.DashboardStatisticsDto;
import com.learnease.server.exception.custom_exception.UserNotFoundException;
import com.learnease.server.model.Instructor;
import com.learnease.server.model.Student;
import com.learnease.server.model.enums.BookingStatus;
import com.learnease.server.repository.*;
import com.learnease.server.service.InstructorDashBoardStatisticsService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class InstructorDashBoardStatisticsServiceImpl implements InstructorDashBoardStatisticsService {

    private final InstructorRepository instructorRepository;
    private final CourseRepository courseRepository;
    private final FeedbackRepository feedbackRepository;
    private final WalletTransactionRepository walletTransactionRepository;
    private final BookingRepository bookingRepository;

    @Override
    public List<CourseStudentCountDto> getStudentPerCourses(UUID authId) {

        Instructor instructor = instructorRepository.findByUserDetails_UserAuth_Id(authId)
                .orElseThrow(()->new UserNotFoundException("No such Instructor Exist"));

        return instructor.getCourses()
                .stream()
                .map((c) -> new CourseStudentCountDto(c.getTitle(), c.getStudents().size()))
                .toList();
    }

    @Override
    public List<CategoryCoursesCountDto> getCoursesPerCategory(UUID authId) {

        Instructor instructor = instructorRepository.findByUserDetails_UserAuth_Id(authId)
                .orElseThrow(()->new UserNotFoundException("No such Instructor Exist"));

        return courseRepository.findCourseCountByCategory(instructor.getId())
                .stream()
                .map((c)->new CategoryCoursesCountDto(c.getCategory(),c.getCourseCount()))
                .toList();
    }

    @Override
    public DashboardStatisticsDto getOverAllStat(UUID authId) {
        Instructor instructor = instructorRepository.findByUserDetails_UserAuth_Id(authId)
                .orElseThrow(()->new UserNotFoundException("No such Instructor Exist"));

        DashboardStatisticsDto dashboardStatisticsDto = new DashboardStatisticsDto();

        //to get total no of unique students
        //flatmap is used to return a stream
        dashboardStatisticsDto.setTotalStudents(instructor.getCourses().stream()
                .flatMap(course -> course.getStudents().stream())
                .map(Student::getId)
                .collect(Collectors.toSet())
                .size());

        //to get total course of the instructor
        dashboardStatisticsDto.setTotalCourse(instructor.getCourses().size());

        //to get total no of enrollments i.e. non-unique students
        dashboardStatisticsDto.setTotalEnrollments(instructor.getCourses().stream()
                .mapToInt(c->c.getStudents().size())
                .sum());

        //to get the feedback review average rating
        dashboardStatisticsDto.setAverageRating(feedbackRepository.findRatingSummaryByInstructorId(instructor.getId()));

        //to get the total no of review
        dashboardStatisticsDto.setTotalReviews(feedbackRepository.findTotalRatingByInstructorId(instructor.getId()));

        //to get the Last month revenue
        LocalDateTime startDate = LocalDateTime.now().minusDays(30);
        dashboardStatisticsDto.setLastMonthRevenue(walletTransactionRepository.findTotalRevenueLast30Days(instructor.getId(),startDate));

        //to get the total  revenue
        dashboardStatisticsDto.setTotalRevenue(walletTransactionRepository.findTotalRevenueByInstructorId(instructor.getId()));

        // to get new student register for the instructor for the first time
        LocalDateTime startDate1 = LocalDateTime.now().minusDays(7);
        dashboardStatisticsDto.setNewStudent(bookingRepository.countTrulyNewStudentsForInstructorSince(instructor.getId(),BookingStatus.PAID, startDate1));

        //to get the  amount of money that is available for withdraw
        dashboardStatisticsDto.setAvailableAmount(walletTransactionRepository.getAvailableBalance(instructor.getId()));

        return dashboardStatisticsDto;

    }
}

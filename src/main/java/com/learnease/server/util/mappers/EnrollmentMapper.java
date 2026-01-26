package com.learnease.server.util.mappers;

import com.learnease.server.dto.course.EnrolledCourseResponseDto;
import com.learnease.server.model.Booking;
import com.learnease.server.model.Course;
import org.springframework.stereotype.Component;

@Component
public class EnrollmentMapper {

    public EnrolledCourseResponseDto toDto(Booking booking) {

        Course course = booking.getPurchasedCourse();

        EnrolledCourseResponseDto dto = new EnrolledCourseResponseDto();

        dto.setBookingId(booking.getId());
        dto.setEnrolledAt(booking.getPaidAt());

        dto.setCourseId(course.getId());
        dto.setCourseTitle(course.getTitle());
        dto.setCourseThumbnail(course.getThumbnail());

        dto.setCategoryName(course.getCategory() != null ? course.getCategory().getTitle() : null);
        dto.setCourseDuration(formatDuration(course.getHour()));

        dto.setCoursePrice(booking.getCoursePriceSnapShot());

        dto.setRating(4.5);

        return dto;
    }

    private String formatDuration(int hours) {
        double months = hours / 30.0;
        return months < 1
                ? hours + " hours"
                : String.format("%.1f months", months);
    }
}

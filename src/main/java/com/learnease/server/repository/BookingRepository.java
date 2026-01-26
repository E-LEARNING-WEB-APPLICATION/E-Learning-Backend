package com.learnease.server.repository;

import com.learnease.server.dto.admin.*;
import com.learnease.server.model.Booking;
import com.learnease.server.model.Course;
import com.learnease.server.model.Student;
import com.learnease.server.model.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking , UUID> {

    Optional<Booking> findByStudentAndPurchasedCourse(
            Student student,
            Course purchasedCourse
    );

    @Query("""
        SELECT new com.learnease.server.dto.admin.MonthlyRevenueDTO(
            YEAR(b.paidAt),
            MONTH(b.paidAt),
            SUM(b.pricePaid)
        )
        FROM Booking b
        WHERE b.paidAt >= :startDate
          AND b.status = :status
        GROUP BY YEAR(b.paidAt), MONTH(b.paidAt)
        ORDER BY YEAR(b.paidAt), MONTH(b.paidAt)
    """)
    List<MonthlyRevenueDTO> findMonthlyRevenue(
            @Param("startDate") LocalDateTime startDate,
            @Param("status") BookingStatus status
    );

    @Query("""
        SELECT new com.learnease.server.dto.admin.MonthlyRevenueDTO(
            YEAR(b.paidAt),
            MONTH(b.paidAt),
            SUM(b.pricePaid)
        )
        FROM Booking b
        WHERE b.paidAt >= :startDate
          AND b.instructor.id = :instructor_id
          AND b.status = :status
        GROUP BY YEAR(b.paidAt), MONTH(b.paidAt)
        ORDER BY YEAR(b.paidAt), MONTH(b.paidAt)
    """)
    List<MonthlyRevenueDTO> findMonthlyRevenueByInstructor(
            @Param("startDate") LocalDateTime startDate,
            @Param("instructor_id") UUID instructorId,
            @Param("status") BookingStatus status
    );

    @Query("""
        SELECT new com.learnease.server.dto.admin.InstructorMonthlyRevenueDTO(
            b.instructor.id,
            CONCAT(b.instructor.userDetails.firstName," ", b.instructor.userDetails.lastName) ,
            SUM(b.pricePaid)
        )
        FROM Booking b
        WHERE b.status = :status
        GROUP BY b.instructor
        ORDER BY SUM(b.pricePaid) DESC
        LIMIT :top
    """)
    List<InstructorMonthlyRevenueDTO> findTopInstructorsByRevenue(
            @Param("status") BookingStatus status, @Param("top") int top
    );

    @Query("""
        SELECT new com.learnease.server.dto.admin.InstructorEnrollmentsDTO(
            b.instructor.id,
            CONCAT(b.instructor.userDetails.firstName," ", b.instructor.userDetails.lastName) ,
            COUNT (b)
        )
        FROM Booking b
        WHERE b.status = :status
        GROUP BY b.instructor
        ORDER BY COUNT (b) DESC
        LIMIT :top
    """)
    List<InstructorEnrollmentsDTO> findTopInstructorsByEnrollments(
            @Param("status") BookingStatus status, @Param("top") int top
    );

    @Query("""
        SELECT COALESCE(SUM(b.pricePaid), 0)
        FROM Booking b
        WHERE b.paidAt BETWEEN :startDate AND :endDate
          AND b.status = :status
    """)
    BigDecimal sumRevenueBetweenDates(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("status") BookingStatus status
    );

    @Query("""
        SELECT new com.learnease.server.dto.admin.CourseEnrollmentDTO(
            b.purchasedCourse.id,
            b.purchasedCourse.title,
            CONCAT (b.instructor.userDetails.firstName," ", b.instructor.userDetails.lastName),
            COUNT (b),
            AVG (f.rating)
        )
        FROM Booking b
        JOIN Feedback f on f.course = b.purchasedCourse
        GROUP BY b.purchasedCourse
        ORDER BY COUNT(b) DESC
        LIMIT :top
        """)
    List<CourseEnrollmentDTO> findTopCoursesByEnrollments(@Param("top") int top);

    List<Booking> findByStudentAndStatusOrderByPaidAtDesc(
            Student student,
            BookingStatus status
    );



}

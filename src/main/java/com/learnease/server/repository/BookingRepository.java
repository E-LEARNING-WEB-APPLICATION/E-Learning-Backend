package com.learnease.server.repository;

import com.learnease.server.dto.admin.*;
import com.learnease.server.model.Booking;
import com.learnease.server.model.Course;
import com.learnease.server.model.Student;
import com.learnease.server.model.enums.BookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    select COALESCE(sum(b.pricePaid), 0)  from Booking b
""")
    BigDecimal findSumPricePaid();

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
        CONCAT(
            b.purchasedCourse.instructor.userDetails.firstName, ' ',
            b.purchasedCourse.instructor.userDetails.lastName
        ),
        COUNT(b),
        COALESCE(CAST(AVG(f.rating) AS double), 0.0)
    )
    FROM Booking b
    LEFT JOIN Feedback f ON f.course = b.purchasedCourse
    GROUP BY b.purchasedCourse.id,
             b.purchasedCourse.title,
             b.purchasedCourse.instructor.userDetails.firstName,
             b.purchasedCourse.instructor.userDetails.lastName
    ORDER BY COUNT(b) DESC
""")
    List<CourseEnrollmentDTO> findTopCoursesByEnrollments(Pageable pageable);

    List<Booking> findByStudentAndStatusOrderByPaidAtDesc(
            Student student,
            BookingStatus status
    );



    @Query(value = """
 SELECT *
                FROM (
                    SELECT
                        BIN_TO_UUID(i.instructor_id) AS instructorId,
                        CONCAT(ud.first_name, ' ', ud.last_name) AS instructorName,
                        COALESCE(SUM(b.price_paid), 0) AS totalRevenue,
                        COUNT(DISTINCT b.booking_id) AS totalEnrollments,
                        COALESCE(AVG(f.rating), 0) AS avgCourseRating,
                        COUNT(DISTINCT c.course_id) AS totalCourses,
                        RANK() OVER (ORDER BY SUM(b.price_paid) DESC) AS `rankRevenue`,
                        RANK() OVER (ORDER BY COUNT(DISTINCT b.booking_id) DESC) AS `rankEnrollments`
                    FROM instructor i
                    JOIN user_details ud
                        ON i.user_id = ud.user_id
                    JOIN user_auth ua
                        ON ud.auth_id = ua.auth_id
                        AND ua.status = 'ACTIVE'
                    LEFT JOIN course c
                        ON c.instructor_id = i.instructor_id
                    LEFT JOIN feedback f
                        ON c.course_id = f.course_id
                    LEFT JOIN booking b
                        ON b.course_id = c.course_id
                        AND b.status = :status
                    GROUP BY i.instructor_id, ud.first_name, ud.last_name
                ) t
                ORDER BY
                    CASE
                        WHEN :sortBy = 'REVENUE' THEN t.totalRevenue
                        WHEN :sortBy = 'ENROLLMENTS' THEN t.totalEnrollments
                    END DESC
                LIMIT :limit
                
""", nativeQuery = true)
    List<InstructorLeaderboardDTO> findTopInstructors(
            @Param("status") String status,
            @Param("sortBy") String sortBy,
            @Param("limit") int limit
    );


    @Query("""
        SELECT new com.learnease.server.dto.admin.EnrolledStudentAdminDTO(
            b.id,
            b.status,
            b.purchaseTime,
            b.paidAt,

            c.id,
            c.title,

            s.id,
            ud.id,
            CONCAT(ud.firstName, ' ', ud.lastName),
            ua.email,
            ud.phoneNo,

            i.id,
            CONCAT(iud.firstName, ' ', iud.lastName),

            b.pricePaid,
            b.currency,
            b.paymentMethod
        )
        FROM Booking b
        JOIN b.purchasedCourse c
        JOIN b.student s
        JOIN s.userDetails ud
        JOIN ud.userAuth ua
        JOIN b.instructor i
        JOIN i.userDetails iud
        WHERE c.id = :courseId
        ORDER BY b.purchaseTime DESC
    """)
    Page<EnrolledStudentAdminDTO> findEnrolledStudentsByCourse(
            @Param("courseId") UUID courseId,
            Pageable pageable
    );

    @Query("""
    SELECT new com.learnease.server.dto.admin.MonthlyStudentEnrollmentDTO(
        YEAR(b.createdAt),
        MONTH(b.createdAt),
        COUNT(b.id)
    )
    FROM Booking b
    JOIN b.purchasedCourse c
    WHERE b.createdAt >= :startDate
    AND c.id = :courseId
    GROUP BY YEAR(b.createdAt), MONTH(b.createdAt)
    ORDER BY YEAR(b.createdAt), MONTH(b.createdAt)
    """)
    List<MonthlyStudentEnrollmentDTO> findCourseStudentEnrollmentByMonth(
            @Param("startDate") LocalDateTime startDate,
            @Param("courseId") UUID courseId);
}

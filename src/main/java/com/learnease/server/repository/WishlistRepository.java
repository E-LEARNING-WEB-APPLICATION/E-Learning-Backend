package com.learnease.server.repository;

import com.learnease.server.dto.admin.CourseConversionDTO;
import com.learnease.server.model.Course;
import com.learnease.server.model.Student;
import com.learnease.server.model.Wishlist;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface WishlistRepository extends JpaRepository<Wishlist , UUID> {

    boolean existsByStudentAndCourse(Student student, Course course);

    List<Wishlist> findByStudent(Student student);

    long countByStudent(Student student);

    void deleteByStudentAndCourse(Student student, Course course);

    @Query("""
    SELECT (COUNT(DISTINCT b.id) * 100.0) / NULLIF(COUNT(DISTINCT w.id), 0)
    FROM Wishlist w
    LEFT JOIN Booking b ON w.student.id = b.student.id
        AND w.course.id = b.purchasedCourse.id
        AND b.status = com.learnease.server.model.enums.BookingStatus.PAID
    """)
    Double findOverallWishlistToBookingConversionRate();

    @Query("""
    SELECT (COUNT(DISTINCT b.id) * 100.0) / NULLIF(COUNT(DISTINCT w.id), 0)
    FROM Wishlist w
    LEFT JOIN Booking b ON w.student.id = b.student.id
        AND w.course.id = b.purchasedCourse.id
        AND b.status = com.learnease.server.model.enums.BookingStatus.PAID
    WHERE w.course.id = :courseId
    """)
    Double findOverallWishlistToBookingConversionRateByCourse(@Param("courseId") UUID courseId);

    @Query("""
    SELECT new com.learnease.server.dto.admin.CourseConversionDTO(
        c.id,
        c.title,
        (COUNT(DISTINCT b.id) * 100.0) / NULLIF(COUNT(DISTINCT w.id), 0)
    )
    FROM Course c
    JOIN Wishlist w ON w.course.id = c.id
    LEFT JOIN Booking b ON b.purchasedCourse.id = c.id 
        AND b.student.id = w.student.id
        AND b.status = com.learnease.server.model.enums.BookingStatus.PAID
    GROUP BY c.id, c.title
    ORDER BY (COUNT(DISTINCT b.id) * 100.0) / NULLIF(COUNT(DISTINCT w.id), 0) DESC
    """)
    List<CourseConversionDTO> findCourseRankingByConversionRate(Pageable pageable);
}

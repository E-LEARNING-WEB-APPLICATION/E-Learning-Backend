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

    List<Wishlist> findByStudentAndIsPurchasedFalse(Student student);

    long countByStudentAndIsPurchasedFalse(Student student);

    void deleteByStudentAndCourse(Student student, Course course);

    Wishlist findByStudentAndCourse(Student student, Course course);

    @Query("""
    SELECT (COUNT(f) FILTER (WHERE f.isPurchased = true) * 100.0) / NULLIF(COUNT(f), 0)
    FROM Wishlist f
    """)
    Double findOverallWishlistToBookingConversionRate();

    @Query("""
    SELECT (SUM(CASE WHEN w.isPurchased = true THEN 1.0 ELSE 0.0 END) * 100.0) /
           NULLIF(COUNT(w), 0)
    FROM Wishlist w
    WHERE w.course.id = :courseId
    """)
    Double findOverallWishlistToBookingConversionRateByCourse(@Param("courseId") UUID courseId);

    @Query("""
    SELECT new com.learnease.server.dto.admin.CourseConversionDTO(
        c.id,
        c.title,
        (SUM(CASE WHEN w.isPurchased = true THEN 1.0 ELSE 0.0 END) * 100.0) / COUNT(w)
    )
    FROM Wishlist w
    JOIN w.course c
    GROUP BY c.id, c.title
    ORDER BY (SUM(CASE WHEN w.isPurchased = true THEN 1.0 ELSE 0.0 END) * 100.0) / COUNT(w) DESC
    """)
    List<CourseConversionDTO> findCourseRankingByConversionRate(Pageable pageable);
}

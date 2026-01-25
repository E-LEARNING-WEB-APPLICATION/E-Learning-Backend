package com.learnease.server.repository;

import com.learnease.server.dto.admin.CourseRatingDTO;
import com.learnease.server.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface FeedbackRepository extends JpaRepository<Feedback , UUID> {

    @Query("""
           SELECT
                COALESCE(AVG(f.rating) , 0),
                COUNT(f.id)
           FROM Feedback f
           WHERE f.course.id = :courseId
    """)
    Object findRatingSummaryByCourseId(@Param("courseId") UUID courseId);

    @Query("""
        SELECT new com.learnease.server.dto.admin.CourseRatingDTO(
            f.course.id,
            f.course.title,
            AVG(f.rating)
        )
        FROM Feedback f
        GROUP BY f.course
        ORDER BY AVG(f.rating) DESC
        LIMIT :top
        """)
    List<CourseRatingDTO> findTopCoursesByRating(@Param("top") int top);
}

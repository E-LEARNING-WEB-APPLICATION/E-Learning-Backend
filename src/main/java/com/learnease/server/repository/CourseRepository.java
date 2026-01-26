package com.learnease.server.repository;

import com.learnease.server.dto.CoursesDto;
import com.learnease.server.model.Course;
import com.learnease.server.model.Instructor;
import com.learnease.server.projection.course.DashboardCoursesProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CourseRepository extends JpaRepository<Course, UUID> {

    @Query("""
        SELECT new com.learnease.server.dto.CoursesDto(
            c.title,
            c.description,
            c.fees,
            c.discount,
            c.thumbnail,
            c.introVideo,
            c.hour
        )
        FROM Course c
        WHERE c.instructor.id = :instructorId
    """)
    List<CoursesDto> findByInstructor(@Param("instructorId") UUID instructorId);

    //left join fetch bcz course may exist with sections and same goes for topics
    @Query("""
        SELECT DISTINCT c
        FROM Course c
        JOIN FETCH c.instructor
        JOIN FETCH c.category
        LEFT JOIN FETCH c.sections s
        WHERE c.id = :courseId
    """)

    Optional<Course> findCourseGraphById(@Param("courseId") UUID courseId);

    // Fetch dashboard-ready course data with average rating and total reviews using a native SQL query
    @Query(value = """
    SELECT
        BIN_TO_UUID(c.course_id) AS id,
        BIN_TO_UUID(c.category_id) AS categoryId,
        c.thumbnail AS thumbnail,
        c.title AS title,
        c.fees AS fees,
        COALESCE(AVG(f.rating), 0) AS rating,
        COUNT(f.feedback_id) AS reviews,
        c.hour AS duration,
        c.discount AS discount
    FROM course c
    LEFT JOIN feedback f ON f.course_id = c.course_id
    GROUP BY 
        c.course_id,
        c.category_id,
        c.thumbnail,
        c.title,
        c.fees,
        c.hour,
        c.discount
    """,
            nativeQuery = true)
    List<DashboardCoursesProjection> findDashboardCourses();

    // Fetch dashboard-ready course data by categoryId with average rating and total reviews
    @Query(value = """
    SELECT 
        BIN_TO_UUID(c.course_id) AS id,
        BIN_TO_UUID(c.category_id) AS categoryId,
        c.thumbnail AS thumbnail,
        c.title AS title,
        c.fees AS fees,
        COALESCE(AVG(f.rating), 0) AS rating,
        COUNT(f.feedback_id) AS reviews,
        c.hour AS duration,
        c.discount AS discount
    FROM course c
    LEFT JOIN feedback f ON f.course_id = c.course_id
    WHERE c.category_id = :categoryId
    GROUP BY 
        c.course_id,
        c.category_id,
        c.thumbnail,
        c.title,
        c.fees,
        c.hour,
        c.discount
    """,
            nativeQuery = true)
    List<DashboardCoursesProjection> findDashboardCoursesByCategoryId(
            @Param("categoryId") UUID categoryId
    );


}

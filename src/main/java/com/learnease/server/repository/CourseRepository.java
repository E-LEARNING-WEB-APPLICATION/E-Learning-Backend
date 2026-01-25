package com.learnease.server.repository;

import com.learnease.server.dto.CoursesDto;
import com.learnease.server.model.Course;
import com.learnease.server.model.Instructor;
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
            LEFT JOIN FETCH c.sections s
            WHERE c.id = :courseId
    """)
    Optional<Course> findCourseGraphById(@Param("courseId") UUID courseId);
}

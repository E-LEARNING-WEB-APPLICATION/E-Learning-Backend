package com.learnease.server.repository;

import com.learnease.server.dto.admin.AdminCourseOverviewDto;
import com.learnease.server.dto.admin.AdminCourseOverviewProjection;
import com.learnease.server.model.Course;
import com.learnease.server.model.enums.BookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface AdminCourseAnalyticsRepository extends JpaRepository<Course, UUID> {

    @Query(
            value = """

                    SELECT *
                                                FROM (
                                                    SELECT
                                                        BIN_TO_UUID(c.course_id) AS courseId,
                                                        c.title AS courseName,
                                                
                                                        BIN_TO_UUID(cat.category_id) AS categoryId,
                                                        cat.title AS categoryName,
                                                
                                                        c.fees AS price,
                                                        c.discount AS discount,
                                                        (c.fees - (c.fees * c.discount / 100)) AS effectivePrice,
                                                
                                                        COUNT(DISTINCT b.booking_id) AS totalEnrollments,
                                                
                                                        SUM(
                                                            CASE
                                                                WHEN b.paid_at >= :recentDate THEN 1
                                                                ELSE 0
                                                            END
                                                        ) AS recentEnrollments,
                                                
                                                        COALESCE(SUM(b.price_paid), 0) AS totalRevenue,
                                                        COALESCE(AVG(b.price_paid), 0) AS avgRevenuePerStudent,
                                                
                                                        COALESCE(AVG(f.rating), 0) AS avgRating,
                                                        COUNT(DISTINCT f.feedback_id) AS totalFeedbacks,
                                                
                                                        BIN_TO_UUID(i.instructor_id) AS instructorId,
                                                        CONCAT(ud.first_name, ' ', ud.last_name) AS instructorName,
                                                
                                                        c.hour AS durationInHours,
                                                
                                                        RANK() OVER (
                                                            ORDER BY COALESCE(SUM(b.price_paid), 0) DESC
                                                        ) AS revenueRank,
                                                
                                                        c.created_at AS createdAt
                                                    FROM course c
                                                    JOIN category cat
                                                        ON c.category_id = cat.category_id
                                                    JOIN instructor i
                                                        ON c.instructor_id = i.instructor_id
                                                    JOIN user_details ud
                                                        ON i.user_id = ud.user_id
                                                    LEFT JOIN booking b
                                                        ON b.course_id = c.course_id
                                                        AND b.status = :status
                                                    LEFT JOIN feedback f
                                                        ON f.course_id = c.course_id
                                                    GROUP BY
                                                        c.course_id,
                                                        cat.category_id,
                                                        i.instructor_id,
                                                        ud.first_name,
                                                        ud.last_name,
                                                        c.created_at
                                                ) t
                                                ORDER BY t.totalRevenue DESC
                                                
    """,
                    nativeQuery = true
    )

    Page<AdminCourseOverviewProjection> fetchCourseOverview(
            @Param("recentDate") LocalDateTime recentDate,
            @Param("status") String status,
            Pageable pageable
    );

}

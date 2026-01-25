package com.learnease.server.repository;

import com.learnease.server.dto.admin.MonthlyStudentEnrollmentDTO;
import com.learnease.server.model.Student;
import com.learnease.server.model.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentRepository extends JpaRepository<Student , UUID> {

    Optional<Student> findByUserDetails_Id(UUID userId);

    Optional<Student> findByUserDetails_UserAuth_Id(UUID authId);
    long countAllByCreatedAtAfterAndUserDetails_UserAuth_Status(LocalDateTime time, Status status);
    long countAllByUserDetails_UserAuth_LastLoginAtAfter(LocalDateTime afterTime);

    @Query("""
    SELECT new com.learnease.server.dto.admin.MonthlyStudentEnrollmentDTO(
        YEAR(s.createdAt),
        MONTH(s.createdAt),
        COUNT(s.id)
    )
    FROM Student s
    WHERE s.createdAt >= :startDate
    GROUP BY YEAR(s.createdAt), MONTH(s.createdAt)
    ORDER BY YEAR(s.createdAt), MONTH(s.createdAt)
    """)
    List<MonthlyStudentEnrollmentDTO> findMonthlyStudentEnrollments(
            @Param("startDate") LocalDateTime startDate
    );

}

package com.learnease.server.repository;

import com.learnease.server.dto.admin.InstructorSummaryDTO;
import com.learnease.server.model.Instructor;
import com.learnease.server.model.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InstructorRepository extends JpaRepository<Instructor , UUID> {


    Optional<Instructor> findByUserDetails_Id(UUID userId);
    Optional<Instructor> findByUserDetails_UserAuth_Id(UUID authId);

    List<Instructor> findInstructorByUserDetailsUserAuthStatus(Status status);
    long countAllByUserDetailsUserAuthStatus(Status status);

    @Query("""
            select new com.learnease.server.dto.admin.InstructorSummaryDTO(
            i.id,
            concat(concat(i.userDetails.firstName, " "), i.userDetails.lastName),
            i.userDetails.userAuth.email
            )
            from Instructor i
            where i.userDetails.userAuth.status= :status
            """)
    List<InstructorSummaryDTO> findAllInstructorSummary(@Param("status") Status status);
}

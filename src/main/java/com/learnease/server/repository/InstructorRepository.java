package com.learnease.server.repository;

import com.learnease.server.model.Instructor;
import com.learnease.server.model.Student;
import com.learnease.server.model.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InstructorRepository extends JpaRepository<Instructor , UUID> {


    Optional<Instructor> findByUserDetails_Id(UUID userId);
    Optional<Instructor> findByUserDetails_UserAuth_Id(UUID authId);

    public List<Instructor> findInstructorByUserDetailsUserAuthStatus(Status status);
    public long countAllByUserDetailsUserAuthStatus(Status status);
}

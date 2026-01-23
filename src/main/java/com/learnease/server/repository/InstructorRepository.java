package com.learnease.server.repository;

import com.learnease.server.model.Instructor;
import com.learnease.server.model.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface InstructorRepository extends JpaRepository<Instructor , UUID> {
    public List<Instructor> findInstructorByUserDetailsUserAuthStatus(Status status);
}

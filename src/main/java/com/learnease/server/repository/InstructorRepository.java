package com.learnease.server.repository;

import com.learnease.server.model.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InstructorRepository extends JpaRepository<Instructor , UUID> {
}

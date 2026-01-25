package com.learnease.server.repository;

import com.learnease.server.model.Specialization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpecializationRepository extends JpaRepository<Specialization , UUID> {
}

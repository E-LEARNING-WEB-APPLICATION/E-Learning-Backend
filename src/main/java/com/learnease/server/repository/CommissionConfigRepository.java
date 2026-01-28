package com.learnease.server.repository;

import com.learnease.server.model.CommissionConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface CommissionConfigRepository extends JpaRepository<CommissionConfig, UUID> {
    @Query("SELECT c FROM CommissionConfig c")
    Optional<CommissionConfig> findFirst();
}

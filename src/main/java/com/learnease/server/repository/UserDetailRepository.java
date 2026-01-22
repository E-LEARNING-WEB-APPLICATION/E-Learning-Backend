package com.learnease.server.repository;

import com.learnease.server.model.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserDetailRepository extends JpaRepository<UserDetails, UUID> {
    Optional<UserDetails> findByUserAuth_Id(UUID uuid);
}

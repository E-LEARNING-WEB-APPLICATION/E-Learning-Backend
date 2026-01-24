package com.learnease.server.repository;

import com.learnease.server.model.UserAuth;
import com.learnease.server.model.enums.Role;
import com.learnease.server.model.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserAuthRepository extends JpaRepository<UserAuth , UUID> {
    boolean existsByEmail(String email);
    Optional<UserAuth> findByEmail(String email);

    List<UserAuth> findByRole(Role role);
    UserAuth findByEmailAndStatusAndRole(String email, Status status, Role role);
}

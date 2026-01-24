package com.learnease.server.repository;

import com.learnease.server.model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SkillsRepository extends JpaRepository<Skill , UUID> {
}

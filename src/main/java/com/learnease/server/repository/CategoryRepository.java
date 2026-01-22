package com.learnease.server.repository;

import com.learnease.server.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
    @Query("""
        select c from Category c
        join c.keywords k
        where lower(k) = lower(:keyword)
        """)
    public List<Category> findByKeyword(String keyword);

    Optional<Category> findByTitle(String Title);
}

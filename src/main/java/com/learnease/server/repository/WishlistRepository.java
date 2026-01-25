package com.learnease.server.repository;

import com.learnease.server.model.Course;
import com.learnease.server.model.Student;
import com.learnease.server.model.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface WishlistRepository extends JpaRepository<Wishlist , UUID> {

    boolean existsByStudentAndCourse(Student student, Course course);

    List<Wishlist> findByStudent(Student student);

    long countByStudent(Student student);

    void deleteByStudentAndCourse(Student student, Course course);
}

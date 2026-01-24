package com.learnease.server.repository;

import com.learnease.server.model.Booking;
import com.learnease.server.model.Course;
import com.learnease.server.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking , UUID> {

    Optional<Booking> findByStudentAndPurchasedCourse(
            Student student,
            Course purchasedCourse
    );

}

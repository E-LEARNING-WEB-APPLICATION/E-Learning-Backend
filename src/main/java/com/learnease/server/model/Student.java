package com.learnease.server.model;


import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


//CREATE TABLE student_details (
//        student_id INT PRIMARY KEY AUTO_INCREMENT,
//        profile_id INT UNIQUE NOT NULL,
//        total_courses_enrolled INT DEFAULT 0,
//        created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
//        FOREIGN KEY (profile_id) REFERENCES user_profile(profile_id)
//        );

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@AttributeOverride(name = "id" , column = @Column(name = "student_id"))
public class Student extends BaseEntity{

    @OneToOne(cascade = CascadeType.PERSIST) //to protect multiple db save method whenever object is created then it gets persists at that time only
    @JoinColumn(name = "user_id" , nullable = false)
    private UserDetails userDetails;

    @Min(value = 0, message = "Total enrolled courses cannot be negative")
    @Column(columnDefinition = "int default 0")
    private int totalEnrolledCourses;

    @PastOrPresent(message = "Last login time cannot be in the future")
    private LocalDateTime lastLoginAt;

    @Valid
    @ManyToMany
    @JoinTable(name = "student_skills",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private Set<Skill> skills = new HashSet<>();


    @Valid
    @ManyToMany
    @JoinTable(name = "student_course",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private Set<Course> courses = new HashSet<>();
}

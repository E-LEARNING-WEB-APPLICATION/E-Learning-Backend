package com.learnease.server.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@AttributeOverride(name = "id", column = @Column(name = "instructor_id"))
public class Instructor extends BaseEntity{

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id" , nullable = false)
    private UserDetails userDetails;

    private String experience;
    private double rating;
    private String gitHubUrl;
    private String linkedInUrl;
    private String twitterUrl;
    private double balance;
    @ManyToOne
    private Admin processedBy;
    private LocalDateTime processedAt;

    @Size(max = 500, message = "Bio cannot exceed 500 characters")
    private String bio;

    @ManyToMany
    @JoinTable(name = "instructor_specialization",
            joinColumns = @JoinColumn(name = "instructor_id"),
            inverseJoinColumns = @JoinColumn(name = "specialization_id")
    )
    private Set<Specialization> specializations = new HashSet<>();

    @OneToMany(mappedBy = "instructor", cascade = CascadeType.ALL)
    private List<Course> courses = new ArrayList<>();


}

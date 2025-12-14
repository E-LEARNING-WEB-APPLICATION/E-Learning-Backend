package com.learnease.server.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @OneToOne
    @JoinColumn(name = "user_id" , nullable = false)
    private UserDetails userDetails;

    private int experience;
    private double rating;
    private String gitHubUrl;
    private String linkedInUrl;
    private String twitterUrl;
    private double balance;

    @ManyToMany
    @JoinTable(name = "instructor_specialization",
            joinColumns = @JoinColumn(name = "instructor_id"),
            inverseJoinColumns = @JoinColumn(name = "specialization_id")
    )
    private Set<Specialization> specializations = new HashSet<>();

    @OneToMany
    @JoinColumn(name = "instructor_id" , nullable = false)
    private List<Course> courses = new ArrayList<>();

}

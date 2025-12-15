package com.learnease.server.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
@AttributeOverride(name = "id", column = @Column(name = "course_id"))
public class Course extends BaseEntity{
    @NotNull
    private String title;
    @NotNull
    @Column(length = 500)
    private String description;
    @NotNull
    private double fees;
    @NotNull
    private int discount;
    @NotNull
    private String thumbnail;
    @NotNull
    private String introVideo;
    @NotNull
    private int hour;
    // This will create course_id as a foreign key in the section table
    @OneToMany
    @JoinColumn(name = "course_id")
    private List<Section> sections = new ArrayList<>();

    @ManyToMany(mappedBy = "courses")
    private Set<Student> students = new HashSet<>();
}

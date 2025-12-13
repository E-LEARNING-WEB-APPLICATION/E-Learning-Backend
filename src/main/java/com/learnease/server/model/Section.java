package com.learnease.server.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@AttributeOverride(name = "id", column = @Column(name = "section_id"))
public class Section extends BaseEntity{

    @NotNull
    private int sectionNumber;
    @NotNull
    private String title;
    @NotNull
    private String description;
    @OneToMany
    //this will create a section_id as a foreign key in topics table
    @JoinColumn(name = "section_id")
    private List<Topic> topics = new ArrayList<>();
}

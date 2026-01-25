package com.learnease.server.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@AttributeOverride(name = "id", column = @Column(name = "category_id"))
@Accessors(chain = true)
public class Category extends BaseEntity{

    @NotNull
    private String title;

    @NotNull
    @Column(length = 300)
    private String description;

    @ElementCollection
    @CollectionTable(
            name = "category_keywords",
            joinColumns = @JoinColumn(name = "category_id")
    )
    @Column(name = "keyword", length = 50)
    private Set<String> keywords = new HashSet<>();

    private String categoryImageUrl;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<Course> courses = new ArrayList<>();
}

package com.learnease.server.model;

import com.learnease.server.model.enums.ContentStatus;
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
@Table(
        name = "section",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"course_id", "section_number"})
        }
)
@AttributeOverride(name = "id", column = @Column(name = "section_id"))
public class Section extends BaseEntity{

    @NotNull
    private int sectionNumber;
    @NotNull
    private String title;
    @NotNull
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Topic> topics = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContentStatus status = ContentStatus.ACTIVE;
}

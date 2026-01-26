package com.learnease.server.model;


import com.learnease.server.model.enums.ContentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(
        name = "topic",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"section_id", "topic_number"})
        }
)
@AttributeOverride(name = "id", column = @Column(name = "topic_id"))
public class Topic extends BaseEntity{

    @NotNull
    private int topicNumber;
    @NotNull
    private String title;
    @Column(length = 400)
    @NotNull
    private String description;
    private int hour;
    private int min;
    @Column(length = 300)
    @NotNull
    private String video;
    @Column(length = 300)
    @NotNull
    private String notes;

    //bidirectional mapping
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id", nullable = false)
    private Section section;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContentStatus status = ContentStatus.ACTIVE;
}

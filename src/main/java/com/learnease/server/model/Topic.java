package com.learnease.server.model;


import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@AttributeOverride(name = "id", column = @Column(name = "topic_id"))
public class Topic extends BaseEntity{

    @NotNull
    private int topicNumber;
    @NotNull
    private String title;
    @Column(length = 400)
    @NotNull
    private String description;
    @Column(length = 300)
    @NotNull
    private String video;
    @Column(length = 300)
    @NotNull
    private String notes;


}

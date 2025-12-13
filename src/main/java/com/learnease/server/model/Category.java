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
@AttributeOverride(name = "id", column = @Column(name = "category_id"))
public class Category extends BaseEntity{

    @NotNull
    private String title;

    @NotNull
    @Column(length = 300)
    private String description;
}

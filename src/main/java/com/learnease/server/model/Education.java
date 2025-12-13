package com.learnease.server.model;



//CREATE TABLE education_details (
//        edu_id INT PRIMARY KEY AUTO_INCREMENT,
//        degree VARCHAR(100),
//        field_of_study VARCHAR(100),
//        institute VARCHAR(150),
//        grade VARCHAR(20),
//        description TEXT,
//        start_date DATE,
//        end_date DATE,
//        created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
//        FOREIGN KEY (profile_id) REFERENCES user_profile(profile_id)
//        );
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
@AttributeOverride(name = "id", column = @Column(name = "education_id"))
public class Education extends BaseEntity{

    @Column(length = 100)
    @NotNull
    private String degree;

    @NotNull
    private String fieldOfStudy;

    @NotNull
    private String institute;

    @NotNull
    private int passingYear;

}

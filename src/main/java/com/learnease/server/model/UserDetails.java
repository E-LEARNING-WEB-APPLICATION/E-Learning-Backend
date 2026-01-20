package com.learnease.server.model;


//CREATE TABLE user_profile (
//        profile_id INT PRIMARY KEY AUTO_INCREMENT,
//        auth_id INT UNIQUE NOT NULL,
//        fname VARCHAR(50),
//        lname VARCHAR(50),
//        dob DATE,
//        gender ENUM('male','female','other'),
//        profile_pic VARCHAR(255),
//        created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
//        FOREIGN KEY (auth_id) REFERENCES auth(auth_id)
//        );

import com.learnease.server.model.enums.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user_details")
@AttributeOverride(name = "id" , column = @Column(name = "user_id"))
@Accessors(chain = true)
public class UserDetails extends BaseEntity{

    private String firstName;
    private String lastName;
    private LocalDate dob;

    @Column(length = 500)
    private String bio;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @NotNull
    @Column(nullable = false)
    private String phoneNo;

    @Column(length = 300)
    private String profilePic;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "auth_id" , nullable = false)
    private UserAuth userAuth;

    //Here user_id will be created as a foreign key in the education table
    @OneToMany
    @JoinColumn(name = "user_id")
    private List<Education> educations = new ArrayList<>();

    @Embedded
    private Address address;
}

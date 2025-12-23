package com.learnease.server.model;


//CREATE TABLE user_details (
//        user_id BIGINT PRIMARY KEY AUTO_INCREMENT,
//        first_name VARCHAR(50) NOT NULL,
//        last_name VARCHAR(50) NOT NULL,
//        dob DATE NOT NULL,
//        bio VARCHAR(500),
//        gender ENUM('MALE', 'FEMALE', 'OTHER') NOT NULL,
//        phone_no VARCHAR(15) NOT NULL UNIQUE,
//        profile_pic VARCHAR(300),
//        auth_id BIGINT NOT NULL UNIQUE,
//        created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
//        updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
//        CONSTRAINT fk_user_auth
//        FOREIGN KEY (auth_id)
//        REFERENCES auth(auth_id)
//        ON DELETE CASCADE
//        );


import com.learnease.server.model.enums.Gender;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
public class UserDetails extends BaseEntity{

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be 2–50 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be 2–50 characters")
    private String lastName;

    @Past(message = "Date of birth must be in the past")
    private LocalDate dob;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Pattern(
            regexp = "^[0-9]{10}$",
            message = "Phone number must be a valid 10-digit number"
    )
    @Column(nullable = false , unique = true)
    private String phoneNo;

    @Size(max = 300, message = "Profile picture URL too long")
    private String profilePic;

    @NotNull(message = "Auth details are required")
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "auth_id" , nullable = false)
    private UserAuth userAuth;

    //Here user_id will be created as a foreign key in the education table
    @OneToMany
    @JoinColumn(name = "user_id")
    @Valid
    private List<Education> educations = new ArrayList<>();

    @Embedded
    @Valid
    private Address address;
}

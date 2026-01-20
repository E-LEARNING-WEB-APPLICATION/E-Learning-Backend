package com.learnease.server.model;



//CREATE TABLE auth (
//        auth_id INT PRIMARY KEY AUTO_INCREMENT,
//        email VARCHAR(255) UNIQUE NOT NULL,
//        password_hash VARCHAR(255) NOT NULL,
//        role ENUM('student','instructor','admin') NOT NULL,
//        status ENUM('active','inactive') DEFAULT 'active',
//        created_at DATETIME DEFAULT CURRENT_TIMESTAMP
//        );

import com.learnease.server.model.enums.Role;
import com.learnease.server.model.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@AttributeOverride(name = "id" , column = @Column(name = "auth_id"))
public class UserAuth extends BaseEntity{

    @Email
    @NotBlank
    @Column(length = 100 , unique = true)
    private String email;

    @NotBlank
    @Column(length = 300)
    private String password;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Role role;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Status status;

    @Override
    public String toString() {
        return "UserAuth{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", status=" + status +
                '}';
    }
}

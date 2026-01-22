package com.learnease.server.model;

import com.learnease.server.model.enums.Role;
import com.learnease.server.model.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@AttributeOverride(name = "id" , column = @Column(name = "auth_id"))
@Accessors(chain = true)
public class UserAuth extends BaseEntity implements UserDetails {

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
                ", role=" + role +
                ", UserId=" + getId() +
                '}';
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.role.name()));
    }

    @Override
    public String getUsername() {
        return this.email;
    }
}

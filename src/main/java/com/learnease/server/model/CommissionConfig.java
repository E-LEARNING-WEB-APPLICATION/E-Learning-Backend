package com.learnease.server.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@AttributeOverride(name = "id", column = @Column(name = "commission_id"))
@Accessors(chain = true)
public class CommissionConfig extends BaseEntity{

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "admin_id" , nullable = false)
    private Admin admin;
    @Column(name = "commission", nullable = false)
    private Double commission = 10.0;

}

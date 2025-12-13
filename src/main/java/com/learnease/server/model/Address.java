package com.learnease.server.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class Address {
    private String addressLine1;
    private String addressLine2;
    @Column(length = 50)
    private String city;
    @Column(length = 50)
    private String state;
    @Column(length = 10)
    private String pinCode;
    @Column(length = 50)
    private String country;
}

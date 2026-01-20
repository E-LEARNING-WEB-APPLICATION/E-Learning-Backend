package com.learnease.server.dto.auth;

public record AddressDto(
        String street,
        String city,
        String state,
        String country,
        String zipCode
) {
}

package com.learnease.server.dto.auth;

import com.learnease.server.model.Address;
import lombok.Builder;

@Builder
public record AddressDto(
        String street,
        String addressLine1,
        String city,
        String state,
        String country,
        String zipCode
) {
    public static AddressDto fromEntity(Address address) {
        if (address == null) return null;

        return AddressDto.builder()
                .street(address.getAddressLine1())
                .addressLine1(address.getAddressLine2())
                .city(address.getCity())
                .state(address.getState())
                .zipCode(address.getPinCode())
                .country(address.getCountry())
                .build();
    }
}

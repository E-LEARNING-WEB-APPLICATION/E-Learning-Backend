package com.learnease.server.util.mappers;

import com.learnease.server.dto.auth.AddressDto;
import com.learnease.server.model.Address;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    public Address toEntity(AddressDto dto) {
        if (dto == null) return null;

        Address address = new Address();
        address.setAddressLine1(dto.street());
        address.setCity(dto.city());
        address.setState(dto.state());
        address.setCountry(dto.country());
        address.setPinCode(dto.zipCode());
        return address;
    }
}

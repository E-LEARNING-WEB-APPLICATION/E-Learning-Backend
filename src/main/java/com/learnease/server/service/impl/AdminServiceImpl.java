package com.learnease.server.service.impl;

import com.learnease.server.dto.auth.AdminRegisterRequest;
import com.learnease.server.exception.custom_exception.BadClientRequestException;
import com.learnease.server.model.Admin;
import com.learnease.server.model.UserAuth;
import com.learnease.server.model.UserDetails;
import com.learnease.server.model.enums.Role;
import com.learnease.server.model.enums.Status;
import com.learnease.server.repository.AdminRepository;
import com.learnease.server.service.AdminService;
import com.learnease.server.util.mappers.AddressMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final AddressMapper addressMapper;

    @Override
    public Admin registerAdmin(UUID creatorAdminID, AdminRegisterRequest newUser) {
        UserAuth userAuth = new UserAuth()
                .setEmail(newUser.email())
                .setPassword(passwordEncoder.encode(newUser.password()))
                .setRole(Role.ADMIN)
                .setStatus(Status.ACTIVE);
        UserDetails userDetails = new UserDetails()
                .setUserAuth(userAuth)
                .setFirstName(newUser.firstName())
                .setLastName(newUser.lastName())
                .setDob(newUser.dob())
                .setGender(newUser.gender())
                .setPhoneNo(newUser.phoneNo())
                .setProfilePic(newUser.profilePic())
                .setAddress(addressMapper.toEntity(newUser.address()));


        Admin newAdmin = new Admin()
                .setUserDetails(userDetails)
                .setCreatedBy(adminRepository.findByUserDetailsUserAuthId(creatorAdminID).orElseThrow(
                        () -> new BadClientRequestException("creator admin cannot be null")));

        return adminRepository.save(newAdmin);
    }
}

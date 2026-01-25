package com.learnease.server.service.impl;

import com.learnease.server.dto.InstructorResponseDto;
import com.learnease.server.dto.auth.AdminRegisterRequest;
import com.learnease.server.exception.custom_exception.BadClientRequestException;
import com.learnease.server.model.Admin;
import com.learnease.server.model.Instructor;
import com.learnease.server.model.UserAuth;
import com.learnease.server.model.UserDetails;
import com.learnease.server.model.enums.Role;
import com.learnease.server.model.enums.Status;
import com.learnease.server.repository.*;
import com.learnease.server.service.AdminService;
import com.learnease.server.util.mappers.AddressMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final AddressMapper addressMapper;
    private final InstructorRepository instructorRepository;
    private final S3Service s3Service;
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;

    @Override
    public Admin registerAdmin(UUID creatorAdminID, AdminRegisterRequest newUser, MultipartFile profilePic) {
        try {
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
                    .setProfilePic(s3Service.uploadFile(profilePic, "profile_pictures"))
                    .setAddress(addressMapper.toEntity(newUser.address()));


            Admin newAdmin = new Admin()
                    .setUserDetails(userDetails)
                    .setCreatedBy(adminRepository.findByUserDetailsUserAuthId(creatorAdminID).orElseThrow(
                            () -> new BadClientRequestException("creator admin cannot be null")));

            return adminRepository.save(newAdmin);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<InstructorResponseDto> getInstructorByStatus(Status status) {
        if (status == null) {
            return instructorRepository.findAll()
                    .stream().map(InstructorResponseDto::fromEntity).toList();
        } else {
            return instructorRepository.findInstructorByUserDetailsUserAuthStatus(status)
                    .stream().map(InstructorResponseDto::fromEntity).toList();
        }
    }

    @Override
    public long getInstructorCountByStatus(Status status) {
        if (status == null) {
            return instructorRepository.countAllByUserDetailsUserAuthStatus(Status.ACTIVE);
        } else {
            return instructorRepository.countAllByUserDetailsUserAuthStatus(status);
        }
    }

    @Transactional
    @Override
    public Instructor approveInstructor(UUID adminId, UUID instructorID) {
        Admin admin = adminRepository.findByUserDetailsUserAuthId(adminId)
                .orElseThrow(() -> new BadClientRequestException("Admin with id " + adminId + " not found"));
        Instructor instructor = instructorRepository.findById(instructorID)
                .orElseThrow(() -> new BadClientRequestException("Instructor with id " + instructorID + " not found"));
        if (instructor.getUserDetails().getUserAuth().getStatus() != Status.PENDING &&
                instructor.getUserDetails().getUserAuth().getStatus() != Status.REJECTED
        ) {
            throw new BadClientRequestException("Instructor already Processed");
        }
        instructor.getUserDetails().getUserAuth().setStatus(Status.ACTIVE);
        instructor.setProcessedBy(admin);
        instructor.setProcessedAt(LocalDateTime.now());
        return instructor;
    }

    @Transactional
    @Override
    public Instructor rejectInstructor(UUID adminId, UUID instructorID) {
        Admin admin = adminRepository.findByUserDetailsUserAuthId(adminId)
                .orElseThrow(() -> new BadClientRequestException("Admin with id " + adminId + " not found"));
        Instructor instructor = instructorRepository.findById(instructorID)
                .orElseThrow(() -> new BadClientRequestException("Instructor with id " + instructorID + " not found"));
        if (instructor.getUserDetails().getUserAuth().getStatus() != Status.PENDING) {
            throw new BadClientRequestException("Instructor already Processed");
        }
        instructor.getUserDetails().getUserAuth().setStatus(Status.REJECTED);
        instructor.setProcessedBy(admin);
        instructor.setProcessedAt(LocalDateTime.now());
        return instructor;
    }

    @Override
    public long getAllCourseCount() {
        return courseRepository.count();
    }

    @Override
    public long getActiveStudentCountByDate(LocalDateTime afterDate) {
        return studentRepository.countAllByUserDetails_UserAuth_LastLoginAtAfter(afterDate);
    }
}

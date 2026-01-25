package com.learnease.server.service;

import com.learnease.server.dto.InstructorResponseDto;
import com.learnease.server.dto.auth.AdminRegisterRequest;
import com.learnease.server.model.Admin;
import com.learnease.server.model.Instructor;
import com.learnease.server.model.UserDetails;
import com.learnease.server.model.enums.Status;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface AdminService {
    public Admin registerAdmin(UUID creatorAdminId, AdminRegisterRequest adminRequest, MultipartFile profilePic);
    public List<InstructorResponseDto> getInstructorByStatus(Status status);
    public long getInstructorCountByStatus(Status status);
    public Instructor approveInstructor(UUID adminId, UUID instructorID);
    public Instructor rejectInstructor(UUID adminId, UUID instructorID);
}

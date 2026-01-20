package com.learnease.server.service;

import com.learnease.server.dto.auth.AdminRegisterRequest;
import com.learnease.server.model.Admin;
import com.learnease.server.model.UserDetails;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.UUID;

public interface AdminService {
    public Admin registerAdmin(UUID creatorAdminId, AdminRegisterRequest adminRequest) throws BadRequestException;
}

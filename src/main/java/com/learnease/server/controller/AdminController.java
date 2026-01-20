package com.learnease.server.controller;

import com.learnease.server.dto.ApiResponse;
import com.learnease.server.dto.auth.AdminRegisterRequest;
import com.learnease.server.model.Admin;
import com.learnease.server.model.UserDetails;
import com.learnease.server.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping("register")
    public ResponseEntity<?> registerAdmin(
            @AuthenticationPrincipal UserDetails user,  //TODO: update this with custom authentication principal
            @RequestBody AdminRegisterRequest adminRegisterRequest) {
        try {
            Admin newAdmin = adminService.registerAdmin(user.getId(), adminRegisterRequest);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse(true, "new admin registered " + "successfully!"));
        } catch (BadRequestException e) {
            throw new RuntimeException(e);
        }
    }
}

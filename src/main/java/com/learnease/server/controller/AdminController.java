package com.learnease.server.controller;

import com.learnease.server.dto.ApiResponse;
import com.learnease.server.dto.JWTDTO;
import com.learnease.server.dto.auth.AdminRegisterRequest;
import com.learnease.server.model.Admin;
import com.learnease.server.model.UserDetails;
import com.learnease.server.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/")
@RequiredArgsConstructor
@Tag(name = "Admin APIs", description = "apis for admin related activities")
public class AdminController {

    private final AdminService adminService;

    @Operation(summary = "register new admin")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("register")
    public ResponseEntity<?> registerAdmin(
            @AuthenticationPrincipal JWTDTO user,
            @RequestBody AdminRegisterRequest adminRegisterRequest) {

            System.out.println("userid from token" + user.getUserId());
            Admin newAdmin = adminService.registerAdmin(user.getUserId(), adminRegisterRequest);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse(true, "new admin registered successfully!"));
    }
}

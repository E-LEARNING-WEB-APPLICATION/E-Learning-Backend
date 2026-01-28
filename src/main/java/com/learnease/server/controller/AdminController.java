package com.learnease.server.controller;

import com.learnease.server.dto.ApiResponse;
import com.learnease.server.dto.InstructorResponseDto;
import com.learnease.server.dto.JWTDTO;
import com.learnease.server.dto.auth.AdminRegisterRequest;
import com.learnease.server.dto.auth.AdminUpdateProfileRequest;
import com.learnease.server.dto.auth.PasswordUpdateDto;
import com.learnease.server.model.Admin;
import com.learnease.server.model.Instructor;
import com.learnease.server.model.enums.Status;
import com.learnease.server.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@Tag(name = "Admin APIs", description = "apis for admin related activities")
public class AdminController {

    private final AdminService adminService;

    @Operation(summary = "register new admin")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/register")
    public ResponseEntity<?> registerAdmin(
            @AuthenticationPrincipal JWTDTO user,
            @Valid @RequestPart("data") AdminRegisterRequest adminRegisterRequest,
            @RequestPart("profilePic")MultipartFile profilePic) {
            Admin newAdmin = adminService.registerAdmin(user.getUserId(), adminRegisterRequest, profilePic);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse(true, "new admin registered successfully!"));
    }

    @Operation(summary = "update admin details")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/profile")
    public ResponseEntity<?> updateAdminProfile(
            @AuthenticationPrincipal JWTDTO user,
            @Valid @RequestPart("data") AdminUpdateProfileRequest adminRegisterRequest,
            @RequestPart("profilePic")MultipartFile profilePic) {
        Admin newAdmin = adminService.updateAdminProfile(user.getUserId(), adminRegisterRequest, profilePic);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(true, "admin profile updated successfully!"));
    }

    @Operation(summary = "update admin password")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/password")
    public ResponseEntity<ApiResponse<String>> updatePassword(
            @AuthenticationPrincipal JWTDTO user,
            @Valid @RequestBody PasswordUpdateDto dto
            ){
        adminService.updatePassword(user.getUserId(), dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Password updated successfully"));
    }

}

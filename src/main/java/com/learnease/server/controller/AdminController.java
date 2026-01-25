package com.learnease.server.controller;

import com.learnease.server.dto.ApiResponse;
import com.learnease.server.dto.InstructorResponseDto;
import com.learnease.server.dto.JWTDTO;
import com.learnease.server.dto.auth.AdminRegisterRequest;
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
            @Valid @RequestPart("data") AdminRegisterRequest adminRegisterRequest,
            @RequestPart("profilePic")MultipartFile profilePic) {
            Admin newAdmin = adminService.registerAdmin(user.getUserId(), adminRegisterRequest, profilePic);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse(true, "new admin registered successfully!"));
    }

    @Operation(summary =  "get all instructor count by status, add status param PENDING to get pending instructors count")
    @GetMapping("/instructors/count")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getInstructorsCountByStatus(
            @RequestParam(name = "status", required = false) Status status
    ) {
        long instructorCount = adminService.getInstructorCountByStatus(status);
        return ResponseEntity.ok(new ApiResponse<>(true, instructorCount));
    }

    @Operation(summary =  "get all instructors by status, add status param PENDING to get pending instructors")
    @GetMapping("/instructors")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getInstructorsByStatus(
            @RequestParam(name = "status", required = false) Status status
    ) {
        List<InstructorResponseDto> instructors = adminService.getInstructorByStatus(status);
        return ResponseEntity.ok(instructors);
    }

    @PutMapping("/instructors/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> approveInstructor(
            @AuthenticationPrincipal JWTDTO admin,
            @PathVariable UUID id) {

        adminService.approveInstructor(admin.getUserId(), id);
        return ResponseEntity.ok(new ApiResponse(true, "Instructor approved"));
    }

    @PutMapping("/instructors/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> rejectInstructor(
            @AuthenticationPrincipal JWTDTO admin,
            @PathVariable UUID id) {

        adminService.rejectInstructor(admin.getUserId(), id);
        return ResponseEntity.ok(new ApiResponse(true, "Instructor approved"));
    }

}

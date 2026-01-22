package com.learnease.server.controller;

import com.learnease.server.dto.ApiResponse;
import com.learnease.server.dto.JWTDTO;
import com.learnease.server.dto.auth.AdminRegisterRequest;
import com.learnease.server.dto.notification.NotificationResponseDTO;
import com.learnease.server.model.Admin;
import com.learnease.server.model.Instructor;
import com.learnease.server.model.Notification;
import com.learnease.server.model.UserDetails;
import com.learnease.server.model.enums.Status;
import com.learnease.server.service.AdminService;
import com.learnease.server.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/")
@RequiredArgsConstructor
@Tag(name = "Admin APIs", description = "apis for admin related activities")
public class AdminController {

    private final AdminService adminService;
    private final NotificationService notificationService;

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

    @Operation(summary =  "get all instructors by status, add status param PENDING to get pending instructors")
    @GetMapping("/instructors")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getInstructorsByStatus(
            @RequestParam(name = "status", required = false) Status status
    ) {
        List<Instructor> instructors = adminService.getInstructorByStatus(status);
        return ResponseEntity.ok(instructors);
    }

    @PostMapping("/instructors/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> approveInstructor(
            @AuthenticationPrincipal JWTDTO admin,
            @PathVariable UUID id) {

        adminService.approveInstructor(admin.getUserId(), id);
        return ResponseEntity.ok(new ApiResponse(true, "Instructor approved"));
    }

    @PostMapping("/instructors/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> rejectInstructor(
            @AuthenticationPrincipal JWTDTO admin,
            @PathVariable UUID id) {

        adminService.rejectInstructor(admin.getUserId(), id);
        return ResponseEntity.ok(new ApiResponse(true, "Instructor approved"));
    }

    @GetMapping("/admin/notifications")
    @PreAuthorize("hasRole('ADMIN')")
    public Page<NotificationResponseDTO> getAdminNotifications(Pageable pageable) {
        return notificationService
                .getAdminNotifications(pageable)
                .map(Notification::toDto);
    }
}

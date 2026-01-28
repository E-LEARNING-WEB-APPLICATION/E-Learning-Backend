package com.learnease.server.controller.adminPanel;

import com.learnease.server.dto.admin.EnrolledStudentAdminDTO;
import com.learnease.server.service.AdminService;
import com.learnease.server.service.AdminStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/course")
@RequiredArgsConstructor
public class AdminCoursesController {
    private final AdminService adminService;

    @GetMapping("/count")
    public ResponseEntity<?> getCourseCount(){
        return ResponseEntity.ok(adminService.getAllCourseCount());
    }

    @GetMapping("/{courseId}/enrollments")
    public ResponseEntity<Page<EnrolledStudentAdminDTO>> getEnrolledStudents(
            @PathVariable UUID courseId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                adminService.getEnrolledStudents(courseId, page, size)
        );
    }

}

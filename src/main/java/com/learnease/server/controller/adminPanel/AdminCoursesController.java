package com.learnease.server.controller.adminPanel;

import com.learnease.server.service.AdminService;
import com.learnease.server.service.AdminStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/course")
@RequiredArgsConstructor
public class AdminCoursesController {
    private final AdminService adminService;

    @GetMapping("/count")
    public ResponseEntity<?> getCourseCount(){
        return ResponseEntity.ok(adminService.getAllCourseCount());
    }

}

package com.edulearn.controller;

import com.edulearn.dto.DashboardStatsDTO;
import com.edulearn.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/admin")
    public ResponseEntity<DashboardStatsDTO> getAdminStats() {
        DashboardStatsDTO stats = dashboardService.getAdminStats();
        return ResponseEntity.ok(stats);
    }

        @GetMapping("/instructor/{instructorId}")
    public ResponseEntity<DashboardStatsDTO> getInstructorStats(@PathVariable Long instructorId) {
        DashboardStatsDTO stats = dashboardService.getInstructorStats(instructorId);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<DashboardStatsDTO> getStudentStats(@PathVariable Long studentId) {
        DashboardStatsDTO stats = dashboardService.getStudentStats(studentId);
        return ResponseEntity.ok(stats);
    }
}
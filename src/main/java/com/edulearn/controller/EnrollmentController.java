package com.edulearn.controller;

import com.edulearn.model.Enrollment;
import com.edulearn.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @PostMapping
    public ResponseEntity<Enrollment> enroll(@RequestBody Map<String, Long> request) {
        Long studentId = request.get("studentId");
        Long courseId = request.get("courseId");

        try {
            Enrollment enrollment = enrollmentService.enroll(studentId, courseId);
            return ResponseEntity.ok(enrollment);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Enrollment>> getEnrollments(
            @RequestParam(required = false) Long studentId,
            @RequestParam(required = false) Long courseId
    ) {
        if (studentId != null) {
            return ResponseEntity.ok(enrollmentService.findByStudent(studentId));
        } else if (courseId != null) {
            return ResponseEntity.ok(enrollmentService.findByCourse(courseId));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/progress")
    public ResponseEntity<?> updateProgress(
            @PathVariable Long id,
            @RequestBody Map<String, Integer> request
    ) {
        Integer progress = request.get("progress");
        try {
            enrollmentService.updateProgress(id, progress);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping
    public ResponseEntity<?> unenroll(@RequestBody Map<String, Long> request) {
        Long studentId = request.get("studentId");
        Long courseId = request.get("courseId");

        try {
            enrollmentService.unenroll(studentId, courseId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/check")
    public ResponseEntity<Map<String, Boolean>> checkEnrollment(
            @RequestParam Long studentId,
            @RequestParam Long courseId
    ) {
        boolean enrolled = enrollmentService.isEnrolled(studentId, courseId);
        return ResponseEntity.ok(Map.of("enrolled", enrolled));
    }
}
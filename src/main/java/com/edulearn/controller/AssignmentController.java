package com.edulearn.controller;

import com.edulearn.dto.AssignmentDTO;
import com.edulearn.dto.AssignmentSubmissionDTO;
import com.edulearn.service.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {

    @Autowired
    private AssignmentService assignmentService;

    @PostMapping
    public ResponseEntity<AssignmentDTO> createAssignment(@Valid @RequestBody AssignmentDTO assignment) {
        try {
            AssignmentDTO newAssignment = assignmentService.createAssignment(assignment);
            URI location = URI.create(String.format("/api/assignments/%s", newAssignment.getId()));
            return ResponseEntity.created(location).body(newAssignment);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<AssignmentDTO>> getAssignments(
            @RequestParam(required = false) Long courseId,
            @RequestParam(required = false) List<Long> courseIds
    ) {
        if (courseId != null) {
            List<AssignmentDTO> assignments = assignmentService.findByCourse(courseId);
            return ResponseEntity.ok(assignments);
        } else if (courseIds != null && !courseIds.isEmpty()) {
            List<AssignmentDTO> assignments = assignmentService.findByEnrolledCourses(courseIds);
            return ResponseEntity.ok(assignments);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssignmentDTO> getAssignment(@PathVariable Long id) {
        Optional<AssignmentDTO> assignment = assignmentService.findById(id);
        return assignment.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AssignmentDTO> updateAssignment(
            @PathVariable Long id,
            @Valid @RequestBody AssignmentDTO assignment
    ) {
        Optional<AssignmentDTO> existing = assignmentService.findById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        try {
            assignment.setId(id);
            AssignmentDTO updatedAssignment = assignmentService.updateAssignment(assignment);
            return ResponseEntity.ok(updatedAssignment);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssignment(@PathVariable Long id) {
        Optional<AssignmentDTO> existing = assignmentService.findById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        assignmentService.deleteAssignment(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/submit")
    public ResponseEntity<AssignmentSubmissionDTO> submitAssignment(
            @PathVariable Long id,
            @Valid @RequestBody AssignmentSubmissionDTO submission
    ) {
        submission.setAssignmentId(id);
        try {
            AssignmentSubmissionDTO newSubmission = assignmentService.submitAssignment(submission);
            URI location = URI.create(String.format("/api/assignments/%d/submissions/%d", id, newSubmission.getId()));
            return ResponseEntity.created(location).body(newSubmission);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/submissions/{id}/grade")
    public ResponseEntity<AssignmentSubmissionDTO> gradeSubmission(
            @PathVariable Long id,
            @RequestBody Map<String, Object> request
    ) {
        try {
            Integer grade = null;
            String feedback = null;

            Object gradeObj = request.get("grade");
            if (gradeObj instanceof Integer) {
                grade = (Integer) gradeObj;
            } else if (gradeObj instanceof String) {
                grade = Integer.parseInt((String) gradeObj);
            }

            Object feedbackObj = request.get("feedback");
            if (feedbackObj != null) {
                feedback = feedbackObj.toString();
            }

            AssignmentSubmissionDTO gradedSubmission = assignmentService.gradeSubmission(id, grade, feedback);
            return ResponseEntity.ok(gradedSubmission);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/submissions")
    public ResponseEntity<List<AssignmentSubmissionDTO>> getSubmissions(@PathVariable Long id) {
        List<AssignmentSubmissionDTO> submissions = assignmentService.getSubmissions(id);
        return ResponseEntity.ok(submissions);
    }

    @GetMapping("/submissions/student/{studentId}")
    public ResponseEntity<List<AssignmentSubmissionDTO>> getStudentSubmissions(@PathVariable Long studentId) {
        List<AssignmentSubmissionDTO> submissions = assignmentService.getStudentSubmissions(studentId);
        return ResponseEntity.ok(submissions);
    }

    @GetMapping("/{assignmentId}/submissions/student/{studentId}")
    public ResponseEntity<AssignmentSubmissionDTO> getStudentSubmission(
            @PathVariable Long assignmentId,
            @PathVariable Long studentId
    ) {
        return assignmentService.getStudentSubmission(assignmentId, studentId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

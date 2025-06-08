package com.edulearn.service;

import com.edulearn.dto.AssignmentDTO;
import com.edulearn.dto.AssignmentSubmissionDTO;
import com.edulearn.model.Assignment;
import com.edulearn.model.AssignmentSubmission;
import com.edulearn.model.Course;
import com.edulearn.model.User;
import com.edulearn.repository.AssignmentRepository;
import com.edulearn.repository.AssignmentSubmissionRepository;
import com.edulearn.repository.CourseRepository;
import com.edulearn.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class AssignmentService {

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private AssignmentSubmissionRepository submissionRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

//    public AssignmentDTO createAssignment(AssignmentDTO assignmentDTO) {
//        Assignment assignment = new Assignment();
//        assignment.setTitle(assignmentDTO.getTitle());
//        assignment.setDescription(assignmentDTO.getDescription());
//        assignment.setDueDate(assignmentDTO.getDueDate());
//        assignment.setTotalPoints(assignmentDTO.getTotalPoints());
//        assignment.setCreatedAt(LocalDateTime.now());
//
//        Course course = courseRepository.findById(assignmentDTO.getCourseId())
//                .orElseThrow(() -> new IllegalArgumentException("Course not found"));
//        assignment.setCourse(course);
//
//        assignment = assignmentRepository.save(assignment);
//        return convertToDTO(assignment);
//    }

    public Optional<AssignmentDTO> findById(Long id) {
        return assignmentRepository.findById(id).map(this::convertToDTO);
    }

    public List<AssignmentDTO> findByCourse(Long courseId) {
        return assignmentRepository.findByCourseId(courseId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AssignmentDTO> findByEnrolledCourses(List<Long> courseIds) {
        return assignmentRepository.findByCourseIdIn(courseIds)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public AssignmentDTO updateAssignment(AssignmentDTO assignmentDTO) {
        Assignment assignment = assignmentRepository.findById(assignmentDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("Assignment not found"));

        assignment.setTitle(assignmentDTO.getTitle());
        assignment.setDescription(assignmentDTO.getDescription());
        assignment.setDueDate(assignmentDTO.getDueDate());
        assignment.setTotalPoints(assignmentDTO.getTotalPoints());

        assignment = assignmentRepository.save(assignment);
        return convertToDTO(assignment);
    }

    public void deleteAssignment(Long id) {
        assignmentRepository.deleteById(id);
    }

    public AssignmentSubmissionDTO submitAssignment(AssignmentSubmissionDTO submissionDTO) {
        // Check if student has already submitted
        Optional<AssignmentSubmission> existingSubmission = submissionRepository
                .findByAssignmentIdAndStudentId(submissionDTO.getAssignmentId(), submissionDTO.getStudentId());

        if (existingSubmission.isPresent()) {
            throw new IllegalArgumentException("Assignment already submitted");
        }

        AssignmentSubmission submission = new AssignmentSubmission();
        submission.setContent(submissionDTO.getContent());
        submission.setSubmissionDate(LocalDateTime.now());

        Assignment assignment = assignmentRepository.findById(submissionDTO.getAssignmentId())
                .orElseThrow(() -> new IllegalArgumentException("Assignment not found"));
        submission.setAssignment(assignment);

        User student = userRepository.findById(submissionDTO.getStudentId())
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        submission.setStudent(student);

        submission = submissionRepository.save(submission);
        return convertSubmissionToDTO(submission);
    }

    public AssignmentSubmissionDTO gradeSubmission(Long submissionId, Integer grade, String feedback) {
        AssignmentSubmission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new IllegalArgumentException("Submission not found"));

        submission.setGrade(grade);
        submission.setFeedback(feedback);
        submission = submissionRepository.save(submission);

        return convertSubmissionToDTO(submission);
    }

    public List<AssignmentSubmissionDTO> getSubmissions(Long assignmentId) {
        return submissionRepository.findByAssignmentId(assignmentId)
                .stream()
                .map(this::convertSubmissionToDTO)
                .collect(Collectors.toList());
    }

    public List<AssignmentSubmissionDTO> getStudentSubmissions(Long studentId) {
        return submissionRepository.findByStudentId(studentId)
                .stream()
                .map(this::convertSubmissionToDTO)
                .collect(Collectors.toList());
    }

    public Optional<AssignmentSubmissionDTO> getStudentSubmission(Long assignmentId, Long studentId) {
        return submissionRepository.findByAssignmentIdAndStudentId(assignmentId, studentId)
                .map(this::convertSubmissionToDTO);
    }

    private AssignmentDTO convertToDTO(Assignment assignment) {
        AssignmentDTO dto = new AssignmentDTO();
        dto.setId(assignment.getId());
        dto.setCourseId(assignment.getCourse().getId());
        dto.setTitle(assignment.getTitle());
        dto.setDescription(assignment.getDescription());
        dto.setDueDate(assignment.getDueDate());
        dto.setTotalPoints(assignment.getTotalPoints());
        dto.setCreatedAt(assignment.getCreatedAt());

        List<AssignmentSubmissionDTO> submissions = assignment.getSubmissions()
                .stream()
                .map(this::convertSubmissionToDTO)
                .collect(Collectors.toList());
        dto.setSubmissions(submissions);

        return dto;
    }

    private AssignmentSubmissionDTO convertSubmissionToDTO(AssignmentSubmission submission) {
        AssignmentSubmissionDTO dto = new AssignmentSubmissionDTO();
        dto.setId(submission.getId());
        dto.setAssignmentId(submission.getAssignment().getId());
        dto.setStudentId(submission.getStudent().getId());
        dto.setSubmissionDate(submission.getSubmissionDate());
        dto.setContent(submission.getContent());
        dto.setGrade(submission.getGrade());
        dto.setFeedback(submission.getFeedback());
        return dto;
    }

    public AssignmentDTO createAssignment(AssignmentDTO assignmentDTO) {
        validateAssignmentDTO(assignmentDTO);
        Assignment assignment = new Assignment();
        assignment.setTitle(assignmentDTO.getTitle());
        assignment.setDescription(assignmentDTO.getDescription());
        assignment.setDueDate(assignmentDTO.getDueDate());
        assignment.setTotalPoints(assignmentDTO.getTotalPoints());
        assignment.setCreatedAt(LocalDateTime.now());

        Course course = courseRepository.findById(assignmentDTO.getCourseId())
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));
        assignment.setCourse(course);

        Assignment savedAssignment = assignmentRepository.save(assignment);
        return convertToDTO(savedAssignment);
    }

    private void validateAssignmentDTO(AssignmentDTO dto) {
        if (dto.getTitle() == null || dto.getTitle().isBlank()) {
            throw new IllegalArgumentException("Title cannot be blank");
        }
        if (dto.getDueDate() == null || dto.getDueDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Due date must be in the future");
        }
        if (dto.getTotalPoints() == null || dto.getTotalPoints() <= 0) {
            throw new IllegalArgumentException("Total points must be positive");
        }
        if (dto.getCourseId() == null) {
            throw new IllegalArgumentException("Course ID is required");
        }
    }
}

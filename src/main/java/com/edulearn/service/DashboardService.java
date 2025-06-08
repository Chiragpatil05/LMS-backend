package com.edulearn.service;

import com.edulearn.dto.ActivityDTO;
import com.edulearn.dto.CourseStatsDTO;
import com.edulearn.dto.DashboardStatsDTO;
import com.edulearn.model.*;
import com.edulearn.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class DashboardService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private AssignmentSubmissionRepository submissionRepository;

    @Autowired
    private QuizAttemptRepository attemptRepository;

    public DashboardStatsDTO getAdminStats() {
        DashboardStatsDTO stats = new DashboardStatsDTO();

        // Basic counts
        stats.setTotalCourses((long) courseRepository.findAll().size());
        stats.setTotalStudents((long) userRepository.findByRole("student").size());
        stats.setTotalInstructors((long) userRepository.findByRole("instructor").size());
        stats.setTotalEnrollments((long) enrollmentRepository.findAll().size());
        stats.setTotalAssignments((long) assignmentRepository.findAll().size());
        stats.setTotalQuizzes((long) quizRepository.findAll().size());

        // Average rating
        List<Course> courses = courseRepository.findAll();
        double avgRating = courses.stream()
                .mapToDouble(Course::getRating)
                .average()
                .orElse(0.0);
        stats.setAverageRating(avgRating);

        // Popular courses
        List<CourseStatsDTO> popularCourses = courses.stream()
                .sorted((c1, c2) -> Integer.compare(c2.getEnrolled(), c1.getEnrolled()))
                .limit(5)
                .map(this::convertToCourseStats)
                .collect(Collectors.toList());
        stats.setPopularCourses(popularCourses);

        // Recent activities
        stats.setRecentActivities(getRecentActivities());

        return stats;
    }

    public DashboardStatsDTO getInstructorStats(Long instructorId) {
        DashboardStatsDTO stats = new DashboardStatsDTO();

        List<Course> instructorCourses = courseRepository.findByInstructorId(instructorId);

        stats.setTotalCourses((long) instructorCourses.size());

        // Total students across all courses
        long totalStudents = instructorCourses.stream()
                .mapToLong(course -> enrollmentRepository.findByCourseId(course.getId()).size())
                .sum();
        stats.setTotalStudents(totalStudents);

        // Total assignments and quizzes
        List<Long> courseIds = instructorCourses.stream()
                .map(Course::getId)
                .collect(Collectors.toList());

        if (!courseIds.isEmpty()) {
            stats.setTotalAssignments((long) assignmentRepository.findByCourseIdIn(courseIds).size());
            stats.setTotalQuizzes((long) quizRepository.findByCourseIdIn(courseIds).size());
        } else {
            stats.setTotalAssignments(0L);
            stats.setTotalQuizzes(0L);
        }

        // Average rating for instructor courses
        double avgRating = instructorCourses.stream()
                .mapToDouble(Course::getRating)
                .average()
                .orElse(0.0);
        stats.setAverageRating(avgRating);

        // Popular courses for this instructor
        List<CourseStatsDTO> popularCourses = instructorCourses.stream()
                .sorted((c1, c2) -> Integer.compare(c2.getEnrolled(), c1.getEnrolled()))
                .limit(5)
                .map(this::convertToCourseStats)
                .collect(Collectors.toList());
        stats.setPopularCourses(popularCourses);

        // Recent activities for instructor courses
        stats.setRecentActivities(getInstructorActivities(courseIds));

        return stats;
    }

    public DashboardStatsDTO getStudentStats(Long studentId) {
        DashboardStatsDTO stats = new DashboardStatsDTO();

        List<Enrollment> enrollments = enrollmentRepository.findByStudentId(studentId);

        stats.setTotalCourses((long) enrollments.size());

        // Completed courses
        long completedCourses = enrollments.stream()
                .mapToLong(enrollment -> enrollment.getCompleted() ? 1 : 0)
                .sum();

        // Assignments submitted
        long submittedAssignments = submissionRepository.findByStudentId(studentId).size();
        stats.setTotalAssignments(submittedAssignments);

        // Quizzes attempted
        long attemptedQuizzes = attemptRepository.findByStudentId(studentId).size();
        stats.setTotalQuizzes(attemptedQuizzes);

        // Average quiz score
        List<QuizAttempt> attempts = attemptRepository.findByStudentId(studentId);
        double avgScore = attempts.stream()
                .mapToDouble(QuizAttempt::getScore)
                .average()
                .orElse(0.0);
        stats.setAverageRating(avgScore);

        // Enrolled courses
        List<CourseStatsDTO> enrolledCourses = enrollments.stream()
                .map(enrollment -> {
                    Course course = enrollment.getCourse();
                    CourseStatsDTO courseStats = convertToCourseStats(course);
                    return courseStats;
                })
                .collect(Collectors.toList());
        stats.setPopularCourses(enrolledCourses);

        // Recent activities for student
        stats.setRecentActivities(getStudentActivities(studentId));

        return stats;
    }

    private CourseStatsDTO convertToCourseStats(Course course) {
        CourseStatsDTO stats = new CourseStatsDTO();
        stats.setId(course.getId());
        stats.setTitle(course.getTitle());
        stats.setInstructorName(course.getInstructor().getName());
        stats.setEnrolled(course.getEnrolled());
        stats.setRating(course.getRating());
        stats.setCategory(course.getCategory());
        return stats;
    }

    private List<ActivityDTO> getRecentActivities() {
        List<ActivityDTO> activities = new ArrayList<>();

        // Recent enrollments
        List<Enrollment> recentEnrollments = enrollmentRepository.findAll()
                .stream()
                .sorted((e1, e2) -> e2.getEnrolledDate().compareTo(e1.getEnrolledDate()))
                .limit(10)
                .collect(Collectors.toList());

        for (Enrollment enrollment : recentEnrollments) {
            ActivityDTO activity = new ActivityDTO();
            activity.setType("enrollment");
            activity.setMessage("New enrollment");
            activity.setUserName(enrollment.getStudent().getName());
            activity.setCourseName(enrollment.getCourse().getTitle());
            activity.setTimestamp(enrollment.getEnrolledDate());
            activities.add(activity);
        }

        // Recent submissions
        List<AssignmentSubmission> recentSubmissions = submissionRepository.findAll()
                .stream()
                .sorted((s1, s2) -> s2.getSubmissionDate().compareTo(s1.getSubmissionDate()))
                .limit(10)
                .collect(Collectors.toList());

        for (AssignmentSubmission submission : recentSubmissions) {
            ActivityDTO activity = new ActivityDTO();
            activity.setType("submission");
            activity.setMessage("Assignment submitted");
            activity.setUserName(submission.getStudent().getName());
            activity.setCourseName(submission.getAssignment().getCourse().getTitle());
            activity.setTimestamp(submission.getSubmissionDate());
            activities.add(activity);
        }

        return activities.stream()
                .sorted((a1, a2) -> a2.getTimestamp().compareTo(a1.getTimestamp()))
                .limit(20)
                .collect(Collectors.toList());
    }

    private List<ActivityDTO> getInstructorActivities(List<Long> courseIds) {
        List<ActivityDTO> activities = new ArrayList<>();

        if (courseIds.isEmpty()) {
            return activities;
        }

        // Recent enrollments in instructor's courses
        List<Enrollment> enrollments = enrollmentRepository.findAll()
                .stream()
                .filter(e -> courseIds.contains(e.getCourse().getId()))
                .sorted((e1, e2) -> e2.getEnrolledDate().compareTo(e1.getEnrolledDate()))
                .limit(10)
                .collect(Collectors.toList());

        for (Enrollment enrollment : enrollments) {
            ActivityDTO activity = new ActivityDTO();
            activity.setType("enrollment");
            activity.setMessage("Student enrolled");
            activity.setUserName(enrollment.getStudent().getName());
            activity.setCourseName(enrollment.getCourse().getTitle());
            activity.setTimestamp(enrollment.getEnrolledDate());
            activities.add(activity);
        }

        return activities;
    }

    private List<ActivityDTO> getStudentActivities(Long studentId) {
        List<ActivityDTO> activities = new ArrayList<>();

        // Recent submissions
        List<AssignmentSubmission> submissions = submissionRepository.findByStudentId(studentId)
                .stream()
                .sorted((s1, s2) -> s2.getSubmissionDate().compareTo(s1.getSubmissionDate()))
                .limit(10)
                .collect(Collectors.toList());

        for (AssignmentSubmission submission : submissions) {
            ActivityDTO activity = new ActivityDTO();
            activity.setType("submission");
            activity.setMessage("Assignment submitted");
            activity.setCourseName(submission.getAssignment().getCourse().getTitle());
            activity.setTimestamp(submission.getSubmissionDate());
            activities.add(activity);
        }

        // Recent quiz attempts
        List<QuizAttempt> attempts = attemptRepository.findByStudentId(studentId)
                .stream()
                .sorted((a1, a2) -> a2.getAttemptDate().compareTo(a1.getAttemptDate()))
                .limit(10)
                .collect(Collectors.toList());

        for (QuizAttempt attempt : attempts) {
            ActivityDTO activity = new ActivityDTO();
            activity.setType("quiz");
            activity.setMessage("Quiz attempted");
            activity.setCourseName(attempt.getQuiz().getCourse().getTitle());
            activity.setTimestamp(attempt.getAttemptDate());
            activities.add(activity);
        }

        return activities.stream()
                .sorted((a1, a2) -> a2.getTimestamp().compareTo(a1.getTimestamp()))
                .limit(20)
                .collect(Collectors.toList());
    }
}
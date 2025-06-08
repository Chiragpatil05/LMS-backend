package com.edulearn.dto;

import java.util.List;

public class DashboardStatsDTO {
    private Long totalCourses;
    private Long totalStudents;
    private Long totalInstructors;
    private Long totalEnrollments;
    private Long totalAssignments;
    private Long totalQuizzes;
    private Double averageRating;
    private List<CourseStatsDTO> popularCourses;
    private List<ActivityDTO> recentActivities;

    // Getters and Setters
    public Long getTotalCourses() {
        return totalCourses;
    }

    public void setTotalCourses(Long totalCourses) {
        this.totalCourses = totalCourses;
    }

    public Long getTotalStudents() {
        return totalStudents;
    }

    public void setTotalStudents(Long totalStudents) {
        this.totalStudents = totalStudents;
    }

    public Long getTotalInstructors() {
        return totalInstructors;
    }

    public void setTotalInstructors(Long totalInstructors) {
        this.totalInstructors = totalInstructors;
    }

    public Long getTotalEnrollments() {
        return totalEnrollments;
    }

    public void setTotalEnrollments(Long totalEnrollments) {
        this.totalEnrollments = totalEnrollments;
    }

    public Long getTotalAssignments() {
        return totalAssignments;
    }

    public void setTotalAssignments(Long totalAssignments) {
        this.totalAssignments = totalAssignments;
    }

    public Long getTotalQuizzes() {
        return totalQuizzes;
    }

    public void setTotalQuizzes(Long totalQuizzes) {
        this.totalQuizzes = totalQuizzes;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public List<CourseStatsDTO> getPopularCourses() {
        return popularCourses;
    }

    public void setPopularCourses(List<CourseStatsDTO> popularCourses) {
        this.popularCourses = popularCourses;
    }

    public List<ActivityDTO> getRecentActivities() {
        return recentActivities;
    }

    public void setRecentActivities(List<ActivityDTO> recentActivities) {
        this.recentActivities = recentActivities;
    }
}
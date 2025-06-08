//package com.edulearn.dto;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//public class CourseDTO {
//    private Long id;
//    private String title;
//    private String description;
//    private String thumbnail;
//    private String category;
//    private String level;
//    private Double price;
//    private Double originalPrice;
//    private String status;
//    private Long instructorId;
//    private String instructorName;
//    private String instructorAvatar;
//    private Double rating;
//    private Integer enrolled;
//    private LocalDateTime createdAt;
//    private LocalDateTime updatedAt;
//    private List<String> outcomes;
//    private List<String> prerequisites;
//    private List<LessonDTO> lessons;
//
//    // Getters and Setters
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public String getThumbnail() {
//        return thumbnail;
//    }
//
//    public void setThumbnail(String thumbnail) {
//        this.thumbnail = thumbnail;
//    }
//
//    public String getCategory() {
//        return category;
//    }
//
//    public void setCategory(String category) {
//        this.category = category;
//    }
//
//    public String getLevel() {
//        return level;
//    }
//
//    public void setLevel(String level) {
//        this.level = level;
//    }
//
//    public Double getPrice() {
//        return price;
//    }
//
//    public void setPrice(Double price) {
//        this.price = price;
//    }
//
//    public Double getOriginalPrice() {
//        return originalPrice;
//    }
//
//    public void setOriginalPrice(Double originalPrice) {
//        this.originalPrice = originalPrice;
//    }
//
//    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }
//
//    public Long getInstructorId() {
//        return instructorId;
//    }
//
//    public void setInstructorId(Long instructorId) {
//        this.instructorId = instructorId;
//    }
//
//    public String getInstructorName() {
//        return instructorName;
//    }
//
//    public void setInstructorName(String instructorName) {
//        this.instructorName = instructorName;
//    }
//
//    public String getInstructorAvatar() {
//        return instructorAvatar;
//    }
//
//    public void setInstructorAvatar(String instructorAvatar) {
//        this.instructorAvatar = instructorAvatar;
//    }
//
//    public Double getRating() {
//        return rating;
//    }
//
//    public void setRating(Double rating) {
//        this.rating = rating;
//    }
//
//    public Integer getEnrolled() {
//        return enrolled;
//    }
//
//    public void setEnrolled(Integer enrolled) {
//        this.enrolled = enrolled;
//    }
//
//    public LocalDateTime getCreatedAt() {
//        return createdAt;
//    }
//
//    public void setCreatedAt(LocalDateTime createdAt) {
//        this.createdAt = createdAt;
//    }
//
//    public LocalDateTime getUpdatedAt() {
//        return updatedAt;
//    }
//
//    public void setUpdatedAt(LocalDateTime updatedAt) {
//        this.updatedAt = updatedAt;
//    }
//
//    public List<String> getOutcomes() {
//        return outcomes;
//    }
//
//    public void setOutcomes(List<String> outcomes) {
//        this.outcomes = outcomes;
//    }
//
//    public List<String> getPrerequisites() {
//        return prerequisites;
//    }
//
//    public void setPrerequisites(List<String> prerequisites) {
//        this.prerequisites = prerequisites;
//    }
//
//    public List<LessonDTO> getLessons() {
//        return lessons;
//    }
//
//    public void setLessons(List<LessonDTO> lessons) {
//        this.lessons = lessons;
//    }
//}

package com.edulearn.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

public class CourseDTO {
    private Long id;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Thumbnail is required")
    private String thumbnail;

    @NotBlank(message = "Category is required")
    private String category;

    @NotBlank(message = "Level is required")
    private String level;

    @NotNull(message = "Price is required")
    @PositiveOrZero(message = "Price must be positive or zero")
    private Double price;

    private Double originalPrice;
    private String status;

    @NotNull(message = "Instructor ID is required")
    private Long instructorId;

    private String instructorName;
    private String instructorAvatar;
    private Double rating;
    private Integer enrolled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<String> outcomes;
    private List<String> prerequisites;
    private List<LessonDTO> lessons;

    // Constructors
    public CourseDTO() {}

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(Double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(Long instructorId) {
        this.instructorId = instructorId;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public String getInstructorAvatar() {
        return instructorAvatar;
    }

    public void setInstructorAvatar(String instructorAvatar) {
        this.instructorAvatar = instructorAvatar;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Integer getEnrolled() {
        return enrolled;
    }

    public void setEnrolled(Integer enrolled) {
        this.enrolled = enrolled;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<String> getOutcomes() {
        return outcomes;
    }

    public void setOutcomes(List<String> outcomes) {
        this.outcomes = outcomes;
    }

    public List<String> getPrerequisites() {
        return prerequisites;
    }

    public void setPrerequisites(List<String> prerequisites) {
        this.prerequisites = prerequisites;
    }

    public List<LessonDTO> getLessons() {
        return lessons;
    }

    public void setLessons(List<LessonDTO> lessons) {
        this.lessons = lessons;
    }
}
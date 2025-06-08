//package com.edulearn.service;
//
//import com.edulearn.dto.CourseDTO;
//import com.edulearn.dto.LessonDTO;
//import com.edulearn.model.*;
//import com.edulearn.repository.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@Service
//@Transactional
//public class CourseService {
//
//    @Autowired
//    private CourseRepository courseRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private CourseOutcomeRepository outcomeRepository;
//
//    @Autowired
//    private CoursePrerequisiteRepository prerequisiteRepository;
//
//    @Autowired
//    private LessonRepository lessonRepository;
//
////    public CourseDTO createCourse(CourseDTO courseDTO) {
////        Course course = new Course();
////        course.setTitle(courseDTO.getTitle());
////        course.setDescription(courseDTO.getDescription());
////        course.setThumbnail(courseDTO.getThumbnail());
////        course.setCategory(courseDTO.getCategory());
////        course.setLevel(courseDTO.getLevel());
////        course.setPrice(courseDTO.getPrice());
////        course.setOriginalPrice(courseDTO.getOriginalPrice());
////        course.setStatus(courseDTO.getStatus());
////        course.setRating(0.0);
////        course.setEnrolled(0);
////        course.setCreatedAt(LocalDateTime.now());
////        course.setUpdatedAt(LocalDateTime.now());
////
////        // Set instructor
////        User instructor = userRepository.findById(courseDTO.getInstructorId())
////            .orElseThrow(() -> new IllegalArgumentException("Instructor not found"));
////        course.setInstructor(instructor);
////
////        course = courseRepository.save(course);
////
////        // Save outcomes
////        if (courseDTO.getOutcomes() != null) {
////            for (String outcomeText : courseDTO.getOutcomes()) {
////                CourseOutcome outcome = new CourseOutcome();
////                outcome.setCourse(course);
////                outcome.setOutcome(outcomeText);
////                outcomeRepository.save(outcome);
////            }
////        }
////
////        // Save prerequisites
////        if (courseDTO.getPrerequisites() != null) {
////            for (String prerequisiteText : courseDTO.getPrerequisites()) {
////                CoursePrerequisite prerequisite = new CoursePrerequisite();
////                prerequisite.setCourse(course);
////                prerequisite.setPrerequisite(prerequisiteText);
////                prerequisiteRepository.save(prerequisite);
////            }
////        }
////
////        // Save lessons
////        if (courseDTO.getLessons() != null) {
////            for (LessonDTO lessonDTO : courseDTO.getLessons()) {
////                Lesson lesson = new Lesson();
////                lesson.setCourse(course);
////                lesson.setTitle(lessonDTO.getTitle());
////                lesson.setDescription(lessonDTO.getDescription());
////                lesson.setDuration(lessonDTO.getDuration());
////                lesson.setVideoUrl(lessonDTO.getVideoUrl());
////                lessonRepository.save(lesson);
////            }
////        }
////
////        return convertToDTO(course);
////    }
//
//    @Transactional
//    public CourseDTO createCourse(CourseDTO courseDTO) {
//        User instructor = userRepository.findById(courseDTO.getInstructorId())
//                .orElseThrow(() -> new IllegalArgumentException("Instructor not found"));
//
//        Course course = new Course();
//        course.setTitle(courseDTO.getTitle());
//        course.setDescription(courseDTO.getDescription());
//        course.setThumbnail(courseDTO.getThumbnail());
//        course.setCategory(courseDTO.getCategory());
//        course.setLevel(courseDTO.getLevel());
//        course.setPrice(courseDTO.getPrice());
//        course.setOriginalPrice(courseDTO.getOriginalPrice());
//        course.setStatus(courseDTO.getStatus());
//        course.setRating(0.0);
//        course.setEnrolled(0);
//        course.setCreatedAt(LocalDateTime.now());
//        course.setUpdatedAt(LocalDateTime.now());
//        course.setInstructor(instructor);
//
//        // Save course first to get ID for relationships
//        course = courseRepository.save(course);
//
//        // Save outcomes
//        List<CourseOutcome> savedOutcomes = new ArrayList<>();
//        if (courseDTO.getOutcomes() != null) {
//            for (String outcomeText : courseDTO.getOutcomes()) {
//                CourseOutcome outcome = new CourseOutcome();
//                outcome.setCourse(course);
//                outcome.setOutcome(outcomeText);
//                savedOutcomes.add(outcomeRepository.save(outcome));
//            }
//        }
//        course.setOutcomes(savedOutcomes);
//
//        // Save prerequisites
//        List<CoursePrerequisite> savedPrerequisites = new ArrayList<>();
//        if (courseDTO.getPrerequisites() != null) {
//            for (String prerequisiteText : courseDTO.getPrerequisites()) {
//                CoursePrerequisite prerequisite = new CoursePrerequisite();
//                prerequisite.setCourse(course);
//                prerequisite.setPrerequisite(prerequisiteText);
//                savedPrerequisites.add(prerequisiteRepository.save(prerequisite));
//            }
//        }
//        course.setPrerequisites(savedPrerequisites);
//
//        // Save lessons
//        List<Lesson> savedLessons = new ArrayList<>();
//        if (courseDTO.getLessons() != null) {
//            for (LessonDTO lessonDTO : courseDTO.getLessons()) {
//                Lesson lesson = new Lesson();
//                lesson.setCourse(course);
//                lesson.setTitle(lessonDTO.getTitle());
//                lesson.setDescription(lessonDTO.getDescription());
//                lesson.setDuration(lessonDTO.getDuration());
//                lesson.setVideoUrl(lessonDTO.getVideoUrl());
//                savedLessons.add(lessonRepository.save(lesson));
//            }
//        }
//        course.setLessons(savedLessons);
//
//        // Optionally save again if needed
//        // course = courseRepository.save(course);
//
//        return convertToDTO(course);
//    }
//
//    private CourseDTO convertToDTO(Course course) {
//        CourseDTO dto = new CourseDTO();
//
//        dto.setId(course.getId());
//        dto.setTitle(course.getTitle());
//        dto.setDescription(course.getDescription());
//        dto.setThumbnail(course.getThumbnail());
//        dto.setCategory(course.getCategory());
//        dto.setLevel(course.getLevel());
//        dto.setPrice(course.getPrice());
//        dto.setOriginalPrice(course.getOriginalPrice());
//        dto.setStatus(course.getStatus());
//        dto.setInstructorId(course.getInstructor().getId());
//        dto.setInstructorName(course.getInstructor().getName());
//        dto.setInstructorAvatar(course.getInstructor().getAvatar());
//        dto.setRating(course.getRating());
//        dto.setEnrolled(course.getEnrolled());
//        dto.setCreatedAt(course.getCreatedAt());
//        dto.setUpdatedAt(course.getUpdatedAt());
//
//        // Map outcomes to String list
//        List<String> outcomes = course.getOutcomes().stream()
//                .map(CourseOutcome::getOutcome)
//                .collect(Collectors.toList());
//        dto.setOutcomes(outcomes);
//
//        // Map prerequisites to String list
//        List<String> prerequisites = course.getPrerequisites().stream()
//                .map(CoursePrerequisite::getPrerequisite)
//                .collect(Collectors.toList());
//        dto.setPrerequisites(prerequisites);
//
//        // Map lessons to LessonDTO list
//        List<LessonDTO> lessonDTOs = course.getLessons().stream()
//                .map(lesson -> {
//                    LessonDTO lDto = new LessonDTO();
//                    lDto.setId(lesson.getId());
//                    lDto.setTitle(lesson.getTitle());
//                    lDto.setDescription(lesson.getDescription());
//                    lDto.setDuration(lesson.getDuration());
//                    lDto.setVideoUrl(lesson.getVideoUrl());
//                    return lDto;
//                })
//                .collect(Collectors.toList());
//        dto.setLessons(lessonDTOs);
//
//        return dto;
//    }
//
//
//    public Optional<CourseDTO> findById(Long id) {
//        return courseRepository.findById(id).map(this::convertToDTO);
//    }
//
//    public List<CourseDTO> findByInstructor(Long instructorId) {
//        return courseRepository.findByInstructorId(instructorId)
//            .stream()
//            .map(this::convertToDTO)
//            .collect(Collectors.toList());
//    }
//
//    public List<CourseDTO> findActiveCourses() {
//        return courseRepository.findAllActiveCourses()
//            .stream()
//            .map(this::convertToDTO)
//            .collect(Collectors.toList());
//    }
//
//    public List<CourseDTO> searchCourses(String query) {
//        return courseRepository.searchCourses(query)
//            .stream()
//            .map(this::convertToDTO)
//            .collect(Collectors.toList());
//    }
//
////    public CourseDTO updateCourse(CourseDTO courseDTO) {
////        Course course = courseRepository.findById(courseDTO.getId())
////            .orElseThrow(() -> new IllegalArgumentException("Course not found"));
////
////        course.setTitle(courseDTO.getTitle());
////        course.setDescription(courseDTO.getDescription());
////        course.setThumbnail(courseDTO.getThumbnail());
////        course.setCategory(courseDTO.getCategory());
////        course.setLevel(courseDTO.getLevel());
////        course.setPrice(courseDTO.getPrice());
////        course.setOriginalPrice(courseDTO.getOriginalPrice());
////        course.setStatus(courseDTO.getStatus());
////        course.setUpdatedAt(LocalDateTime.now());
////
////        // Update outcomes
////        outcomeRepository.deleteByCourseId(course.getId());
////        if (courseDTO.getOutcomes() != null) {
////            for (String outcomeText : courseDTO.getOutcomes()) {
////                CourseOutcome outcome = new CourseOutcome();
////                outcome.setCourse(course);
////                outcome.setOutcome(outcomeText);
////                outcomeRepository.save(outcome);
////            }
////        }
////
////        // Update prerequisites
////        prerequisiteRepository.deleteByCourseId(course.getId());
////        if (courseDTO.getPrerequisites() != null) {
////            for (String prerequisiteText : courseDTO.getPrerequisites()) {
////                CoursePrerequisite prerequisite = new CoursePrerequisite();
////                prerequisite.setCourse(course);
////                prerequisite.setPrerequisite(prerequisiteText);
////                prerequisiteRepository.save(prerequisite);
////            }
////        }
////
////        // Update lessons
////        lessonRepository.deleteByCourseId(course.getId());
////        if (courseDTO.getLessons() != null) {
////            for (LessonDTO lessonDTO : courseDTO.getLessons()) {
////                Lesson lesson = new Lesson();
////                lesson.setCourse(course);
////                lesson.setTitle(lessonDTO.getTitle());
////                lesson.setDescription(lessonDTO.getDescription());
////                lesson.setDuration(lessonDTO.getDuration());
////                lesson.setVideoUrl(lessonDTO.getVideoUrl());
////                lessonRepository.save(lesson);
////            }
////        }
////
////        course = courseRepository.save(course);
////        return convertToDTO(course);
////    }
//
//    @Transactional
//    public CourseDTO updateCourse(CourseDTO courseDTO) {
//        // Fetch existing course by id or throw error if not found
//        Course course = courseRepository.findById(courseDTO.getId())
//                .orElseThrow(() -> new IllegalArgumentException("Course not found with id: " + courseDTO.getId()));
//
//        // Update basic fields
//        course.setTitle(courseDTO.getTitle());
//        course.setDescription(courseDTO.getDescription());
//        course.setThumbnail(courseDTO.getThumbnail());
//        course.setCategory(courseDTO.getCategory());
//        course.setLevel(courseDTO.getLevel());
//        course.setPrice(courseDTO.getPrice());
//        course.setOriginalPrice(courseDTO.getOriginalPrice());
//        course.setStatus(courseDTO.getStatus());
//        course.setUpdatedAt(LocalDateTime.now());
//
//        // Update instructor if instructorId is provided
//        if (courseDTO.getInstructorId() != null) {
//            User instructor = userRepository.findById(courseDTO.getInstructorId())
//                    .orElseThrow(() -> new IllegalArgumentException("Instructor not found with id: " + courseDTO.getInstructorId()));
//            course.setInstructor(instructor);
//        }
//
//        // Clear existing outcomes and add new ones from DTO
//        course.getOutcomes().clear();
//        if (courseDTO.getOutcomes() != null) {
//            for (String outcomeText : courseDTO.getOutcomes()) {
//                CourseOutcome outcome = new CourseOutcome();
//                outcome.setCourse(course);
//                outcome.setOutcome(outcomeText);
//                course.getOutcomes().add(outcome);
//            }
//        }
//
//        // Clear existing prerequisites and add new ones from DTO
//        course.getPrerequisites().clear();
//        if (courseDTO.getPrerequisites() != null) {
//            for (String prerequisiteText : courseDTO.getPrerequisites()) {
//                CoursePrerequisite prerequisite = new CoursePrerequisite();
//                prerequisite.setCourse(course);
//                prerequisite.setPrerequisite(prerequisiteText);
//                course.getPrerequisites().add(prerequisite);
//            }
//        }
//
//        // Clear existing lessons and add new lessons from DTO
//        course.getLessons().clear();
//        if (courseDTO.getLessons() != null) {
//            for (LessonDTO lessonDTO : courseDTO.getLessons()) {
//                Lesson lesson = new Lesson();
//                lesson.setCourse(course);
//                lesson.setTitle(lessonDTO.getTitle());
//                lesson.setDescription(lessonDTO.getDescription());
//                lesson.setDuration(lessonDTO.getDuration());
//                lesson.setVideoUrl(lessonDTO.getVideoUrl());
//                course.getLessons().add(lesson);
//            }
//        }
//
//        // Save the updated course entity (cascades save to child entities)
//        course = courseRepository.save(course);
//
//        // Convert updated entity back to DTO and return
//        return convertToDTO(course);
//    }
//
//    private LessonDTO convertLessonToDTO(Lesson lesson) {
//        LessonDTO dto = new LessonDTO();
//        dto.setId(lesson.getId());
//        dto.setTitle(lesson.getTitle());
//        dto.setDescription(lesson.getDescription());
//        dto.setDuration(lesson.getDuration());
//        dto.setVideoUrl(lesson.getVideoUrl());
//        return dto;
//    }
//}

package com.edulearn.service;

import com.edulearn.dto.CourseDTO;
import com.edulearn.dto.LessonDTO;
import com.edulearn.model.*;
import com.edulearn.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseOutcomeRepository outcomeRepository;

    @Autowired
    private CoursePrerequisiteRepository prerequisiteRepository;

    @Autowired
    private LessonRepository lessonRepository;

    public CourseDTO createCourse(CourseDTO courseDTO) {
        Course course = new Course();
        course.setTitle(courseDTO.getTitle());
        course.setDescription(courseDTO.getDescription());
        course.setThumbnail(courseDTO.getThumbnail());
        course.setCategory(courseDTO.getCategory());
        course.setLevel(courseDTO.getLevel());
        course.setPrice(courseDTO.getPrice());
        course.setOriginalPrice(courseDTO.getOriginalPrice() != null ? courseDTO.getOriginalPrice() : courseDTO.getPrice() * 2);
        course.setStatus(courseDTO.getStatus() != null ? courseDTO.getStatus() : "active");
        course.setRating(0.0);
        course.setEnrolled(0);
        course.setCreatedAt(LocalDateTime.now());
        course.setUpdatedAt(LocalDateTime.now());

        // Set instructor
        User instructor = userRepository.findById(courseDTO.getInstructorId())
                .orElseThrow(() -> new IllegalArgumentException("Instructor not found"));
        course.setInstructor(instructor);

        course = courseRepository.save(course);

        // Save outcomes
        if (courseDTO.getOutcomes() != null && !courseDTO.getOutcomes().isEmpty()) {
            for (String outcomeText : courseDTO.getOutcomes()) {
                if (outcomeText != null && !outcomeText.trim().isEmpty()) {
                    CourseOutcome outcome = new CourseOutcome();
                    outcome.setCourse(course);
                    outcome.setOutcome(outcomeText.trim());
                    outcomeRepository.save(outcome);
                }
            }
        }

        // Save prerequisites
        if (courseDTO.getPrerequisites() != null && !courseDTO.getPrerequisites().isEmpty()) {
            for (String prerequisiteText : courseDTO.getPrerequisites()) {
                if (prerequisiteText != null && !prerequisiteText.trim().isEmpty()) {
                    CoursePrerequisite prerequisite = new CoursePrerequisite();
                    prerequisite.setCourse(course);
                    prerequisite.setPrerequisite(prerequisiteText.trim());
                    prerequisiteRepository.save(prerequisite);
                }
            }
        }

        // Save lessons
        if (courseDTO.getLessons() != null && !courseDTO.getLessons().isEmpty()) {
            for (LessonDTO lessonDTO : courseDTO.getLessons()) {
                if (lessonDTO.getTitle() != null && !lessonDTO.getTitle().trim().isEmpty()) {
                    Lesson lesson = new Lesson();
                    lesson.setCourse(course);
                    lesson.setTitle(lessonDTO.getTitle());
                    lesson.setDescription(lessonDTO.getDescription() != null ? lessonDTO.getDescription() : "");
                    lesson.setDuration(lessonDTO.getDuration() != null ? lessonDTO.getDuration() : "45 min");
                    lesson.setVideoUrl(lessonDTO.getVideoUrl() != null ? lessonDTO.getVideoUrl() : "");
                    lessonRepository.save(lesson);
                }
            }
        }

        return convertToDTO(course);
    }

    public Optional<CourseDTO> findById(Long id) {
        return courseRepository.findById(id).map(this::convertToDTO);
    }

    public List<CourseDTO> findByInstructor(Long instructorId) {
        return courseRepository.findByInstructorId(instructorId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<CourseDTO> findAllCourses() {
        return courseRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<CourseDTO> findActiveCourses() {
        return courseRepository.findByStatus("active")
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<CourseDTO> findByStatus(String status) {
        return courseRepository.findByStatus(status)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<CourseDTO> searchCourses(String query) {
        return courseRepository.searchCourses(query)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public CourseDTO updateCourse(CourseDTO courseDTO) {
        Course course = courseRepository.findById(courseDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        course.setTitle(courseDTO.getTitle());
        course.setDescription(courseDTO.getDescription());
        course.setThumbnail(courseDTO.getThumbnail());
        course.setCategory(courseDTO.getCategory());
        course.setLevel(courseDTO.getLevel());
        course.setPrice(courseDTO.getPrice());
        course.setOriginalPrice(courseDTO.getOriginalPrice() != null ? courseDTO.getOriginalPrice() : courseDTO.getPrice() * 2);
        course.setStatus(courseDTO.getStatus() != null ? courseDTO.getStatus() : course.getStatus());
        course.setUpdatedAt(LocalDateTime.now());

        // Update outcomes
        outcomeRepository.deleteByCourseId(course.getId());
        if (courseDTO.getOutcomes() != null && !courseDTO.getOutcomes().isEmpty()) {
            for (String outcomeText : courseDTO.getOutcomes()) {
                if (outcomeText != null && !outcomeText.trim().isEmpty()) {
                    CourseOutcome outcome = new CourseOutcome();
                    outcome.setCourse(course);
                    outcome.setOutcome(outcomeText.trim());
                    outcomeRepository.save(outcome);
                }
            }
        }

        // Update prerequisites
        prerequisiteRepository.deleteByCourseId(course.getId());
        if (courseDTO.getPrerequisites() != null && !courseDTO.getPrerequisites().isEmpty()) {
            for (String prerequisiteText : courseDTO.getPrerequisites()) {
                if (prerequisiteText != null && !prerequisiteText.trim().isEmpty()) {
                    CoursePrerequisite prerequisite = new CoursePrerequisite();
                    prerequisite.setCourse(course);
                    prerequisite.setPrerequisite(prerequisiteText.trim());
                    prerequisiteRepository.save(prerequisite);
                }
            }
        }

        // Update lessons
        lessonRepository.deleteByCourseId(course.getId());
        if (courseDTO.getLessons() != null && !courseDTO.getLessons().isEmpty()) {
            for (LessonDTO lessonDTO : courseDTO.getLessons()) {
                if (lessonDTO.getTitle() != null && !lessonDTO.getTitle().trim().isEmpty()) {
                    Lesson lesson = new Lesson();
                    lesson.setCourse(course);
                    lesson.setTitle(lessonDTO.getTitle());
                    lesson.setDescription(lessonDTO.getDescription() != null ? lessonDTO.getDescription() : "");
                    lesson.setDuration(lessonDTO.getDuration() != null ? lessonDTO.getDuration() : "45 min");
                    lesson.setVideoUrl(lessonDTO.getVideoUrl() != null ? lessonDTO.getVideoUrl() : "");
                    lessonRepository.save(lesson);
                }
            }
        }

        course = courseRepository.save(course);
        return convertToDTO(course);
    }

    public void deleteCourse(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        // Delete related data (cascading should handle this, but being explicit)
        outcomeRepository.deleteByCourseId(id);
        prerequisiteRepository.deleteByCourseId(id);
        lessonRepository.deleteByCourseId(id);

        courseRepository.delete(course);
    }

    private CourseDTO convertToDTO(Course course) {
        CourseDTO dto = new CourseDTO();
        dto.setId(course.getId());
        dto.setTitle(course.getTitle());
        dto.setDescription(course.getDescription());
        dto.setThumbnail(course.getThumbnail());
        dto.setCategory(course.getCategory());
        dto.setLevel(course.getLevel());
        dto.setPrice(course.getPrice());
        dto.setOriginalPrice(course.getOriginalPrice());
        dto.setStatus(course.getStatus());
        dto.setInstructorId(course.getInstructor().getId());
        dto.setInstructorName(course.getInstructor().getName());
        dto.setInstructorAvatar(course.getInstructor().getAvatar());
        dto.setRating(course.getRating());
        dto.setEnrolled(course.getEnrolled());
        dto.setCreatedAt(course.getCreatedAt());
        dto.setUpdatedAt(course.getUpdatedAt());

        // Convert outcomes
        List<String> outcomes = outcomeRepository.findByCourseId(course.getId())
                .stream()
                .map(CourseOutcome::getOutcome)
                .collect(Collectors.toList());
        dto.setOutcomes(outcomes);

        // Convert prerequisites
        List<String> prerequisites = prerequisiteRepository.findByCourseId(course.getId())
                .stream()
                .map(CoursePrerequisite::getPrerequisite)
                .collect(Collectors.toList());
        dto.setPrerequisites(prerequisites);

        // Convert lessons
        List<LessonDTO> lessons = lessonRepository.findByCourseId(course.getId())
                .stream()
                .map(this::convertLessonToDTO)
                .collect(Collectors.toList());
        dto.setLessons(lessons);

        return dto;
    }

    private LessonDTO convertLessonToDTO(Lesson lesson) {
        LessonDTO dto = new LessonDTO();
        dto.setId(lesson.getId());
        dto.setTitle(lesson.getTitle());
        dto.setDescription(lesson.getDescription());
        dto.setDuration(lesson.getDuration());
        dto.setVideoUrl(lesson.getVideoUrl());
        return dto;
    }
}
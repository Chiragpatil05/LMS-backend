//package com.edulearn.controller;
//
//import com.edulearn.dto.CourseDTO;
//import com.edulearn.service.CourseService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import javax.validation.Valid;
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/courses")
//public class CourseController {
//
//    @Autowired
//    private CourseService courseService;
//
//    @PostMapping
//    public ResponseEntity<CourseDTO> createCourse(@Valid @RequestBody CourseDTO course) {
//        CourseDTO newCourse = courseService.createCourse(course);
//        return ResponseEntity.ok(newCourse);
//    }
//
//    @GetMapping
//    public ResponseEntity<List<CourseDTO>> getCourses(
//            @RequestParam(required = false) Long instructorId,
//            @RequestParam(required = false) String search
//    ) {
//        if (instructorId != null) {
//            return ResponseEntity.ok(courseService.findByInstructor(instructorId));
//        } else if (search != null && !search.trim().isEmpty()) {
//            return ResponseEntity.ok(courseService.searchCourses(search));
//        } else {
//            return ResponseEntity.ok(courseService.findActiveCourses());
//        }
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<CourseDTO> getCourse(@PathVariable Long id) {
//        return courseService.findById(id)
//                .map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }
//
////    @PutMapping("/{id}")
////    public ResponseEntity<CourseDTO> updateCourse(
////        @PathVariable Long id,
////        @Valid @RequestBody CourseDTO course
////    ) {
////        return courseService.findById(id)
////            .map(existingCourse -> {
////                course.setId(id);
////                return ResponseEntity.ok(courseService.updateCourse(course));
////            })
////            .orElse(ResponseEntity.notFound().build());
////    }
////}
//
//    @PutMapping("/{id}")
//    public ResponseEntity<CourseDTO> updateCourse(
//            @PathVariable Long id,
//            @Valid @RequestBody CourseDTO course
//    ) {
//        if (!id.equals(course.getId())) {
//            return ResponseEntity.badRequest().build();
//        }
//        try {
//            CourseDTO updatedCourse = courseService.updateCourse(course);
//            return ResponseEntity.ok(updatedCourse);
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.notFound().build();
//        }
//    }
//}


package com.edulearn.controller;

import com.edulearn.dto.CourseDTO;
import com.edulearn.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @PostMapping
    public ResponseEntity<CourseDTO> createCourse(@Valid @RequestBody CourseDTO course) {
        try {
            CourseDTO newCourse = courseService.createCourse(course);
            return ResponseEntity.ok(newCourse);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<CourseDTO>> getCourses(
            @RequestParam(required = false) Long instructorId,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status
    ) {
        try {
            List<CourseDTO> courses;

            if (instructorId != null) {
                courses = courseService.findByInstructor(instructorId);
            } else if (search != null && !search.trim().isEmpty()) {
                courses = courseService.searchCourses(search);
            } else if (status != null && !status.trim().isEmpty()) {
                courses = courseService.findByStatus(status);
            } else {
                courses = courseService.findAllCourses();
            }

            return ResponseEntity.ok(courses);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDTO> getCourse(@PathVariable Long id) {
        try {
            return courseService.findById(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseDTO> updateCourse(
            @PathVariable Long id,
            @Valid @RequestBody CourseDTO course
    ) {
        try {
            return courseService.findById(id)
                    .map(existingCourse -> {
                        course.setId(id);
                        CourseDTO updatedCourse = courseService.updateCourse(course);
                        return ResponseEntity.ok(updatedCourse);
                    })
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable Long id) {
        try {
            return courseService.findById(id)
                    .map(course -> {
                        courseService.deleteCourse(id);
                        return ResponseEntity.ok().build();
                    })
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/categories")
    public ResponseEntity<List<String>> getCategories() {
        List<String> categories = List.of(
                "web-development",
                "mobile-development",
                "data-science",
                "ui-ux-design",
                "programming",
                "business",
                "marketing"
        );
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/levels")
    public ResponseEntity<List<String>> getLevels() {
        List<String> levels = List.of("beginner", "intermediate", "advanced");
        return ResponseEntity.ok(levels);
    }
}
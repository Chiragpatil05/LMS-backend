package com.edulearn.controller;

import com.edulearn.dto.LessonDTO;
import com.edulearn.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/lessons")
public class LessonController {

    @Autowired
    private LessonService lessonService;

//    @PostMapping
//    public ResponseEntity<LessonDTO> createLesson(@Valid @RequestBody LessonDTO lesson) {
//        LessonDTO newLesson = lessonService.createLesson(lesson);
//        return ResponseEntity.ok(newLesson);
//    }

    @PostMapping("/courses/{courseId}/lessons")
    public ResponseEntity<LessonDTO> createLesson(
            @PathVariable Long courseId,
            @Valid @RequestBody LessonDTO lesson) {

        lesson.setCourseId(courseId); // Set course ID from path
        LessonDTO newLesson = lessonService.createLesson(lesson);
        return ResponseEntity.ok(newLesson);
    }


    @GetMapping
    public ResponseEntity<List<LessonDTO>> getLessons(@RequestParam Long courseId) {
        List<LessonDTO> lessons = lessonService.findByCourse(courseId);
        return ResponseEntity.ok(lessons);
    }

    // iska kaam nhi hai
//    @GetMapping("/{id}")
//    public ResponseEntity<LessonDTO> getLesson(@PathVariable Long id) {
//        return lessonService.findById(id)
//                .map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }

    @PutMapping("/{id}")
    public ResponseEntity<LessonDTO> updateLesson(
            @PathVariable Long id,
            @Valid @RequestBody LessonDTO lesson
    ) {
        return lessonService.findById(id)
                .map(existingLesson -> {
                    lesson.setId(id);
                    return ResponseEntity.ok(lessonService.updateLesson(lesson));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLesson(@PathVariable Long id) {
        return lessonService.findById(id)
                .map(lesson -> {
                    lessonService.deleteLesson(id);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
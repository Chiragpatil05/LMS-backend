package com.edulearn.service;

import com.edulearn.dto.LessonDTO;
import com.edulearn.model.Course;
import com.edulearn.model.Lesson;
import com.edulearn.repository.CourseRepository;
import com.edulearn.repository.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class LessonService {

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private CourseRepository courseRepository;

    public LessonDTO createLesson(LessonDTO lessonDTO) {
        Lesson lesson = new Lesson();
        lesson.setTitle(lessonDTO.getTitle());
        lesson.setDescription(lessonDTO.getDescription());
        lesson.setDuration(lessonDTO.getDuration());
        lesson.setVideoUrl(lessonDTO.getVideoUrl());

//
        Course course = courseRepository.findById(lessonDTO.getCourseId())
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));
        lesson.setCourse(course);

        lesson = lessonRepository.save(lesson);
        return convertToDTO(lesson);
    }

    public Optional<LessonDTO> findById(Long id) {
        return lessonRepository.findById(id).map(this::convertToDTO);
    }

    public List<LessonDTO> findByCourse(Long courseId) {
        return lessonRepository.findByCourseId(courseId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public LessonDTO updateLesson(LessonDTO lessonDTO) {
        Lesson lesson = lessonRepository.findById(lessonDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("Lesson not found"));

        lesson.setTitle(lessonDTO.getTitle());
        lesson.setDescription(lessonDTO.getDescription());
        lesson.setDuration(lessonDTO.getDuration());
        lesson.setVideoUrl(lessonDTO.getVideoUrl());

        lesson = lessonRepository.save(lesson);
        return convertToDTO(lesson);
    }

    public void deleteLesson(Long id) {
        lessonRepository.deleteById(id);
    }

    private LessonDTO convertToDTO(Lesson lesson) {
        LessonDTO dto = new LessonDTO();
        dto.setId(lesson.getId());
        dto.setTitle(lesson.getTitle());
        dto.setDescription(lesson.getDescription());
        dto.setDuration(lesson.getDuration());
        dto.setVideoUrl(lesson.getVideoUrl());
        dto.setCourseId(lesson.getCourse().getId());
        return dto;
    }
}
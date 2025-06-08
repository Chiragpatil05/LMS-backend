package com.edulearn.controller;

import com.edulearn.dto.QuizDTO;
import com.edulearn.dto.QuizAttemptDTO;
import com.edulearn.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    @Autowired
    private QuizService quizService;

    @PostMapping
    public ResponseEntity<QuizDTO> createQuiz(@Valid @RequestBody QuizDTO quiz) {
        try {
            QuizDTO newQuiz = quizService.createQuiz(quiz);
            return ResponseEntity.ok(newQuiz);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<QuizDTO>> getQuizzes(
            @RequestParam(required = false) Long courseId,
            @RequestParam(required = false) List<Long> courseIds
    ) {
        if (courseId != null) {
            List<QuizDTO> quizzes = quizService.findByCourse(courseId);
            return ResponseEntity.ok(quizzes);
        } else if (courseIds != null && !courseIds.isEmpty()) {
            List<QuizDTO> quizzes = quizService.findByEnrolledCourses(courseIds);
            return ResponseEntity.ok(quizzes);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuizDTO> getQuiz(@PathVariable Long id) {
        return quizService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuizDTO> updateQuiz(
            @PathVariable Long id,
            @Valid @RequestBody QuizDTO quiz
    ) {
        return (ResponseEntity<QuizDTO>) quizService.findById(id)
                .map(existingQuiz -> {
                    quiz.setId(id);
                    try {
                        return ResponseEntity.ok(quizService.updateQuiz(quiz));
                    } catch (IllegalArgumentException e) {
                        return ResponseEntity.<QuizDTO>badRequest().build();
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteQuiz(@PathVariable Long id) {
        return quizService.findById(id)
                .map(quiz -> {
                    quizService.deleteQuiz(id);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/attempt")
    public ResponseEntity<QuizAttemptDTO> submitAttempt(
            @PathVariable Long id,
            @Valid @RequestBody QuizAttemptDTO attempt
    ) {
        attempt.setQuizId(id);
        try {
            QuizAttemptDTO newAttempt = quizService.submitAttempt(attempt);
            return ResponseEntity.ok(newAttempt);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}/attempts")
    public ResponseEntity<List<QuizAttemptDTO>> getAttempts(@PathVariable Long id) {
        List<QuizAttemptDTO> attempts = quizService.getAttempts(id);
        return ResponseEntity.ok(attempts);
    }

    @GetMapping("/attempts/student/{studentId}")
    public ResponseEntity<List<QuizAttemptDTO>> getStudentAttempts(@PathVariable Long studentId) {
        List<QuizAttemptDTO> attempts = quizService.getStudentAttempts(studentId);
        return ResponseEntity.ok(attempts);
    }

    @GetMapping("/{quizId}/attempts/student/{studentId}")
    public ResponseEntity<QuizAttemptDTO> getStudentAttempt(
            @PathVariable Long quizId,
            @PathVariable Long studentId
    ) {
        return quizService.getStudentAttempt(quizId, studentId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
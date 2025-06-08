package com.edulearn.service;

import com.edulearn.dto.*;
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
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuizAttemptRepository attemptRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuestionOptionRepository optionRepository;

    @Autowired
    private QuizAnswerRepository answerRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    public QuizDTO createQuiz(QuizDTO quizDTO) {
        Quiz quiz = new Quiz();
        quiz.setTitle(quizDTO.getTitle());
        quiz.setDescription(quizDTO.getDescription());
        quiz.setTimeLimit(quizDTO.getTimeLimit());
        quiz.setTotalPoints(quizDTO.getTotalPoints());
        quiz.setPassingScore(quizDTO.getPassingScore());
        quiz.setCreatedAt(LocalDateTime.now());

        Course course = courseRepository.findById(quizDTO.getCourseId())
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));
        quiz.setCourse(course);

        quiz = quizRepository.save(quiz);

        // Save questions
        if (quizDTO.getQuestions() != null) {
            for (QuestionDTO questionDTO : quizDTO.getQuestions()) {
                Question question = new Question();
                question.setQuiz(quiz);
                question.setType(questionDTO.getType());
                question.setText(questionDTO.getText());
                question.setCorrectAnswer(questionDTO.getCorrectAnswer());
                question = questionRepository.save(question);

                // Save options for multiple choice questions
                if (questionDTO.getOptions() != null) {
                    for (String optionText : questionDTO.getOptions()) {
                        QuestionOption option = new QuestionOption();
                        option.setQuestion(question);
                        option.setOptionText(optionText);
                        optionRepository.save(option);
                    }
                }
            }
        }

        return convertToDTO(quiz);
    }

    public Optional<QuizDTO> findById(Long id) {
        return quizRepository.findById(id).map(this::convertToDTO);
    }

    public List<QuizDTO> findByCourse(Long courseId) {
        return quizRepository.findByCourseId(courseId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<QuizDTO> findByEnrolledCourses(List<Long> courseIds) {
        return quizRepository.findByCourseIdIn(courseIds)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public QuizDTO updateQuiz(QuizDTO quizDTO) {
        Quiz quiz = quizRepository.findById(quizDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("Quiz not found"));

        quiz.setTitle(quizDTO.getTitle());
        quiz.setDescription(quizDTO.getDescription());
        quiz.setTimeLimit(quizDTO.getTimeLimit());
        quiz.setTotalPoints(quizDTO.getTotalPoints());
        quiz.setPassingScore(quizDTO.getPassingScore());

        // Delete existing questions and options
        List<Question> existingQuestions = questionRepository.findByQuizId(quiz.getId());
        for (Question question : existingQuestions) {
            optionRepository.deleteByQuestionId(question.getId());
        }
        questionRepository.deleteByQuizId(quiz.getId());

        quiz = quizRepository.save(quiz);

        // Save new questions
        if (quizDTO.getQuestions() != null) {
            for (QuestionDTO questionDTO : quizDTO.getQuestions()) {
                Question question = new Question();
                question.setQuiz(quiz);
                question.setType(questionDTO.getType());
                question.setText(questionDTO.getText());
                question.setCorrectAnswer(questionDTO.getCorrectAnswer());
                question = questionRepository.save(question);

                // Save options for multiple choice questions
                if (questionDTO.getOptions() != null) {
                    for (String optionText : questionDTO.getOptions()) {
                        QuestionOption option = new QuestionOption();
                        option.setQuestion(question);
                        option.setOptionText(optionText);
                        optionRepository.save(option);
                    }
                }
            }
        }

        return convertToDTO(quiz);
    }

    public void deleteQuiz(Long id) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Quiz not found"));

        // Delete all related data
        List<Question> questions = questionRepository.findByQuizId(id);
        for (Question question : questions) {
            optionRepository.deleteByQuestionId(question.getId());
        }
        questionRepository.deleteByQuizId(id);

        List<QuizAttempt> attempts = attemptRepository.findByQuizId(id);
        for (QuizAttempt attempt : attempts) {
            answerRepository.deleteByAttemptId(attempt.getId());
        }

        quizRepository.deleteById(id);
    }

    public QuizAttemptDTO submitAttempt(QuizAttemptDTO attemptDTO) {
        if (attemptRepository.existsByQuizIdAndStudentId(attemptDTO.getQuizId(), attemptDTO.getStudentId())) {
            throw new IllegalArgumentException("Student has already attempted this quiz");
        }

        QuizAttempt attempt = new QuizAttempt();
        attempt.setAttemptDate(LocalDateTime.now());
        attempt.setScore(attemptDTO.getScore());
        attempt.setTimeSpent(attemptDTO.getTimeSpent());

        Quiz quiz = quizRepository.findById(attemptDTO.getQuizId())
                .orElseThrow(() -> new IllegalArgumentException("Quiz not found"));
        attempt.setQuiz(quiz);

        User student = userRepository.findById(attemptDTO.getStudentId())
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        attempt.setStudent(student);

        attempt = attemptRepository.save(attempt);

        // Save answers
        if (attemptDTO.getAnswers() != null) {
            for (QuizAnswerDTO answerDTO : attemptDTO.getAnswers()) {
                QuizAnswer answer = new QuizAnswer();
                answer.setAttempt(attempt);
                answer.setAnswer(answerDTO.getAnswer());

                Question question = questionRepository.findById(answerDTO.getQuestionId())
                        .orElseThrow(() -> new IllegalArgumentException("Question not found"));
                answer.setQuestion(question);

                answerRepository.save(answer);
            }
        }

        return convertAttemptToDTO(attempt);
    }

    public List<QuizAttemptDTO> getAttempts(Long quizId) {
        return attemptRepository.findByQuizId(quizId)
                .stream()
                .map(this::convertAttemptToDTO)
                .collect(Collectors.toList());
    }

    public List<QuizAttemptDTO> getStudentAttempts(Long studentId) {
        return attemptRepository.findByStudentId(studentId)
                .stream()
                .map(this::convertAttemptToDTO)
                .collect(Collectors.toList());
    }

    public Optional<QuizAttemptDTO> getStudentAttempt(Long quizId, Long studentId) {
        return attemptRepository.findByQuizId(quizId)
                .stream()
                .filter(attempt -> attempt.getStudent().getId().equals(studentId))
                .findFirst()
                .map(this::convertAttemptToDTO);
    }

    private QuizDTO convertToDTO(Quiz quiz) {
        QuizDTO dto = new QuizDTO();
        dto.setId(quiz.getId());
        dto.setCourseId(quiz.getCourse().getId());
        dto.setTitle(quiz.getTitle());
        dto.setDescription(quiz.getDescription());
        dto.setTimeLimit(quiz.getTimeLimit());
        dto.setTotalPoints(quiz.getTotalPoints());
        dto.setPassingScore(quiz.getPassingScore());
        dto.setCreatedAt(quiz.getCreatedAt());

        List<QuestionDTO> questions = questionRepository.findByQuizId(quiz.getId())
                .stream()
                .map(this::convertQuestionToDTO)
                .collect(Collectors.toList());
        dto.setQuestions(questions);

        List<QuizAttemptDTO> attempts = quiz.getAttempts()
                .stream()
                .map(this::convertAttemptToDTO)
                .collect(Collectors.toList());
        dto.setAttempts(attempts);

        return dto;
    }

    private QuestionDTO convertQuestionToDTO(Question question) {
        QuestionDTO dto = new QuestionDTO();
        dto.setId(question.getId());
        dto.setType(question.getType());
        dto.setText(question.getText());
        dto.setCorrectAnswer(question.getCorrectAnswer());

        List<String> options = optionRepository.findByQuestionId(question.getId())
                .stream()
                .map(QuestionOption::getOptionText)
                .collect(Collectors.toList());
        dto.setOptions(options);

        return dto;
    }

    private QuizAttemptDTO convertAttemptToDTO(QuizAttempt attempt) {
        QuizAttemptDTO dto = new QuizAttemptDTO();
        dto.setId(attempt.getId());
        dto.setQuizId(attempt.getQuiz().getId());
        dto.setStudentId(attempt.getStudent().getId());
        dto.setAttemptDate(attempt.getAttemptDate());
        dto.setScore(attempt.getScore());
        dto.setTimeSpent(attempt.getTimeSpent());

        List<QuizAnswerDTO> answers = answerRepository.findByAttemptId(attempt.getId())
                .stream()
                .map(this::convertAnswerToDTO)
                .collect(Collectors.toList());
        dto.setAnswers(answers);

        return dto;
    }

    private QuizAnswerDTO convertAnswerToDTO(QuizAnswer answer) {
        QuizAnswerDTO dto = new QuizAnswerDTO();
        dto.setId(answer.getId());
        dto.setQuestionId(answer.getQuestion().getId());
        dto.setAnswer(answer.getAnswer());
        return dto;
    }
}
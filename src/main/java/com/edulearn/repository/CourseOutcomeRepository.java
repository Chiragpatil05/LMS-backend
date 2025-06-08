package com.edulearn.repository;

import com.edulearn.model.CourseOutcome;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseOutcomeRepository extends JpaRepository<CourseOutcome, Long> {
    List<CourseOutcome> findByCourseId(Long courseId);
    void deleteByCourseId(Long courseId);
}
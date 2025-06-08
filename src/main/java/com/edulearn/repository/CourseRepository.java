//package com.edulearn.repository;
//
//import com.edulearn.model.Course;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public interface CourseRepository extends JpaRepository<Course, Long> {
//    List<Course> findByInstructorId(Long instructorId);
//
//    @Query("SELECT c FROM Course c WHERE c.status = 'active'")
//    List<Course> findAllActiveCourses();
//
//    @Query("SELECT c FROM Course c WHERE c.status = 'active' AND " +
//           "(LOWER(c.title) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
//           "LOWER(c.description) LIKE LOWER(CONCAT('%', :search, '%')))")
//    List<Course> searchCourses(@Param("search") String search);
//
//    List<Course> findByStatus(String status);
//}

package com.edulearn.repository;

import com.edulearn.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByInstructorId(Long instructorId);

    List<Course> findByStatus(String status);

    @Query("SELECT c FROM Course c WHERE " +
            "LOWER(c.title) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(c.description) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(c.category) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Course> searchCourses(@Param("search") String search);

    List<Course> findByCategory(String category);

    List<Course> findByLevel(String level);

    @Query("SELECT c FROM Course c WHERE c.status = 'active' ORDER BY c.enrolled DESC")
    List<Course> findPopularCourses();

    @Query("SELECT c FROM Course c WHERE c.status = 'active' ORDER BY c.createdAt DESC")
    List<Course> findRecentCourses();

    @Query("SELECT COUNT(c) FROM Course c WHERE c.instructor.id = :instructorId")
    Long countByInstructorId(@Param("instructorId") Long instructorId);
}
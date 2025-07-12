package com.lms.repository;

import com.lms.entity.Assessment;
import com.lms.entity.Course;
import com.lms.entity.CourseContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssessmentRepository extends JpaRepository<Assessment, Long> {
    
    List<Assessment> findByCourse(Course course);
    
    List<Assessment> findByCourseAndIsActive(Course course, Boolean isActive);
    
    List<Assessment> findByContent(CourseContent content);
    
    List<Assessment> findByIsActive(Boolean isActive);
    
    @Query("SELECT a FROM Assessment a WHERE a.course = :course AND a.isActive = true AND (a.startDate IS NULL OR a.startDate <= CURRENT_TIMESTAMP) AND (a.endDate IS NULL OR a.endDate >= CURRENT_TIMESTAMP)")
    List<Assessment> findActiveAssessmentsForCourse(@Param("course") Course course);
    
    long countByCourse(Course course);
    
    long countByCourseAndIsActive(Course course, Boolean isActive);
}

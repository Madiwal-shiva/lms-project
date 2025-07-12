package com.lms.repository;

import com.lms.entity.Course;
import com.lms.entity.CourseContent;
import com.lms.entity.Progress;
import com.lms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProgressRepository extends JpaRepository<Progress, Long> {
    
    Optional<Progress> findByUserAndCourseAndContent(User user, Course course, CourseContent content);
    
    List<Progress> findByUser(User user);
    
    List<Progress> findByUserAndCourse(User user, Course course);
    
    List<Progress> findByCourse(Course course);
    
    List<Progress> findByContent(CourseContent content);
    
    List<Progress> findByUserAndIsCompleted(User user, Boolean isCompleted);
    
    @Query("SELECT AVG(p.completionPercentage) FROM Progress p WHERE p.user = :user AND p.course = :course")
    Double findAverageProgressByUserAndCourse(@Param("user") User user, @Param("course") Course course);
    
    long countByUserAndCourse(User user, Course course);
    
    long countByUserAndIsCompleted(User user, Boolean isCompleted);
}

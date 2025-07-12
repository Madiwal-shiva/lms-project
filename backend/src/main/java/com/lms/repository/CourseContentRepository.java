package com.lms.repository;

import com.lms.entity.Course;
import com.lms.entity.CourseContent;
import com.lms.enums.ContentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for CourseContent entity
 */
@Repository
public interface CourseContentRepository extends JpaRepository<CourseContent, Long> {
    
    List<CourseContent> findByCourse(Course course);
    
    List<CourseContent> findByCourseOrderByOrderIndex(Course course);
    
    List<CourseContent> findByContentType(ContentType contentType);
    
    List<CourseContent> findByCourseAndContentType(Course course, ContentType contentType);
    
    @Query("SELECT c FROM CourseContent c WHERE c.course = :course AND c.isFree = true ORDER BY c.orderIndex")
    List<CourseContent> findFreeCourseContent(@Param("course") Course course);
    
    long countByCourse(Course course);
    
    long countByCourseAndContentType(Course course, ContentType contentType);
}

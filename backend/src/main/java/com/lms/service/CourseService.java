package com.lms.service;

import com.lms.entity.Course;
import com.lms.entity.User;
import com.lms.enums.CourseStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for Course operations
 */
public interface CourseService {
    
    Course createCourse(Course course);
    
    Course updateCourse(Long id, Course course);
    
    Optional<Course> findById(Long id);
    
    Page<Course> getAllCourses(Pageable pageable);
    
    Page<Course> getCoursesByInstructor(User instructor, Pageable pageable);
    
    Page<Course> getCoursesByStatus(CourseStatus status, Pageable pageable);
    
    Page<Course> searchCourses(String searchTerm, Pageable pageable);
    
    Page<Course> getFeaturedCourses(Pageable pageable);
    
    Course publishCourse(Long id);
    
    Course unpublishCourse(Long id);
    
    void deleteCourse(Long id);
    
    List<Course> getCoursesWithAvailableSlots();
    
    CourseStatistics getCourseStatistics();
    
    class CourseStatistics {
        private long totalCourses;
        private long publishedCourses;
        private long draftCourses;
        private long totalEnrollments;
        
        public CourseStatistics(long totalCourses, long publishedCourses, long draftCourses, long totalEnrollments) {
            this.totalCourses = totalCourses;
            this.publishedCourses = publishedCourses;
            this.draftCourses = draftCourses;
            this.totalEnrollments = totalEnrollments;
        }
        
        // Getters and setters
        public long getTotalCourses() { return totalCourses; }
        public void setTotalCourses(long totalCourses) { this.totalCourses = totalCourses; }
        public long getPublishedCourses() { return publishedCourses; }
        public void setPublishedCourses(long publishedCourses) { this.publishedCourses = publishedCourses; }
        public long getDraftCourses() { return draftCourses; }
        public void setDraftCourses(long draftCourses) { this.draftCourses = draftCourses; }
        public long getTotalEnrollments() { return totalEnrollments; }
        public void setTotalEnrollments(long totalEnrollments) { this.totalEnrollments = totalEnrollments; }
    }
}

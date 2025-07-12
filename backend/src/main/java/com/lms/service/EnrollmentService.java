package com.lms.service;

import com.lms.entity.Course;
import com.lms.entity.Enrollment;
import com.lms.entity.User;
import com.lms.enums.EnrollmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface EnrollmentService {
    
    Enrollment enrollStudent(User student, Course course);
    
    Enrollment updateEnrollmentStatus(Long enrollmentId, EnrollmentStatus status);
    
    Optional<Enrollment> findById(Long id);
    
    Optional<Enrollment> findByStudentAndCourse(User student, Course course);
    
    Page<Enrollment> getEnrollmentsByStudent(User student, Pageable pageable);
    
    Page<Enrollment> getEnrollmentsByCourse(Course course, Pageable pageable);
    
    Page<Enrollment> getEnrollmentsByStatus(EnrollmentStatus status, Pageable pageable);
    
    List<Course> getEnrolledCourses(User student);
    
    List<User> getEnrolledStudents(Course course);
    
    boolean isStudentEnrolled(User student, Course course);
    
    void unenrollStudent(User student, Course course);
    
    long getEnrollmentCount(Course course);
    
    EnrollmentStatistics getEnrollmentStatistics();
    
    class EnrollmentStatistics {
        private long totalEnrollments;
        private long activeEnrollments;
        private long completedEnrollments;
        private long droppedEnrollments;
        
        public EnrollmentStatistics(long totalEnrollments, long activeEnrollments, long completedEnrollments, long droppedEnrollments) {
            this.totalEnrollments = totalEnrollments;
            this.activeEnrollments = activeEnrollments;
            this.completedEnrollments = completedEnrollments;
            this.droppedEnrollments = droppedEnrollments;
        }
        
        public long getTotalEnrollments() { return totalEnrollments; }
        public void setTotalEnrollments(long totalEnrollments) { this.totalEnrollments = totalEnrollments; }
        public long getActiveEnrollments() { return activeEnrollments; }
        public void setActiveEnrollments(long activeEnrollments) { this.activeEnrollments = activeEnrollments; }
        public long getCompletedEnrollments() { return completedEnrollments; }
        public void setCompletedEnrollments(long completedEnrollments) { this.completedEnrollments = completedEnrollments; }
        public long getDroppedEnrollments() { return droppedEnrollments; }
        public void setDroppedEnrollments(long droppedEnrollments) { this.droppedEnrollments = droppedEnrollments; }
    }
}
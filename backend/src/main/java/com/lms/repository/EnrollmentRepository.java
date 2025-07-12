package com.lms.repository;

import com.lms.entity.Course;
import com.lms.entity.Enrollment;
import com.lms.entity.User;
import com.lms.enums.EnrollmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Enrollment entity
 * 
 * @author LMS Team
 * @version 1.0
 */
@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    /**
     * Find enrollment by student and course
     * 
     * @param student The student user
     * @param course The course
     * @return Optional enrollment
     */
    Optional<Enrollment> findByStudentAndCourse(User student, Course course);

    /**
     * Find enrollments by student
     * 
     * @param student The student user
     * @return List of enrollments
     */
    List<Enrollment> findByStudent(User student);

    /**
     * Find enrollments by student with pagination
     * 
     * @param student The student user
     * @param pageable Pagination information
     * @return Page of enrollments
     */
    Page<Enrollment> findByStudent(User student, Pageable pageable);

    /**
     * Find enrollments by course
     * 
     * @param course The course
     * @return List of enrollments
     */
    List<Enrollment> findByCourse(Course course);

    /**
     * Find enrollments by course with pagination
     * 
     * @param course The course
     * @param pageable Pagination information
     * @return Page of enrollments
     */
    Page<Enrollment> findByCourse(Course course, Pageable pageable);

    /**
     * Find enrollments by status
     * 
     * @param status The enrollment status
     * @return List of enrollments
     */
    List<Enrollment> findByStatus(EnrollmentStatus status);

    /**
     * Find enrollments by status with pagination
     * 
     * @param status The enrollment status
     * @param pageable Pagination information
     * @return Page of enrollments
     */
    Page<Enrollment> findByStatus(EnrollmentStatus status, Pageable pageable);

    /**
     * Find courses by student
     * 
     * @param student The student user
     * @return List of courses
     */
    @Query("SELECT e.course FROM Enrollment e WHERE e.student = :student")
    List<Course> findCoursesByStudent(@Param("student") User student);

    /**
     * Find students by course
     * 
     * @param course The course
     * @return List of students
     */
    @Query("SELECT e.student FROM Enrollment e WHERE e.course = :course")
    List<User> findStudentsByCourse(@Param("course") Course course);

    /**
     * Find enrollments by student and status
     * 
     * @param student The student user
     * @param status The enrollment status
     * @return List of enrollments
     */
    List<Enrollment> findByStudentAndStatus(User student, EnrollmentStatus status);

    /**
     * Find enrollments by course and status
     * 
     * @param course The course
     * @param status The enrollment status
     * @return List of enrollments
     */
    List<Enrollment> findByCourseAndStatus(Course course, EnrollmentStatus status);

    /**
     * Find enrollments by progress percentage range
     * 
     * @param minProgress Minimum progress percentage
     * @param maxProgress Maximum progress percentage
     * @return List of enrollments
     */
    List<Enrollment> findByProgressPercentageBetween(Double minProgress, Double maxProgress);

    /**
     * Find completed enrollments by student
     * 
     * @param student The student user
     * @return List of completed enrollments
     */
    @Query("SELECT e FROM Enrollment e WHERE e.student = :student AND e.status = 'COMPLETED'")
    List<Enrollment> findCompletedEnrollmentsByStudent(@Param("student") User student);

    /**
     * Find active enrollments by student
     * 
     * @param student The student user
     * @return List of active enrollments
     */
    @Query("SELECT e FROM Enrollment e WHERE e.student = :student AND e.status = 'ACTIVE'")
    List<Enrollment> findActiveEnrollmentsByStudent(@Param("student") User student);

    /**
     * Find enrollments by enrollment date range
     * 
     * @param startDate Start date
     * @param endDate End date
     * @return List of enrollments
     */
    List<Enrollment> findByEnrollmentDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find enrollments by completion date range
     * 
     * @param startDate Start date
     * @param endDate End date
     * @return List of enrollments
     */
    List<Enrollment> findByCompletionDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Check if student is enrolled in course
     * 
     * @param student The student user
     * @param course The course
     * @return true if enrolled
     */
    boolean existsByStudentAndCourse(User student, Course course);

    /**
     * Count enrollments by course
     * 
     * @param course The course
     * @return Count of enrollments
     */
    long countByCourse(Course course);

    /**
     * Count enrollments by status
     * 
     * @param status The enrollment status
     * @return Count of enrollments
     */
    long countByStatus(EnrollmentStatus status);

    /**
     * Count enrollments by student
     * 
     * @param student The student user
     * @return Count of enrollments
     */
    long countByStudent(User student);

    /**
     * Find enrollments by instructor (through course)
     * 
     * @param instructor The instructor user
     * @return List of enrollments
     */
    @Query("SELECT e FROM Enrollment e WHERE e.course.instructor = :instructor")
    List<Enrollment> findByInstructor(@Param("instructor") User instructor);

    /**
     * Find recent enrollments
     * 
     * @param since Date since when to find enrollments
     * @param pageable Pagination information
     * @return Page of recent enrollments
     */
    Page<Enrollment> findByEnrollmentDateAfter(LocalDateTime since, Pageable pageable);

    /**
     * Find enrollments with high progress
     * 
     * @param minProgress Minimum progress percentage
     * @param pageable Pagination information
     * @return Page of enrollments with high progress
     */
    Page<Enrollment> findByProgressPercentageGreaterThanEqual(Double minProgress, Pageable pageable);

    /**
     * Find student's enrollment statistics
     * 
     * @param student The student user
     * @return List of enrollment statistics
     */
    @Query("SELECT e.status, COUNT(e) FROM Enrollment e WHERE e.student = :student GROUP BY e.status")
    List<Object[]> findEnrollmentStatisticsByStudent(@Param("student") User student);

    /**
     * Find course enrollment statistics
     * 
     * @param course The course
     * @return List of enrollment statistics
     */
    @Query("SELECT e.status, COUNT(e) FROM Enrollment e WHERE e.course = :course GROUP BY e.status")
    List<Object[]> findEnrollmentStatisticsByCourse(@Param("course") Course course);
}

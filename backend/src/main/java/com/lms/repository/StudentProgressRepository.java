package com.lms.repository;

import com.lms.entity.Course;
import com.lms.entity.StudentProgress;
import com.lms.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentProgressRepository extends JpaRepository<StudentProgress, Long> {

    List<StudentProgress> findByStudentAndCourse(User student, Course course);
    
    Optional<StudentProgress> findByStudentAndCourseAndContentId(User student, Course course, String contentId);
    
    List<StudentProgress> findByStudent(User student);
    
    Page<StudentProgress> findByStudent(User student, Pageable pageable);
    
    List<StudentProgress> findByCourse(Course course);
    
    Page<StudentProgress> findByCourse(Course course, Pageable pageable);
    
    List<StudentProgress> findByLastAccessedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT sp FROM StudentProgress sp WHERE sp.course.instructor = :instructor")
    List<StudentProgress> findByInstructor(@Param("instructor") User instructor);
    
    @Query("SELECT sp FROM StudentProgress sp WHERE sp.student.city = :city OR sp.student.country = :country")
    List<StudentProgress> findByStudentCityOrStudentCountry(@Param("city") String city, @Param("country") String country);
    
    @Query("SELECT sp FROM StudentProgress sp WHERE YEAR(sp.lastAccessedAt) = :year")
    List<StudentProgress> findByYear(@Param("year") int year);
    
    long countByStudent(User student);
    
    long countByCourse(Course course);
    
    @Query("SELECT AVG(sp.progressPercentage) FROM StudentProgress sp WHERE sp.course = :course")
    Double findAverageProgressByCourse(@Param("course") Course course);
    
    @Query("SELECT AVG(sp.progressPercentage) FROM StudentProgress sp WHERE sp.student = :student")
    Double findAverageProgressByStudent(@Param("student") User student);
}

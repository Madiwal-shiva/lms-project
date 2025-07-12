package com.lms.controller;

import com.lms.entity.Course;
import com.lms.entity.Enrollment;
import com.lms.entity.User;
import com.lms.enums.EnrollmentStatus;
import com.lms.service.CourseService;
import com.lms.service.EnrollmentService;
import com.lms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enrollments")
@CrossOrigin(origins = "http://localhost:3000")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private UserService userService;

    @Autowired
    private CourseService courseService;

    @PostMapping("/enroll/{courseId}")
    public ResponseEntity<Enrollment> enrollInCourse(@PathVariable Long courseId, Authentication authentication) {
        User student = userService.findByEmail(authentication.getName()).orElse(null);
        Course course = courseService.findById(courseId).orElse(null);
        
        if (student == null || course == null) {
            return ResponseEntity.badRequest().build();
        }
        
        Enrollment enrollment = enrollmentService.enrollStudent(student, course);
        return ResponseEntity.ok(enrollment);
    }

    @DeleteMapping("/unenroll/{courseId}")
    public ResponseEntity<Void> unenrollFromCourse(@PathVariable Long courseId, Authentication authentication) {
        User student = userService.findByEmail(authentication.getName()).orElse(null);
        Course course = courseService.findById(courseId).orElse(null);
        
        if (student == null || course == null) {
            return ResponseEntity.badRequest().build();
        }
        
        enrollmentService.unenrollStudent(student, course);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/my-courses")
    public ResponseEntity<List<Course>> getMyEnrolledCourses(Authentication authentication) {
        User student = userService.findByEmail(authentication.getName()).orElse(null);
        if (student == null) {
            return ResponseEntity.badRequest().build();
        }
        
        List<Course> enrolledCourses = enrollmentService.getEnrolledCourses(student);
        return ResponseEntity.ok(enrolledCourses);
    }

    @GetMapping("/course/{courseId}/students")
    public ResponseEntity<List<User>> getCourseStudents(@PathVariable Long courseId) {
        Course course = courseService.findById(courseId).orElse(null);
        if (course == null) {
            return ResponseEntity.notFound().build();
        }
        
        List<User> enrolledStudents = enrollmentService.getEnrolledStudents(course);
        return ResponseEntity.ok(enrolledStudents);
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<Page<Enrollment>> getStudentEnrollments(
            @PathVariable Long studentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        User student = userService.findById(studentId).orElse(null);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Enrollment> enrollments = enrollmentService.getEnrollmentsByStudent(student, pageable);
        return ResponseEntity.ok(enrollments);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<Page<Enrollment>> getCourseEnrollments(
            @PathVariable Long courseId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Course course = courseService.findById(courseId).orElse(null);
        if (course == null) {
            return ResponseEntity.notFound().build();
        }
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Enrollment> enrollments = enrollmentService.getEnrollmentsByCourse(course, pageable);
        return ResponseEntity.ok(enrollments);
    }

    @PutMapping("/{enrollmentId}/status")
    public ResponseEntity<Enrollment> updateEnrollmentStatus(
            @PathVariable Long enrollmentId,
            @RequestParam EnrollmentStatus status) {
        Enrollment updatedEnrollment = enrollmentService.updateEnrollmentStatus(enrollmentId, status);
        return ResponseEntity.ok(updatedEnrollment);
    }

    @GetMapping("/check/{courseId}")
    public ResponseEntity<Boolean> checkEnrollmentStatus(@PathVariable Long courseId, Authentication authentication) {
        User student = userService.findByEmail(authentication.getName()).orElse(null);
        Course course = courseService.findById(courseId).orElse(null);
        
        if (student == null || course == null) {
            return ResponseEntity.badRequest().build();
        }
        
        boolean isEnrolled = enrollmentService.isStudentEnrolled(student, course);
        return ResponseEntity.ok(isEnrolled);
    }

    @GetMapping("/statistics")
    public ResponseEntity<EnrollmentService.EnrollmentStatistics> getEnrollmentStatistics() {
        return ResponseEntity.ok(enrollmentService.getEnrollmentStatistics());
    }
}
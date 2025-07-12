package com.lms.controller;

import com.lms.entity.Course;
import com.lms.entity.StudentProgress;
import com.lms.entity.User;
import com.lms.service.CourseService;
import com.lms.service.StudentProgressService;
import com.lms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/progress")
@CrossOrigin(origins = "http://localhost:3000")
public class ProgressController {

    @Autowired
    private StudentProgressService progressService;

    @Autowired
    private UserService userService;

    @Autowired
    private CourseService courseService;

    @PostMapping
    public ResponseEntity<StudentProgress> saveProgress(@RequestBody StudentProgress progress, Authentication authentication) {
        User student = userService.findByEmail(authentication.getName()).orElse(null);
        if (student != null) {
            progress.setStudent(student);
        }
        StudentProgress savedProgress = progressService.saveProgress(progress);
        return ResponseEntity.ok(savedProgress);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<StudentProgress>> getCourseProgress(
            @PathVariable Long courseId, 
            Authentication authentication) {
        User student = userService.findByEmail(authentication.getName()).orElse(null);
        Course course = courseService.findById(courseId).orElse(null);
        
        if (student == null || course == null) {
            return ResponseEntity.badRequest().build();
        }
        
        List<StudentProgress> progress = progressService.getStudentProgress(student, course);
        return ResponseEntity.ok(progress);
    }

    @GetMapping("/course/{courseId}/percentage")
    public ResponseEntity<Double> getCourseProgressPercentage(
            @PathVariable Long courseId, 
            Authentication authentication) {
        User student = userService.findByEmail(authentication.getName()).orElse(null);
        Course course = courseService.findById(courseId).orElse(null);
        
        if (student == null || course == null) {
            return ResponseEntity.badRequest().build();
        }
        
        Double progressPercentage = progressService.calculateCourseProgress(student, course);
        return ResponseEntity.ok(progressPercentage);
    }

    @GetMapping("/instructor/statistics")
    public ResponseEntity<Map<String, Object>> getInstructorStatistics(Authentication authentication) {
        User instructor = userService.findByEmail(authentication.getName()).orElse(null);
        if (instructor == null || !instructor.isInstructor()) {
            return ResponseEntity.badRequest().build();
        }
        
        Map<String, Object> statistics = progressService.getProgressStatistics(instructor);
        return ResponseEntity.ok(statistics);
    }

    @GetMapping("/instructor/students")
    public ResponseEntity<Page<StudentProgress>> getStudentsProgress(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long courseId,
            Authentication authentication) {
        
        User instructor = userService.findByEmail(authentication.getName()).orElse(null);
        if (instructor == null || !instructor.isInstructor()) {
            return ResponseEntity.badRequest().build();
        }
        
        Pageable pageable = PageRequest.of(page, size);
        
        if (courseId != null) {
            Course course = courseService.findById(courseId).orElse(null);
            if (course != null) {
                Page<StudentProgress> progress = progressService.getProgressByCourse(course, pageable);
                return ResponseEntity.ok(progress);
            }
        }
        
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/analytics/date-range")
    public ResponseEntity<List<StudentProgress>> getProgressByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            Authentication authentication) {
        
        User instructor = userService.findByEmail(authentication.getName()).orElse(null);
        if (instructor == null || !instructor.isInstructor()) {
            return ResponseEntity.badRequest().build();
        }
        
        List<StudentProgress> progress = progressService.getProgressByDateRange(startDate, endDate);
        return ResponseEntity.ok(progress);
    }

    @GetMapping("/analytics/location")
    public ResponseEntity<List<StudentProgress>> getProgressByLocation(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String country,
            Authentication authentication) {
        
        User instructor = userService.findByEmail(authentication.getName()).orElse(null);
        if (instructor == null || !instructor.isInstructor()) {
            return ResponseEntity.badRequest().build();
        }
        
        List<StudentProgress> progress = progressService.getProgressByLocation(city, country);
        return ResponseEntity.ok(progress);
    }

    @GetMapping("/analytics/monthly/{year}")
    public ResponseEntity<Map<String, Long>> getMonthlyProgress(
            @PathVariable int year,
            Authentication authentication) {
        
        User instructor = userService.findByEmail(authentication.getName()).orElse(null);
        if (instructor == null || !instructor.isInstructor()) {
            return ResponseEntity.badRequest().build();
        }
        
        Map<String, Long> monthlyProgress = progressService.getProgressByMonth(year);
        return ResponseEntity.ok(monthlyProgress);
    }
}
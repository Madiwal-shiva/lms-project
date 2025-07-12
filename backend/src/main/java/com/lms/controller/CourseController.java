package com.lms.controller;

import com.lms.dto.CourseCreateDTO;
import com.lms.entity.Course;
import com.lms.entity.User;
import com.lms.enums.CourseStatus;
import com.lms.service.CourseService;
import com.lms.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
@CrossOrigin(origins = "http://localhost:3000")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<Page<Course>> getAllCourses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(courseService.getAllCourses(pageable));
    }

    @GetMapping("/published")
    public ResponseEntity<Page<Course>> getPublishedCourses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(courseService.getCoursesByStatus(CourseStatus.PUBLISHED, pageable));
    }

    @GetMapping("/featured")
    public ResponseEntity<Page<Course>> getFeaturedCourses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(courseService.getFeaturedCourses(pageable));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Course>> searchCourses(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(courseService.searchCourses(query, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
        return courseService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Course> createCourse(@Valid @RequestBody CourseCreateDTO courseDTO, Authentication authentication) {
        User instructor = userService.findByEmail(authentication.getName()).orElse(null);
        if (instructor == null) {
            return ResponseEntity.badRequest().build();
        }
        
        Course course = new Course();
        course.setTitle(courseDTO.getTitle());
        course.setDescription(courseDTO.getDescription());
        course.setShortDescription(courseDTO.getShortDescription());
        course.setCategory(courseDTO.getCategory());
        course.setLevel(courseDTO.getLevel());
        course.setLanguage(courseDTO.getLanguage());
        course.setDurationHours(courseDTO.getDurationHours());
        course.setMaxStudents(courseDTO.getMaxStudents());
        course.setPrerequisites(courseDTO.getPrerequisites());
        course.setLearningObjectives(courseDTO.getLearningObjectives());
        course.setTags(courseDTO.getTags());
        course.setInstructor(instructor);
        
        Course createdCourse = courseService.createCourse(course);
        return ResponseEntity.ok(createdCourse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable Long id, @RequestBody Course course) {
        Course updatedCourse = courseService.updateCourse(id, course);
        return ResponseEntity.ok(updatedCourse);
    }

    @PutMapping("/{id}/publish")
    public ResponseEntity<Course> publishCourse(@PathVariable Long id) {
        Course publishedCourse = courseService.publishCourse(id);
        return ResponseEntity.ok(publishedCourse);
    }

    @PutMapping("/{id}/unpublish")
    public ResponseEntity<Course> unpublishCourse(@PathVariable Long id) {
        Course unpublishedCourse = courseService.unpublishCourse(id);
        return ResponseEntity.ok(unpublishedCourse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/statistics")
    public ResponseEntity<CourseService.CourseStatistics> getCourseStatistics() {
        return ResponseEntity.ok(courseService.getCourseStatistics());
    }

    @GetMapping("/available-slots")
    public ResponseEntity<List<Course>> getCoursesWithAvailableSlots() {
        return ResponseEntity.ok(courseService.getCoursesWithAvailableSlots());
    }
}
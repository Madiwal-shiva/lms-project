package com.lms.service.impl;

import com.lms.entity.Course;
import com.lms.entity.User;
import com.lms.enums.CourseStatus;
import com.lms.exception.ResourceNotFoundException;
import com.lms.repository.CourseRepository;
import com.lms.repository.EnrollmentRepository;
import com.lms.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Override
    public Course createCourse(Course course) {
        course.setStatus(CourseStatus.DRAFT);
        course.setCreatedAt(LocalDateTime.now());
        return courseRepository.save(course);
    }

    @Override
    public Course updateCourse(Long id, Course course) {
        Course existingCourse = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
        
        existingCourse.setTitle(course.getTitle());
        existingCourse.setDescription(course.getDescription());
        existingCourse.setDuration(course.getDuration());
        existingCourse.setMaxStudents(course.getMaxStudents());
        existingCourse.setUpdatedAt(LocalDateTime.now());
        
        return courseRepository.save(existingCourse);
    }

    @Override
    public Optional<Course> findById(Long id) {
        return courseRepository.findById(id);
    }

    @Override
    public Page<Course> getAllCourses(Pageable pageable) {
        return courseRepository.findAll(pageable);
    }

    @Override
    public Page<Course> getCoursesByInstructor(User instructor, Pageable pageable) {
        return courseRepository.findByInstructor(instructor, pageable);
    }

    @Override
    public Page<Course> getCoursesByStatus(CourseStatus status, Pageable pageable) {
        return courseRepository.findByStatus(status, pageable);
    }

    @Override
    public Page<Course> searchCourses(String searchTerm, Pageable pageable) {
        return courseRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(searchTerm, searchTerm, pageable);
    }

    @Override
    public Page<Course> getFeaturedCourses(Pageable pageable) {
        return courseRepository.findByFeaturedTrueAndStatus(CourseStatus.PUBLISHED, pageable);
    }

    @Override
    public Course publishCourse(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
        course.setStatus(CourseStatus.PUBLISHED);
        course.setUpdatedAt(LocalDateTime.now());
        return courseRepository.save(course);
    }

    @Override
    public Course unpublishCourse(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
        course.setStatus(CourseStatus.DRAFT);
        course.setUpdatedAt(LocalDateTime.now());
        return courseRepository.save(course);
    }

    @Override
    public void deleteCourse(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Course not found with id: " + id);
        }
        courseRepository.deleteById(id);
    }

    @Override
    public List<Course> getCoursesWithAvailableSlots() {
        return courseRepository.findCoursesWithAvailableSlots();
    }

    @Override
    public CourseStatistics getCourseStatistics() {
        long totalCourses = courseRepository.count();
        long publishedCourses = courseRepository.countByStatus(CourseStatus.PUBLISHED);
        long draftCourses = courseRepository.countByStatus(CourseStatus.DRAFT);
        long totalEnrollments = enrollmentRepository.count();
        
        return new CourseStatistics(totalCourses, publishedCourses, draftCourses, totalEnrollments);
    }
}
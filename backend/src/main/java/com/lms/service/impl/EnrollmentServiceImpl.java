package com.lms.service.impl;

import com.lms.entity.Course;
import com.lms.entity.Enrollment;
import com.lms.entity.User;
import com.lms.enums.EnrollmentStatus;
import com.lms.exception.ResourceNotFoundException;
import com.lms.repository.EnrollmentRepository;
import com.lms.service.EnrollmentService;
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
public class EnrollmentServiceImpl implements EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Override
    public Enrollment enrollStudent(User student, Course course) {
        Optional<Enrollment> existingEnrollment = enrollmentRepository.findByStudentAndCourse(student, course);
        if (existingEnrollment.isPresent()) {
            throw new RuntimeException("Student is already enrolled in this course");
        }
        
        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setStatus(EnrollmentStatus.ACTIVE);
        enrollment.setEnrollmentDate(LocalDateTime.now());
        
        return enrollmentRepository.save(enrollment);
    }

    @Override
    public Enrollment updateEnrollmentStatus(Long enrollmentId, EnrollmentStatus status) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found with id: " + enrollmentId));
        
        enrollment.setStatus(status);
        if (status == EnrollmentStatus.COMPLETED) {
            enrollment.setCompletionDate(LocalDateTime.now());
        }
        
        return enrollmentRepository.save(enrollment);
    }

    @Override
    public Optional<Enrollment> findById(Long id) {
        return enrollmentRepository.findById(id);
    }

    @Override
    public Optional<Enrollment> findByStudentAndCourse(User student, Course course) {
        return enrollmentRepository.findByStudentAndCourse(student, course);
    }

    @Override
    public Page<Enrollment> getEnrollmentsByStudent(User student, Pageable pageable) {
        return enrollmentRepository.findByStudent(student, pageable);
    }

    @Override
    public Page<Enrollment> getEnrollmentsByCourse(Course course, Pageable pageable) {
        return enrollmentRepository.findByCourse(course, pageable);
    }

    @Override
    public Page<Enrollment> getEnrollmentsByStatus(EnrollmentStatus status, Pageable pageable) {
        return enrollmentRepository.findByStatus(status, pageable);
    }

    @Override
    public List<Course> getEnrolledCourses(User student) {
        return enrollmentRepository.findCoursesByStudent(student);
    }

    @Override
    public List<User> getEnrolledStudents(Course course) {
        return enrollmentRepository.findStudentsByCourse(course);
    }

    @Override
    public boolean isStudentEnrolled(User student, Course course) {
        return enrollmentRepository.findByStudentAndCourse(student, course).isPresent();
    }

    @Override
    public void unenrollStudent(User student, Course course) {
        Optional<Enrollment> enrollment = enrollmentRepository.findByStudentAndCourse(student, course);
        if (enrollment.isPresent()) {
            enrollment.get().setStatus(EnrollmentStatus.DROPPED);
            enrollmentRepository.save(enrollment.get());
        }
    }

    @Override
    public long getEnrollmentCount(Course course) {
        return enrollmentRepository.countByCourse(course);
    }

    @Override
    public EnrollmentStatistics getEnrollmentStatistics() {
        long totalEnrollments = enrollmentRepository.count();
        long activeEnrollments = enrollmentRepository.countByStatus(EnrollmentStatus.ACTIVE);
        long completedEnrollments = enrollmentRepository.countByStatus(EnrollmentStatus.COMPLETED);
        long droppedEnrollments = enrollmentRepository.countByStatus(EnrollmentStatus.DROPPED);
        
        return new EnrollmentStatistics(totalEnrollments, activeEnrollments, completedEnrollments, droppedEnrollments);
    }
}
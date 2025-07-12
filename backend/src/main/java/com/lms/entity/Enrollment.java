package com.lms.entity;

import com.lms.enums.EnrollmentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/**
 * Enrollment entity representing student course enrollments
 * 
 * @author LMS Team
 * @version 1.0
 */
@Entity
@Table(name = "enrollments")
public class Enrollment extends BaseEntity {

    @NotNull(message = "Student is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @NotNull(message = "Course is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private EnrollmentStatus status = EnrollmentStatus.ACTIVE;

    @Column(name = "enrolled_at", nullable = false)
    private LocalDateTime enrolledAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "progress_percentage", nullable = false)
    private Double progressPercentage = 0.0;

    @Column(name = "last_accessed")
    private LocalDateTime lastAccessed;

    @Column(name = "certificate_url")
    private String certificateUrl;

    /**
     * Default constructor
     */
    public Enrollment() {
        super();
        this.enrolledAt = LocalDateTime.now();
    }

    /**
     * Constructor with essential fields
     * 
     * @param student The student user
     * @param course The course
     */
    public Enrollment(User student, Course course) {
        this();
        this.student = student;
        this.course = course;
    }

    /**
     * Checks if the enrollment is active
     * 
     * @return true if enrollment is active
     */
    public boolean isActive() {
        return status == EnrollmentStatus.ACTIVE;
    }

    /**
     * Checks if the enrollment is completed
     * 
     * @return true if enrollment is completed
     */
    public boolean isCompleted() {
        return status == EnrollmentStatus.COMPLETED;
    }

    /**
     * Marks the enrollment as completed
     */
    public void markAsCompleted() {
        this.status = EnrollmentStatus.COMPLETED;
        this.completedAt = LocalDateTime.now();
        this.progressPercentage = 100.0;
    }

    /**
     * Updates the progress percentage
     * 
     * @param percentage The new progress percentage
     */
    public void updateProgress(Double percentage) {
        this.progressPercentage = Math.min(100.0, Math.max(0.0, percentage));
        this.lastAccessed = LocalDateTime.now();
        
        if (this.progressPercentage >= 100.0) {
            markAsCompleted();
        }
    }

    // Getters and Setters
    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public EnrollmentStatus getStatus() {
        return status;
    }

    public void setStatus(EnrollmentStatus status) {
        this.status = status;
    }

    public LocalDateTime getEnrolledAt() {
        return enrolledAt;
    }

    public void setEnrolledAt(LocalDateTime enrolledAt) {
        this.enrolledAt = enrolledAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public Double getProgressPercentage() {
        return progressPercentage;
    }

    public void setProgressPercentage(Double progressPercentage) {
        this.progressPercentage = progressPercentage;
    }

    public LocalDateTime getLastAccessed() {
        return lastAccessed;
    }

    public void setLastAccessed(LocalDateTime lastAccessed) {
        this.lastAccessed = lastAccessed;
    }

    public String getCertificateUrl() {
        return certificateUrl;
    }

    public void setCertificateUrl(String certificateUrl) {
        this.certificateUrl = certificateUrl;
    }

    @Override
    public String toString() {
        return "Enrollment{" +
                "id=" + getId() +
                ", student=" + (student != null ? student.getFullName() : "null") +
                ", course=" + (course != null ? course.getTitle() : "null") +
                ", status=" + status +
                ", progressPercentage=" + progressPercentage +
                "}";
    }
}

package com.lms.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/**
 * LearningPathEnrollment entity representing user enrollments in learning paths
 * 
 * @author LMS Team
 * @version 1.0
 */
@Entity
@Table(name = "learning_path_enrollments")
public class LearningPathEnrollment extends BaseEntity {

    @NotNull(message = "User is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull(message = "Learning path is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "learning_path_id", nullable = false)
    private LearningPath learningPath;

    @Column(name = "enrollment_date", nullable = false)
    private LocalDateTime enrollmentDate;

    @Column(name = "progress_percentage", nullable = false)
    private Double progressPercentage = 0.0;

    @Column(name = "completion_date")
    private LocalDateTime completionDate;

    /**
     * Default constructor
     */
    public LearningPathEnrollment() {
        super();
        this.enrollmentDate = LocalDateTime.now();
    }

    /**
     * Constructor with essential fields
     * 
     * @param user The user
     * @param learningPath The learning path
     */
    public LearningPathEnrollment(User user, LearningPath learningPath) {
        this();
        this.user = user;
        this.learningPath = learningPath;
    }

    /**
     * Completes the learning path enrollment
     */
    public void complete() {
        this.completionDate = LocalDateTime.now();
        this.progressPercentage = 100.0;
        this.learningPath.incrementCompletionCount();
    }

    /**
     * Updates the progress percentage
     * 
     * @param percentage The new progress percentage
     */
    public void updateProgress(Double percentage) {
        this.progressPercentage = Math.min(100.0, Math.max(0.0, percentage));
        
        if (this.progressPercentage >= 100.0) {
            complete();
        }
    }

    // Getters and Setters
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LearningPath getLearningPath() {
        return learningPath;
    }

    public void setLearningPath(LearningPath learningPath) {
        this.learningPath = learningPath;
    }

    public LocalDateTime getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(LocalDateTime enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public Double getProgressPercentage() {
        return progressPercentage;
    }

    public void setProgressPercentage(Double progressPercentage) {
        this.progressPercentage = progressPercentage;
    }

    public LocalDateTime getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(LocalDateTime completionDate) {
        this.completionDate = completionDate;
    }

    @Override
    public String toString() {
        return "LearningPathEnrollment{" +
                "id=" + getId() +
                ", user=" + (user != null ? user.getFullName() : "null") +
                ", learningPath=" + (learningPath != null ? learningPath.getTitle() : "null") +
                ", progressPercentage=" + progressPercentage +
                "}";
    }
}

package com.lms.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/**
 * Progress entity representing user progress in course content
 * 
 * @author LMS Team
 * @version 1.0
 */
@Entity
@Table(name = "progress")
public class Progress extends BaseEntity {

    @NotNull(message = "User is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull(message = "Course is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @NotNull(message = "Course content is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id", nullable = false)
    private CourseContent content;

    @Column(name = "is_completed", nullable = false)
    private Boolean isCompleted = false;

    @Column(name = "completion_percentage", nullable = false)
    private Double completionPercentage = 0.0;

    @Column(name = "time_spent_minutes")
    private Integer timeSpentMinutes = 0;

    @Column(name = "last_accessed")
    private LocalDateTime lastAccessed;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    /**
     * Default constructor
     */
    public Progress() {
        super();
    }

    /**
     * Constructor with essential fields
     * 
     * @param user The user
     * @param course The course
     * @param content The course content
     */
    public Progress(User user, Course course, CourseContent content) {
        this();
        this.user = user;
        this.course = course;
        this.content = content;
        this.lastAccessed = LocalDateTime.now();
    }

    /**
     * Marks the progress as completed
     */
    public void markAsCompleted() {
        this.isCompleted = true;
        this.completionPercentage = 100.0;
        this.completedAt = LocalDateTime.now();
        this.lastAccessed = LocalDateTime.now();
    }

    /**
     * Updates the completion percentage
     * 
     * @param percentage The new completion percentage
     */
    public void updateCompletionPercentage(Double percentage) {
        this.completionPercentage = Math.min(100.0, Math.max(0.0, percentage));
        this.lastAccessed = LocalDateTime.now();
        
        if (this.completionPercentage >= 100.0) {
            markAsCompleted();
        }
    }

    /**
     * Adds time spent on this content
     * 
     * @param minutes Minutes to add
     */
    public void addTimeSpent(Integer minutes) {
        this.timeSpentMinutes += minutes;
        this.lastAccessed = LocalDateTime.now();
    }

    // Getters and Setters
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public CourseContent getContent() {
        return content;
    }

    public void setContent(CourseContent content) {
        this.content = content;
    }

    public Boolean getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public Double getCompletionPercentage() {
        return completionPercentage;
    }

    public void setCompletionPercentage(Double completionPercentage) {
        this.completionPercentage = completionPercentage;
    }

    public Integer getTimeSpentMinutes() {
        return timeSpentMinutes;
    }

    public void setTimeSpentMinutes(Integer timeSpentMinutes) {
        this.timeSpentMinutes = timeSpentMinutes;
    }

    public LocalDateTime getLastAccessed() {
        return lastAccessed;
    }

    public void setLastAccessed(LocalDateTime lastAccessed) {
        this.lastAccessed = lastAccessed;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "Progress{" +
                "id=" + getId() +
                ", user=" + (user != null ? user.getFullName() : "null") +
                ", course=" + (course != null ? course.getTitle() : "null") +
                ", content=" + (content != null ? content.getTitle() : "null") +
                ", isCompleted=" + isCompleted +
                ", completionPercentage=" + completionPercentage +
                "}";
    }
}

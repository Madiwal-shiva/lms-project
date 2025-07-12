package com.lms.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "student_progress", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"student_id", "module_id"})
})
public class StudentProgress extends BaseEntity {

    @NotNull(message = "Student is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @NotNull(message = "Learning module is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id", nullable = false)
    private LearningModule module;

    @Column(name = "current_section")
    private Integer currentSection = 0;

    @Column(name = "completed_objectives", columnDefinition = "JSON")
    private String completedObjectives;

    @Column(name = "quiz_scores", columnDefinition = "JSON")
    private String quizScores;

    @Column(name = "time_spent")
    private Integer timeSpent = 0;

    @Column(name = "last_accessed")
    private LocalDateTime lastAccessed;

    @Column(name = "notes", columnDefinition = "JSON")
    private String notes;

    @Column(name = "bookmarks", columnDefinition = "JSON")
    private String bookmarks;

    @Column(name = "completion_percentage")
    private Double completionPercentage = 0.0;

    @Column(name = "is_completed")
    private Boolean isCompleted = false;

    @Column(name = "completion_date")
    private LocalDateTime completionDate;

    /**
     * Default constructor
     */
    public StudentProgress() {
        super();
    }

    /**
     * Constructor with essential fields
     *
     * @param student Student
     * @param module Learning module
     */
    public StudentProgress(User student, LearningModule module) {
        this.student = student;
        this.module = module;
        this.lastAccessed = LocalDateTime.now();
    }

    // Getters and Setters

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public LearningModule getModule() {
        return module;
    }

    public void setModule(LearningModule module) {
        this.module = module;
    }

    public Integer getCurrentSection() {
        return currentSection;
    }

    public void setCurrentSection(Integer currentSection) {
        this.currentSection = currentSection;
    }

    public String getCompletedObjectives() {
        return completedObjectives;
    }

    public void setCompletedObjectives(String completedObjectives) {
        this.completedObjectives = completedObjectives;
    }

    public String getQuizScores() {
        return quizScores;
    }

    public void setQuizScores(String quizScores) {
        this.quizScores = quizScores;
    }

    public Integer getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(Integer timeSpent) {
        this.timeSpent = timeSpent;
    }

    public LocalDateTime getLastAccessed() {
        return lastAccessed;
    }

    public void setLastAccessed(LocalDateTime lastAccessed) {
        this.lastAccessed = lastAccessed;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getBookmarks() {
        return bookmarks;
    }

    public void setBookmarks(String bookmarks) {
        this.bookmarks = bookmarks;
    }

    public Double getCompletionPercentage() {
        return completionPercentage;
    }

    public void setCompletionPercentage(Double completionPercentage) {
        this.completionPercentage = completionPercentage;
    }

    public Boolean getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
        if (isCompleted && completionDate == null) {
            this.completionDate = LocalDateTime.now();
        }
    }

    public LocalDateTime getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(LocalDateTime completionDate) {
        this.completionDate = completionDate;
    }

    @Override
    public String toString() {
        return "StudentProgress{" +
                "id=" + getId() +
                ", student=" + (student != null ? student.getFullName() : "null") +
                ", module=" + (module != null ? module.getTitle() : "null") +
                ", currentSection=" + currentSection +
                ", completionPercentage=" + completionPercentage +
                ", isCompleted=" + isCompleted +
                '}';
    }
}

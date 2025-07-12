package com.lms.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Assessment entity representing quizzes and assessments
 * 
 * @author LMS Team
 * @version 1.0
 */
@Entity
@Table(name = "assessments")
public class Assessment extends BaseEntity {

    @NotBlank(message = "Assessment title is required")
    @Size(max = 200, message = "Assessment title must not exceed 200 characters")
    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @NotNull(message = "Course is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id")
    private CourseContent content;

    @Column(name = "time_limit_minutes")
    private Integer timeLimitMinutes;

    @Column(name = "max_attempts")
    private Integer maxAttempts;

    @Column(name = "passing_score")
    private Double passingScore;

    @Column(name = "total_points")
    private Double totalPoints;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "instructions", columnDefinition = "TEXT")
    private String instructions;

    @OneToMany(mappedBy = "assessment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Question> questions = new HashSet<>();

    @OneToMany(mappedBy = "assessment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<AssessmentAttempt> attempts = new HashSet<>();

    /**
     * Default constructor
     */
    public Assessment() {
        super();
    }

    /**
     * Constructor with essential fields
     * 
     * @param title Assessment title
     * @param course The course
     */
    public Assessment(String title, Course course) {
        this();
        this.title = title;
        this.course = course;
    }

    /**
     * Checks if the assessment is currently active
     * 
     * @return true if assessment is active and within date range
     */
    public boolean isCurrentlyActive() {
        if (!isActive) {
            return false;
        }
        
        LocalDateTime now = LocalDateTime.now();
        if (startDate != null && now.isBefore(startDate)) {
            return false;
        }
        
        if (endDate != null && now.isAfter(endDate)) {
            return false;
        }
        
        return true;
    }

    /**
     * Gets the number of questions in this assessment
     * 
     * @return number of questions
     */
    public int getQuestionCount() {
        return questions.size();
    }

    /**
     * Calculates the percentage score
     * 
     * @param score The score obtained
     * @return percentage score
     */
    public Double calculatePercentage(Double score) {
        if (totalPoints == null || totalPoints == 0) {
            return 0.0;
        }
        return (score / totalPoints) * 100.0;
    }

    /**
     * Checks if a score is passing
     * 
     * @param score The score to check
     * @return true if score is passing
     */
    public boolean isPassing(Double score) {
        if (passingScore == null) {
            return true;
        }
        Double percentage = calculatePercentage(score);
        return percentage >= passingScore;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Integer getTimeLimitMinutes() {
        return timeLimitMinutes;
    }

    public void setTimeLimitMinutes(Integer timeLimitMinutes) {
        this.timeLimitMinutes = timeLimitMinutes;
    }

    public Integer getMaxAttempts() {
        return maxAttempts;
    }

    public void setMaxAttempts(Integer maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    public Double getPassingScore() {
        return passingScore;
    }

    public void setPassingScore(Double passingScore) {
        this.passingScore = passingScore;
    }

    public Double getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(Double totalPoints) {
        this.totalPoints = totalPoints;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public Set<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(Set<Question> questions) {
        this.questions = questions;
    }

    public Set<AssessmentAttempt> getAttempts() {
        return attempts;
    }

    public void setAttempts(Set<AssessmentAttempt> attempts) {
        this.attempts = attempts;
    }

    @Override
    public String toString() {
        return "Assessment{" +
                "id=" + getId() +
                ", title='" + title + '\'' +
                ", course=" + (course != null ? course.getTitle() : "null") +
                ", totalPoints=" + totalPoints +
                ", passingScore=" + passingScore +
                ", isActive=" + isActive +
                "}";
    }
}

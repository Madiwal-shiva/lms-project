package com.lms.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * AssessmentAttempt entity representing student attempts at assessments
 * 
 * @author LMS Team
 * @version 1.0
 */
@Entity
@Table(name = "assessment_attempts")
public class AssessmentAttempt extends BaseEntity {

    @NotNull(message = "Assessment is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assessment_id", nullable = false)
    private Assessment assessment;

    @NotNull(message = "Student is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @Column(name = "attempt_number", nullable = false)
    private Integer attemptNumber;

    @Column(name = "started_at", nullable = false)
    private LocalDateTime startedAt;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @Column(name = "score")
    private Double score;

    @Column(name = "max_score")
    private Double maxScore;

    @Column(name = "percentage")
    private Double percentage;

    @Column(name = "is_passed")
    private Boolean isPassed;

    @Column(name = "time_taken_minutes")
    private Integer timeTakenMinutes;

    @Column(name = "status", nullable = false)
    private String status = "IN_PROGRESS";

    @OneToMany(mappedBy = "attempt", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<StudentAnswer> answers = new HashSet<>();

    /**
     * Default constructor
     */
    public AssessmentAttempt() {
        super();
        this.startedAt = LocalDateTime.now();
    }

    /**
     * Constructor with essential fields
     * 
     * @param assessment The assessment
     * @param student The student
     * @param attemptNumber The attempt number
     */
    public AssessmentAttempt(Assessment assessment, User student, Integer attemptNumber) {
        this();
        this.assessment = assessment;
        this.student = student;
        this.attemptNumber = attemptNumber;
    }

    /**
     * Submits the assessment attempt
     */
    public void submit() {
        this.submittedAt = LocalDateTime.now();
        this.status = "SUBMITTED";
        calculateTimeTaken();
    }

    /**
     * Calculates and sets the score based on answers
     */
    public void calculateScore() {
        double totalScore = 0.0;
        double maxPossibleScore = 0.0;
        
        for (StudentAnswer answer : answers) {
            if (answer.getQuestion() != null) {
                maxPossibleScore += answer.getQuestion().getPoints();
                if (answer.getIsCorrect()) {
                    totalScore += answer.getQuestion().getPoints();
                }
            }
        }
        
        this.score = totalScore;
        this.maxScore = maxPossibleScore;
        
        if (maxPossibleScore > 0) {
            this.percentage = (totalScore / maxPossibleScore) * 100.0;
        } else {
            this.percentage = 0.0;
        }
        
        // Check if passed based on assessment passing score
        if (assessment != null && assessment.getPassingScore() != null) {
            this.isPassed = this.percentage >= assessment.getPassingScore();
        } else {
            this.isPassed = true; // No passing score defined
        }
    }

    /**
     * Calculates the time taken for the attempt
     */
    private void calculateTimeTaken() {
        if (startedAt != null && submittedAt != null) {
            long minutes = java.time.Duration.between(startedAt, submittedAt).toMinutes();
            this.timeTakenMinutes = (int) minutes;
        }
    }

    /**
     * Checks if the attempt is in progress
     * 
     * @return true if in progress
     */
    public boolean isInProgress() {
        return "IN_PROGRESS".equals(status);
    }

    /**
     * Checks if the attempt is submitted
     * 
     * @return true if submitted
     */
    public boolean isSubmitted() {
        return "SUBMITTED".equals(status);
    }

    // Getters and Setters
    public Assessment getAssessment() {
        return assessment;
    }

    public void setAssessment(Assessment assessment) {
        this.assessment = assessment;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public Integer getAttemptNumber() {
        return attemptNumber;
    }

    public void setAttemptNumber(Integer attemptNumber) {
        this.attemptNumber = attemptNumber;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Double getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(Double maxScore) {
        this.maxScore = maxScore;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public Boolean getIsPassed() {
        return isPassed;
    }

    public void setIsPassed(Boolean isPassed) {
        this.isPassed = isPassed;
    }

    public Integer getTimeTakenMinutes() {
        return timeTakenMinutes;
    }

    public void setTimeTakenMinutes(Integer timeTakenMinutes) {
        this.timeTakenMinutes = timeTakenMinutes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Set<StudentAnswer> getAnswers() {
        return answers;
    }

    public void setAnswers(Set<StudentAnswer> answers) {
        this.answers = answers;
    }

    @Override
    public String toString() {
        return "AssessmentAttempt{" +
                "id=" + getId() +
                ", assessment=" + (assessment != null ? assessment.getTitle() : "null") +
                ", student=" + (student != null ? student.getFullName() : "null") +
                ", attemptNumber=" + attemptNumber +
                ", score=" + score +
                ", percentage=" + percentage +
                ", status='" + status + '\'' +
                "}";
    }
}

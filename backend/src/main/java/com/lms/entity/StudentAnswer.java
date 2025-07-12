package com.lms.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

/**
 * StudentAnswer entity representing student answers to questions
 * 
 * @author LMS Team
 * @version 1.0
 */
@Entity
@Table(name = "student_answers")
public class StudentAnswer extends BaseEntity {

    @NotNull(message = "Assessment attempt is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attempt_id", nullable = false)
    private AssessmentAttempt attempt;

    @NotNull(message = "Question is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "selected_option_id")
    private QuestionOption selectedOption;

    @Column(name = "answer_text", columnDefinition = "TEXT")
    private String answerText;

    @Column(name = "is_correct")
    private Boolean isCorrect;

    @Column(name = "points_earned")
    private Double pointsEarned;

    /**
     * Default constructor
     */
    public StudentAnswer() {
        super();
    }

    /**
     * Constructor with essential fields
     * 
     * @param attempt The assessment attempt
     * @param question The question
     */
    public StudentAnswer(AssessmentAttempt attempt, Question question) {
        this();
        this.attempt = attempt;
        this.question = question;
    }

    /**
     * Constructor for multiple choice answers
     * 
     * @param attempt The assessment attempt
     * @param question The question
     * @param selectedOption The selected option
     */
    public StudentAnswer(AssessmentAttempt attempt, Question question, QuestionOption selectedOption) {
        this(attempt, question);
        this.selectedOption = selectedOption;
        this.isCorrect = selectedOption.getIsCorrect();
        this.pointsEarned = this.isCorrect ? question.getPoints() : 0.0;
    }

    /**
     * Constructor for text answers
     * 
     * @param attempt The assessment attempt
     * @param question The question
     * @param answerText The answer text
     */
    public StudentAnswer(AssessmentAttempt attempt, Question question, String answerText) {
        this(attempt, question);
        this.answerText = answerText;
        // Text answers need to be graded manually or by comparison logic
        this.isCorrect = false;
        this.pointsEarned = 0.0;
    }

    /**
     * Manually grades the answer
     * 
     * @param isCorrect Whether the answer is correct
     * @param pointsEarned Points earned for this answer
     */
    public void grade(Boolean isCorrect, Double pointsEarned) {
        this.isCorrect = isCorrect;
        this.pointsEarned = pointsEarned;
    }

    // Getters and Setters
    public AssessmentAttempt getAttempt() {
        return attempt;
    }

    public void setAttempt(AssessmentAttempt attempt) {
        this.attempt = attempt;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public QuestionOption getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(QuestionOption selectedOption) {
        this.selectedOption = selectedOption;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public Boolean getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(Boolean isCorrect) {
        this.isCorrect = isCorrect;
    }

    public Double getPointsEarned() {
        return pointsEarned;
    }

    public void setPointsEarned(Double pointsEarned) {
        this.pointsEarned = pointsEarned;
    }

    @Override
    public String toString() {
        return "StudentAnswer{" +
                "id=" + getId() +
                ", attempt=" + (attempt != null ? attempt.getId() : "null") +
                ", question=" + (question != null ? question.getId() : "null") +
                ", selectedOption=" + (selectedOption != null ? selectedOption.getId() : "null") +
                ", answerText='" + answerText + '\'' +
                ", isCorrect=" + isCorrect +
                ", pointsEarned=" + pointsEarned +
                "}";
    }
}

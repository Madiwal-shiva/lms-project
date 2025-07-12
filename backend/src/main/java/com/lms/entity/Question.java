package com.lms.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.HashSet;
import java.util.Set;

/**
 * Question entity representing assessment questions
 * 
 * @author LMS Team
 * @version 1.0
 */
@Entity
@Table(name = "questions")
public class Question extends BaseEntity {

    @NotNull(message = "Assessment is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assessment_id", nullable = false)
    private Assessment assessment;

    @NotBlank(message = "Question text is required")
    @Column(name = "question_text", columnDefinition = "TEXT")
    private String questionText;

    @Column(name = "question_type", nullable = false)
    private String questionType = "MULTIPLE_CHOICE";

    @Column(name = "points")
    private Double points = 1.0;

    @Column(name = "order_index")
    private Integer orderIndex;

    @Column(name = "explanation", columnDefinition = "TEXT")
    private String explanation;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<QuestionOption> options = new HashSet<>();

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<StudentAnswer> studentAnswers = new HashSet<>();

    /**
     * Default constructor
     */
    public Question() {
        super();
    }

    /**
     * Constructor with essential fields
     * 
     * @param assessment The assessment
     * @param questionText The question text
     * @param questionType The question type
     */
    public Question(Assessment assessment, String questionText, String questionType) {
        this();
        this.assessment = assessment;
        this.questionText = questionText;
        this.questionType = questionType;
    }

    /**
     * Checks if the question is multiple choice
     * 
     * @return true if multiple choice
     */
    public boolean isMultipleChoice() {
        return "MULTIPLE_CHOICE".equals(questionType);
    }

    /**
     * Checks if the question is true/false
     * 
     * @return true if true/false
     */
    public boolean isTrueFalse() {
        return "TRUE_FALSE".equals(questionType);
    }

    /**
     * Checks if the question is short answer
     * 
     * @return true if short answer
     */
    public boolean isShortAnswer() {
        return "SHORT_ANSWER".equals(questionType);
    }

    /**
     * Gets the correct options for this question
     * 
     * @return set of correct options
     */
    public Set<QuestionOption> getCorrectOptions() {
        Set<QuestionOption> correctOptions = new HashSet<>();
        for (QuestionOption option : options) {
            if (option.getIsCorrect()) {
                correctOptions.add(option);
            }
        }
        return correctOptions;
    }

    // Getters and Setters
    public Assessment getAssessment() {
        return assessment;
    }

    public void setAssessment(Assessment assessment) {
        this.assessment = assessment;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public Double getPoints() {
        return points;
    }

    public void setPoints(Double points) {
        this.points = points;
    }

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public Set<QuestionOption> getOptions() {
        return options;
    }

    public void setOptions(Set<QuestionOption> options) {
        this.options = options;
    }

    public Set<StudentAnswer> getStudentAnswers() {
        return studentAnswers;
    }

    public void setStudentAnswers(Set<StudentAnswer> studentAnswers) {
        this.studentAnswers = studentAnswers;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + getId() +
                ", assessment=" + (assessment != null ? assessment.getTitle() : "null") +
                ", questionType='" + questionType + '\'' +
                ", points=" + points +
                ", orderIndex=" + orderIndex +
                "}";
    }
}

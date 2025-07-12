package com.lms.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * QuestionOption entity representing answer options for questions
 * 
 * @author LMS Team
 * @version 1.0
 */
@Entity
@Table(name = "question_options")
public class QuestionOption extends BaseEntity {

    @NotNull(message = "Question is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @NotBlank(message = "Option text is required")
    @Column(name = "option_text", columnDefinition = "TEXT")
    private String optionText;

    @Column(name = "is_correct", nullable = false)
    private Boolean isCorrect = false;

    @Column(name = "order_index")
    private Integer orderIndex;

    @Column(name = "explanation", columnDefinition = "TEXT")
    private String explanation;

    /**
     * Default constructor
     */
    public QuestionOption() {
        super();
    }

    /**
     * Constructor with essential fields
     * 
     * @param question The question
     * @param optionText The option text
     * @param isCorrect Whether this option is correct
     */
    public QuestionOption(Question question, String optionText, Boolean isCorrect) {
        this();
        this.question = question;
        this.optionText = optionText;
        this.isCorrect = isCorrect;
    }

    // Getters and Setters
    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public String getOptionText() {
        return optionText;
    }

    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }

    public Boolean getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(Boolean isCorrect) {
        this.isCorrect = isCorrect;
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

    @Override
    public String toString() {
        return "QuestionOption{" +
                "id=" + getId() +
                ", question=" + (question != null ? question.getId() : "null") +
                ", optionText='" + optionText + '\'' +
                ", isCorrect=" + isCorrect +
                ", orderIndex=" + orderIndex +
                "}";
    }
}

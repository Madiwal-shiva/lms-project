package com.lms.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(name = "quiz_questions")
public class QuizQuestion extends BaseEntity {

    @Column(name = "question", columnDefinition = "TEXT", nullable = false)
    private String question;

    @Column(name = "question_type", nullable = false)
    private String questionType;

    @Column(name = "options", columnDefinition = "JSON")
    private String options;

    @Column(name = "correct_answer", columnDefinition = "TEXT")
    private String correctAnswer;

    @Column(name = "explanation", columnDefinition = "TEXT")
    private String explanation;

    @Column(name = "points")
    private Integer points = 10;

    @Column(name = "difficulty", length = 20)
    private String difficulty = "easy";

    @Column(name = "hints", columnDefinition = "JSON")
    private String hints;

    @Column(name = "order_index")
    private Integer orderIndex;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id")
    private LearningModule module;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id")
    private LearningSection section;

    /**
     * Default constructor
     */
    public QuizQuestion() {
        super();
    }

    /**
     * Constructor with essential fields
     *
     * @param question Question text
     * @param questionType Question type
     * @param correctAnswer Correct answer
     */
    public QuizQuestion(String question, String questionType, String correctAnswer) {
        this.question = question;
        this.questionType = questionType;
        this.correctAnswer = correctAnswer;
    }

    // Getters and Setters

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getHints() {
        return hints;
    }

    public void setHints(String hints) {
        this.hints = hints;
    }

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    public LearningModule getModule() {
        return module;
    }

    public void setModule(LearningModule module) {
        this.module = module;
    }

    public LearningSection getSection() {
        return section;
    }

    public void setSection(LearningSection section) {
        this.section = section;
    }

    @Override
    public String toString() {
        return "QuizQuestion{" +
                "id=" + getId() +
                ", questionType='" + questionType + '\'' +
                ", points=" + points +
                ", difficulty='" + difficulty + '\'' +
                '}';
    }
}

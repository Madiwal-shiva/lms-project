package com.lms.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(name = "learning_sections")
public class LearningSection extends BaseEntity {

    @NotBlank(message = "Section title is required")
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @NotNull(message = "Learning module is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id", nullable = false)
    private LearningModule module;

    @Column(name = "order_index", nullable = false)
    private Integer orderIndex;

    @Column(name = "estimated_time")
    private Integer estimatedTime;

    @Column(name = "is_required", nullable = false)
    private Boolean isRequired = true;

    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ContentBlock> contentBlocks;

    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<QuizQuestion> quizQuestions;

    /**
     * Default constructor
     */
    public LearningSection() {
        super();
    }

    /**
     * Constructor with essential fields
     *
     * @param title Section title
     * @param module Learning module
     * @param orderIndex Order index
     */
    public LearningSection(String title, LearningModule module, Integer orderIndex) {
        this.title = title;
        this.module = module;
        this.orderIndex = orderIndex;
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

    public LearningModule getModule() {
        return module;
    }

    public void setModule(LearningModule module) {
        this.module = module;
    }

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    public Integer getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(Integer estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public Boolean getIsRequired() {
        return isRequired;
    }

    public void setIsRequired(Boolean isRequired) {
        this.isRequired = isRequired;
    }

    public Set<ContentBlock> getContentBlocks() {
        return contentBlocks;
    }

    public void setContentBlocks(Set<ContentBlock> contentBlocks) {
        this.contentBlocks = contentBlocks;
    }

    public Set<QuizQuestion> getQuizQuestions() {
        return quizQuestions;
    }

    public void setQuizQuestions(Set<QuizQuestion> quizQuestions) {
        this.quizQuestions = quizQuestions;
    }

    @Override
    public String toString() {
        return "LearningSection{" +
                "id=" + getId() +
                ", title='" + title + '\'' +
                ", module=" + (module != null ? module.getTitle() : "null") +
                ", orderIndex=" + orderIndex +
                '}';
    }
}

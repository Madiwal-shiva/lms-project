package com.lms.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "learning_objectives")
public class LearningObjective extends BaseEntity {

    @NotBlank(message = "Objective title is required")
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

    /**
     * Default constructor
     */
    public LearningObjective() {
        super();
    }

    /**
     * Constructor with essential fields
     *
     * @param title Objective title
     * @param description Objective description
     * @param module Learning module
     */
    public LearningObjective(String title, String description, LearningModule module) {
        this.title = title;
        this.description = description;
        this.module = module;
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

    @Override
    public String toString() {
        return "LearningObjective{" +
                "id=" + getId() +
                ", title='" + title + '\'' +
                ", module=" + (module != null ? module.getTitle() : "null") +
                '}';
    }
}

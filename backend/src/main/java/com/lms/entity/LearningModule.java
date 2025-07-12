package com.lms.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(name = "learning_modules")
public class LearningModule extends BaseEntity {

    @NotBlank(message = "Module title is required")
    @Column(name = "title", nullable = false)
    private String title;

    @NotBlank(message = "Module description is required")
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "subject", length = 100)
    private String subject;

    @Column(name = "level", length = 25)
    private String level;

    @NotNull(message = "Estimated duration is required")
    @Column(name = "estimated_duration")
    private Integer estimatedDuration;

    @Column(name = "tags")
    private String tags;

    @NotNull(message = "Creator is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @Column(name = "is_published", nullable = false)
    private Boolean isPublished;

    @OneToMany(mappedBy = "module", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<LearningObjective> learningObjectives;

    @OneToMany(mappedBy = "module", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<LearningSection> sections;

    @OneToMany(mappedBy = "module", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<QuizQuestion> finalAssessment;

    /**
     * Default constructor
     */
    public LearningModule() {
        super();
    }

    /**
     * Constructor with essential fields
     *
     * @param title Module title
     * @param description Module description
     * @param createdBy Module creator
     */
    public LearningModule(String title, String description, User createdBy) {
        this.title = title;
        this.description = description;
        this.createdBy = createdBy;
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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Integer getEstimatedDuration() {
        return estimatedDuration;
    }

    public void setEstimatedDuration(Integer estimatedDuration) {
        this.estimatedDuration = estimatedDuration;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public Boolean getIsPublished() {
        return isPublished;
    }

    public void setIsPublished(Boolean isPublished) {
        this.isPublished = isPublished;
    }

    public Set<LearningObjective> getLearningObjectives() {
        return learningObjectives;
    }

    public void setLearningObjectives(Set<LearningObjective> learningObjectives) {
        this.learningObjectives = learningObjectives;
    }

    public Set<LearningSection> getSections() {
        return sections;
    }

    public void setSections(Set<LearningSection> sections) {
        this.sections = sections;
    }

    public Set<QuizQuestion> getFinalAssessment() {
        return finalAssessment;
    }

    public void setFinalAssessment(Set<QuizQuestion> finalAssessment) {
        this.finalAssessment = finalAssessment;
    }

    @Override
    public String toString() {
        return "LearningModule{" +
                "id=" + getId() +
                ", title='" + title + '\'' +
                ", createdBy=" + (createdBy != null ? createdBy.getFullName() : "null") +
                ", isPublished=" + isPublished +
                '}';
    }
}

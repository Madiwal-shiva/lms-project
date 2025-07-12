package com.lms.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;

/**
 * LearningPath entity representing structured learning paths
 * 
 * @author LMS Team
 * @version 1.0
 */
@Entity
@Table(name = "learning_paths")
public class LearningPath extends BaseEntity {

    @NotBlank(message = "Learning path title is required")
    @Size(max = 200, message = "Learning path title must not exceed 200 characters")
    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @NotBlank(message = "Learning path description is required")
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @NotNull(message = "Creator is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Column(name = "estimated_duration_hours")
    private Integer estimatedDurationHours;

    @Column(name = "difficulty_level", length = 50)
    private String difficultyLevel;

    @Column(name = "tags")
    private String tags;

    @Column(name = "is_published", nullable = false)
    private Boolean isPublished = false;

    @Column(name = "enrollment_count", nullable = false)
    private Integer enrollmentCount = 0;

    @Column(name = "completion_count", nullable = false)
    private Integer completionCount = 0;

    @ManyToMany(mappedBy = "learningPaths", fetch = FetchType.LAZY)
    private Set<Course> courses = new HashSet<>();

    @OneToMany(mappedBy = "learningPath", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<LearningPathEnrollment> enrollments = new HashSet<>();

    /**
     * Default constructor
     */
    public LearningPath() {
        super();
    }

    /**
     * Constructor with essential fields
     * 
     * @param title Learning path title
     * @param description Learning path description
     * @param creator The creator user
     */
    public LearningPath(String title, String description, User creator) {
        this();
        this.title = title;
        this.description = description;
        this.creator = creator;
    }

    /**
     * Publishes the learning path
     */
    public void publish() {
        this.isPublished = true;
    }

    /**
     * Unpublishes the learning path
     */
    public void unpublish() {
        this.isPublished = false;
    }

    /**
     * Increments the enrollment count
     */
    public void incrementEnrollmentCount() {
        this.enrollmentCount++;
    }

    /**
     * Decrements the enrollment count
     */
    public void decrementEnrollmentCount() {
        if (this.enrollmentCount > 0) {
            this.enrollmentCount--;
        }
    }

    /**
     * Increments the completion count
     */
    public void incrementCompletionCount() {
        this.completionCount++;
    }

    /**
     * Gets the completion rate
     * 
     * @return completion rate as percentage
     */
    public Double getCompletionRate() {
        if (enrollmentCount == 0) {
            return 0.0;
        }
        return (completionCount.doubleValue() / enrollmentCount.doubleValue()) * 100.0;
    }

    /**
     * Gets the total number of courses in this learning path
     * 
     * @return number of courses
     */
    public int getCourseCount() {
        return courses.size();
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

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public Integer getEstimatedDurationHours() {
        return estimatedDurationHours;
    }

    public void setEstimatedDurationHours(Integer estimatedDurationHours) {
        this.estimatedDurationHours = estimatedDurationHours;
    }

    public String getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(String difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Boolean getIsPublished() {
        return isPublished;
    }

    public void setIsPublished(Boolean isPublished) {
        this.isPublished = isPublished;
    }

    public Integer getEnrollmentCount() {
        return enrollmentCount;
    }

    public void setEnrollmentCount(Integer enrollmentCount) {
        this.enrollmentCount = enrollmentCount;
    }

    public Integer getCompletionCount() {
        return completionCount;
    }

    public void setCompletionCount(Integer completionCount) {
        this.completionCount = completionCount;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

    public Set<LearningPathEnrollment> getEnrollments() {
        return enrollments;
    }

    public void setEnrollments(Set<LearningPathEnrollment> enrollments) {
        this.enrollments = enrollments;
    }

    @Override
    public String toString() {
        return "LearningPath{" +
                "id=" + getId() +
                ", title='" + title + '\'' +
                ", creator=" + (creator != null ? creator.getFullName() : "null") +
                ", isPublished=" + isPublished +
                ", enrollmentCount=" + enrollmentCount +
                ", completionCount=" + completionCount +
                "}";
    }
}

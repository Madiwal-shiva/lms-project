package com.lms.entity;

import com.lms.enums.CourseStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Course entity representing a learning course
 * 
 * @author LMS Team
 * @version 1.0
 */
@Entity
@Table(name = "courses")
public class Course extends BaseEntity {

    @NotBlank(message = "Course title is required")
    @Size(max = 200, message = "Course title must not exceed 200 characters")
    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @NotBlank(message = "Course description is required")
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "short_description", length = 500)
    private String shortDescription;

    @NotNull(message = "Instructor is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructor_id", nullable = false)
    private User instructor;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CourseStatus status = CourseStatus.DRAFT;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Column(name = "category", length = 100)
    private String category;

    @Column(name = "level", length = 50)
    private String level;

    @Column(name = "language", length = 50)
    private String language;

    @Column(name = "duration_hours")
    private Integer durationHours;

    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "max_students")
    private Integer maxStudents;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "prerequisites", columnDefinition = "TEXT")
    private String prerequisites;

    @Column(name = "learning_objectives", columnDefinition = "TEXT")
    private String learningObjectives;

    @Column(name = "tags")
    private String tags;

    @Column(name = "is_featured", nullable = false)
    private Boolean isFeatured = false;

    @Column(name = "enrollment_count", nullable = false)
    private Integer enrollmentCount = 0;

    @Column(name = "rating")
    private Double rating;

    @Column(name = "review_count", nullable = false)
    private Integer reviewCount = 0;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<CourseContent> contents = new HashSet<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Enrollment> enrollments = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "learning_path_courses",
        joinColumns = @JoinColumn(name = "course_id"),
        inverseJoinColumns = @JoinColumn(name = "learning_path_id")
    )
    private Set<LearningPath> learningPaths = new HashSet<>();

    /**
     * Default constructor
     */
    public Course() {
        super();
    }

    /**
     * Constructor with essential fields
     * 
     * @param title Course title
     * @param description Course description
     * @param instructor Course instructor
     */
    public Course(String title, String description, User instructor) {
        this.title = title;
        this.description = description;
        this.instructor = instructor;
    }

    /**
     * Checks if the course is published
     * 
     * @return true if course is published
     */
    public boolean isPublished() {
        return status == CourseStatus.PUBLISHED;
    }

    /**
     * Checks if the course is full
     * 
     * @return true if course is full
     */
    public boolean isFull() {
        return maxStudents != null && enrollmentCount >= maxStudents;
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
     * Updates the course rating
     * 
     * @param newRating New rating value
     */
    public void updateRating(Double newRating) {
        if (this.rating == null) {
            this.rating = newRating;
        } else {
            this.rating = ((this.rating * this.reviewCount) + newRating) / (this.reviewCount + 1);
        }
        this.reviewCount++;
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

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public User getInstructor() {
        return instructor;
    }

    public void setInstructor(User instructor) {
        this.instructor = instructor;
    }

    public CourseStatus getStatus() {
        return status;
    }

    public void setStatus(CourseStatus status) {
        this.status = status;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Integer getDuration() {
        return durationHours;
    }

    public void setDuration(Integer duration) {
        this.durationHours = duration;
    }

    public Integer getDurationHours() {
        return durationHours;
    }

    public void setDurationHours(Integer durationHours) {
        this.durationHours = durationHours;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getMaxStudents() {
        return maxStudents;
    }

    public void setMaxStudents(Integer maxStudents) {
        this.maxStudents = maxStudents;
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

    public String getPrerequisites() {
        return prerequisites;
    }

    public void setPrerequisites(String prerequisites) {
        this.prerequisites = prerequisites;
    }

    public String getLearningObjectives() {
        return learningObjectives;
    }

    public void setLearningObjectives(String learningObjectives) {
        this.learningObjectives = learningObjectives;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Boolean isFeatured() {
        return isFeatured;
    }

    public void setFeatured(Boolean featured) {
        this.isFeatured = featured;
    }

    public Boolean getIsFeatured() {
        return isFeatured;
    }

    public void setIsFeatured(Boolean isFeatured) {
        this.isFeatured = isFeatured;
    }

    public Integer getEnrollmentCount() {
        return enrollmentCount;
    }

    public void setEnrollmentCount(Integer enrollmentCount) {
        this.enrollmentCount = enrollmentCount;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Integer getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(Integer reviewCount) {
        this.reviewCount = reviewCount;
    }

    public Set<CourseContent> getContents() {
        return contents;
    }

    public void setContents(Set<CourseContent> contents) {
        this.contents = contents;
    }

    public Set<Enrollment> getEnrollments() {
        return enrollments;
    }

    public void setEnrollments(Set<Enrollment> enrollments) {
        this.enrollments = enrollments;
    }

    public Set<LearningPath> getLearningPaths() {
        return learningPaths;
    }

    public void setLearningPaths(Set<LearningPath> learningPaths) {
        this.learningPaths = learningPaths;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + getId() +
                ", title='" + title + '\'' +
                ", instructor=" + (instructor != null ? instructor.getFullName() : "null") +
                ", status=" + status +
                ", enrollmentCount=" + enrollmentCount +
                '}';
    }
}

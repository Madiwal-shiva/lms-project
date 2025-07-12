package com.lms.entity;

import com.lms.enums.ContentType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;

/**
 * CourseContent entity representing course content items
 * 
 * @author LMS Team
 * @version 1.0
 */
@Entity
@Table(name = "course_content")
public class CourseContent extends BaseEntity {

    @NotBlank(message = "Content title is required")
    @Size(max = 200, message = "Content title must not exceed 200 characters")
    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @NotNull(message = "Course is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Enumerated(EnumType.STRING)
    @Column(name = "content_type", nullable = false)
    private ContentType contentType;

    @Column(name = "content_url")
    private String contentUrl;

    @Column(name = "content_data", columnDefinition = "LONGTEXT")
    private String contentData;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    @Column(name = "order_index", nullable = false)
    private Integer orderIndex;

    @Column(name = "is_free", nullable = false)
    private Boolean isFree = false;

    @Column(name = "is_downloadable", nullable = false)
    private Boolean isDownloadable = false;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "file_type")
    private String fileType;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Column(name = "transcript", columnDefinition = "LONGTEXT")
    private String transcript;

    @OneToMany(mappedBy = "content", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Progress> progressRecords = new HashSet<>();

    @OneToMany(mappedBy = "content", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Assessment> assessments = new HashSet<>();

    /**
     * Default constructor
     */
    public CourseContent() {
        super();
    }

    /**
     * Constructor with essential fields
     * 
     * @param title Content title
     * @param course The course
     * @param contentType Type of content
     * @param orderIndex Order index
     */
    public CourseContent(String title, Course course, ContentType contentType, Integer orderIndex) {
        this();
        this.title = title;
        this.course = course;
        this.contentType = contentType;
        this.orderIndex = orderIndex;
    }

    /**
     * Checks if the content is a video
     * 
     * @return true if content is video
     */
    public boolean isVideo() {
        return contentType == ContentType.VIDEO;
    }

    /**
     * Checks if the content is a document
     * 
     * @return true if content is document
     */
    public boolean isDocument() {
        return contentType == ContentType.DOCUMENT;
    }

    /**
     * Checks if the content is a quiz
     * 
     * @return true if content is quiz
     */
    public boolean isQuiz() {
        return contentType == ContentType.QUIZ;
    }

    /**
     * Gets the file size in MB
     * 
     * @return file size in MB
     */
    public Double getFileSizeInMB() {
        if (fileSize == null) {
            return null;
        }
        return fileSize / (1024.0 * 1024.0);
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

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public String getContentData() {
        return contentData;
    }

    public void setContentData(String contentData) {
        this.contentData = contentData;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    public Boolean getIsFree() {
        return isFree;
    }

    public void setIsFree(Boolean isFree) {
        this.isFree = isFree;
    }

    public Boolean getIsDownloadable() {
        return isDownloadable;
    }

    public void setIsDownloadable(Boolean isDownloadable) {
        this.isDownloadable = isDownloadable;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getTranscript() {
        return transcript;
    }

    public void setTranscript(String transcript) {
        this.transcript = transcript;
    }

    public Set<Progress> getProgressRecords() {
        return progressRecords;
    }

    public void setProgressRecords(Set<Progress> progressRecords) {
        this.progressRecords = progressRecords;
    }

    public Set<Assessment> getAssessments() {
        return assessments;
    }

    public void setAssessments(Set<Assessment> assessments) {
        this.assessments = assessments;
    }

    @Override
    public String toString() {
        return "CourseContent{" +
                "id=" + getId() +
                ", title='" + title + '\'' +
                ", course=" + (course != null ? course.getTitle() : "null") +
                ", contentType=" + contentType +
                ", orderIndex=" + orderIndex +
                ", isFree=" + isFree +
                "}";
    }
}

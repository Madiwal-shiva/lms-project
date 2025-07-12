package com.lms.entity;

import com.lms.enums.ContentType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "content_blocks")
public class ContentBlock extends BaseEntity {

    @NotNull(message = "Learning section is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id", nullable = false)
    private LearningSection section;

    @Enumerated(EnumType.STRING)
    @Column(name = "content_type", nullable = false)
    private ContentType contentType;

    @Column(name = "content", columnDefinition = "LONGTEXT")
    private String content;

    @Column(name = "content_url")
    private String contentUrl;

    @Column(name = "order_index", nullable = false)
    private Integer orderIndex;

    @Column(name = "estimated_time")
    private Integer estimatedTime;

    @Column(name = "difficulty", length = 20)
    private String difficulty;

    @Column(name = "tags")
    private String tags;

    @Column(name = "metadata", columnDefinition = "JSON")
    private String metadata;

    /**
     * Default constructor
     */
    public ContentBlock() {
        super();
    }

    /**
     * Constructor with essential fields
     *
     * @param section Learning section
     * @param contentType Content type
     * @param content Content data
     * @param orderIndex Order index
     */
    public ContentBlock(LearningSection section, ContentType contentType, String content, Integer orderIndex) {
        this.section = section;
        this.contentType = contentType;
        this.content = content;
        this.orderIndex = orderIndex;
    }

    // Getters and Setters

    public LearningSection getSection() {
        return section;
    }

    public void setSection(LearningSection section) {
        this.section = section;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
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

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    @Override
    public String toString() {
        return "ContentBlock{" +
                "id=" + getId() +
                ", section=" + (section != null ? section.getTitle() : "null") +
                ", contentType=" + contentType +
                ", orderIndex=" + orderIndex +
                '}';
    }
}

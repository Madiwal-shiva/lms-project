package com.lms.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "learning_resources")
public class LearningResource extends BaseEntity {

    @NotBlank(message = "Resource title is required")
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @NotNull(message = "Learning module is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id", nullable = false)
    private LearningModule module;

    @Column(name = "resource_type", length = 50)
    private String resourceType;

    @Column(name = "url")
    private String url;

    @Column(name = "file_size")
    private String fileSize;

    @Column(name = "download_count")
    private Integer downloadCount = 0;

    @Column(name = "is_downloadable")
    private Boolean isDownloadable = false;

    /**
     * Default constructor
     */
    public LearningResource() {
        super();
    }

    /**
     * Constructor with essential fields
     *
     * @param title Resource title
     * @param module Learning module
     * @param resourceType Resource type
     * @param url Resource URL
     */
    public LearningResource(String title, LearningModule module, String resourceType, String url) {
        this.title = title;
        this.module = module;
        this.resourceType = resourceType;
        this.url = url;
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

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public Integer getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(Integer downloadCount) {
        this.downloadCount = downloadCount;
    }

    public Boolean getIsDownloadable() {
        return isDownloadable;
    }

    public void setIsDownloadable(Boolean isDownloadable) {
        this.isDownloadable = isDownloadable;
    }

    /**
     * Increments the download count
     */
    public void incrementDownloadCount() {
        this.downloadCount++;
    }

    @Override
    public String toString() {
        return "LearningResource{" +
                "id=" + getId() +
                ", title='" + title + '\'' +
                ", module=" + (module != null ? module.getTitle() : "null") +
                ", resourceType='" + resourceType + '\'' +
                '}';
    }
}
